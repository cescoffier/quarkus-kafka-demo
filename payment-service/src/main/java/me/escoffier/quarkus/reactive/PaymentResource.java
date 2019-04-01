package me.escoffier.quarkus.reactive;

import io.smallrye.reactive.messaging.annotations.Emitter;
import io.smallrye.reactive.messaging.annotations.Stream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/payment")
public class PaymentResource {

    @Inject @Stream("payments") Emitter<String> emitter;
    private Jsonb jsonb;

    @Inject Validator validator;

    @PostConstruct
    public void init() {
        jsonb = JsonbBuilder.create();
    }

    @POST
    @Consumes("application/json")
    public Response payment(Payment payment) {
        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
        if (violations.isEmpty()) {
            emitter.send(jsonb.toJson(payment));
            return Response.accepted().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}