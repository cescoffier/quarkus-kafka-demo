package me.escoffier.quarkus.reactive;

import io.smallrye.reactive.messaging.annotations.Stream;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.StringReader;

@ApplicationScoped
@ServerEndpoint("/transactions")
public class TransactionSocket {

    @Inject @Stream("transactions") PublisherBuilder<String> stream;

    @OnOpen
    public void opening(Session session) {
        stream
                .map(tx -> Json.createReader(new StringReader(tx)).readObject())
                .filter(json -> json.getString("account").equals("1111"))
                .forEach(s -> {
                    session.getAsyncRemote().sendText(s.toString());
                })
                .run();
    }

}
