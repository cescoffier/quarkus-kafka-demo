# Quarkus Reactive Demo


## Build

```bash
mvn clean package
```

## Run / Demo


### Prerequisites

```bash
docker-compose up
```

### Payment Service

**Important topics**

* Usage of an `Emitter` to send a message to Kafka
* Kafka configuration (`application.properties)
* Usage of JSON-B to map `Payment to `String`

```bash
cd payment-service
mvn compile quarkus:dev
```

Listening on port `8081`.

Send payments with:

```bash
http :8081/payment account=1111 amount:=10
http :8081/payment account=1112 amount:=520
http :8081/payment account=1113 amount:=50
http :8081/payment account=1111 amount:=45
http :8081/payment account=1112 amount:=22
http :8081/payment account=1113 amount:=345
http :8081/payment account=1111 amount:=3
http :8081/payment account=1112 amount:=67
http :8081/payment account=1113 amount:=99
http :8081/payment account=1111 amount:=100
http :8081/payment account=1112 amount:=345
http :8081/payment account=1113 amount:=678
```

_Optional_: Use [Kafka Tool](http://www.kafkatool.com/) to check topic content:

1. Connect to localhost
2. Navigate to `Clusters -> Topics -> Transactions`
3. Click on data
4. Click on the green arrow

### Transaction Viewer

**Important topics**

* From Kafka to Websocket
* Injection of stream (`@Stream`)

```bash
cd transaction-viewer
mvn compile quarkus:dev
``` 

Listening on port `8082`

1. Open your browser to http://localhost:8082
2. By default, it shows the transactions for the account `1111`.
3. Open `me.escoffier.quarkus.reactive.TransactionSocket` and change the account to `1112`.
4. Save and refresh the browser
5. Send another payment:
```bash
http :8081/payment account=1112 amount:=1234
```
6. It should appear automatically

IMPORTANT: Do not refresh the browser, as the message have been acknowledged, you will only see the new ones.

### Fraud Detector

**Important topics**

* Processing of stream to to complex stream logic

```bash
cd fraud-detector
mvn compile quarkus:dev
``` 

Listening on port `8083`

1. Open your browser to http://localhost:8083
2. By default it detects fraud with a threshold set to 5000
3. Generate a fraud:
```bash
http :8081/payment account=1112 amount:=2234
http :8081/payment account=1112 amount:=200
http :8081/payment account=1112 amount:=300
http :8081/payment account=1112 amount:=2000
http :8081/payment account=1112 amount:=1000
```
4. The page should list it immediately
5. Edit `me.escoffier.quarkus.reactive.FraudDetector` and extend the threshold
6. Refresh the page

IMPORTANT: Do not refresh the browser, as the message have been acknowledged, you will only see the new ones.

 