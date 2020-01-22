# Kotlin Corda template with Braid-Server integration in Gradle

This project demonstrates the current state of play for using Braid's 
OpenAPI server within a standard Kotlin Corda template project.

## Requirements

* An installation of Java compatible with Corda 4.3
* Optionally a gradle capable IDE e.g. Intellij

## Instructions

Build the project:

```bash
./gradlew deployNodes
cd build/nodes
./runnodes
```

Then from a separate terminals start the REST end points using

```bash
./gradlew startBraidPartyA
```

Then navigate to [http://localhost:9006](http://localhost:9006).
You should see the Swagger UI. 

In another terminal repeat the process for PartyB.  
```bash
./gradlew startBraidPartyB
```

Open a browser to [http://localhost:9009](http://localhost:9009) to access the Swagger UI.

## Logging into the REST API

In the Swagger UI execute the `POST /login` with payload:

```json
{
  "user": "user1",
  "password": "test"
}
```

The method will return a token. Copy it.

In the Swagger UI, click the 'Authorize' button and paste in the token. This should provide 
you access to the protected methods.

## Try out some of the endpoints

Experiment with the following:

* `GET /network/nodes`
* `GET /network/nodes/self`
* `GET /network/notaries`
* `GET /cordapps`
* etc

## Running the Echo Flow

The flow works like this

```plantuml 
Client -> Party: EchoFlow "testMessage", the other party
Party -> OtherParty: "test-message"
OtherParty --> Party: "Hi, it's ${identity.name} echoing your message: $testMessage"
Party --> Client: the message from the other party
```

To invoke the flow, first invoke `GET /network` and copy the party identifier for the other party node.

For example:
```json
     {
        "name": "O=PartyB, L=New York, C=US",
        "owningKey": "GfHq2tTVk9z4eXgyQCYMz2H6sgahRvTH9AUpTx4HQCsiA9XscXaecKZSnP9C"
      }
```

Then invoke the `POST /cordapps/workflows/flows/com.template.flows.EchoFlow` with a payload that uses the above party identifier: 

For example:

```json
{
  "message": "hello, world!",
  "otherParty": {
        "name": "O=PartyB, L=New York, C=US",
        "owningKey": "GfHq2tTVk9z4eXgyQCYMz2H6sgahRvTH9AUpTx4HQCsiA9XscXaecKZSnP9C"
  }
}
```

This should give you a response like this:

```
Hi, it's O=PartyB, L=New York, C=US echoing your message: hello, world!
```

## Are there other ways of running Braid?

Yes, lots. As we finalise the implementation of Braid, this list may be refined. 

* [Run Open API server from Docker](https://gitlab.com/bluebank/braid/tree/master/braid-server#running-using-docker).
* [Embed braid](https://gitlab.com/bluebank/braid/blob/master/braid-corda/README.md). This gives access to the JSON-RPC streaming protocol as well. 