# Message Broker

Solving the producer consumer problem in a thread-safe manner (without the use of Priority Queue). 

There are message publishers and message consumers interacting through a message broker.

The following assumptions are made:

1. There is an equal number of MessagePublishers and MessageConsumers
2. For each MessagePublisher publishing messages of a specifc MessageType, there is a matching
MessageConsumer that consumes messages of that MessageType
3. There is no limit on the amount of messages that can be stored in the buffer/MessageBroker
     
These assumptions have led to the current implementation where graceful termination of MessageConumer/s
is achieved by the MessagePublisher/s sending a TerminationMessage once the target of number of messages
to publish is reached
     
Note that TerminationMessage/s are not counted towards total messages published and consumed, only the
messages with "regular" payload are


## Getting Started

Clone this repository.

### Prerequisites

Things you will need to have installed:
```
Java 11
```

## How to Run/Deploy

1. Import as a Gradle project in IntelliJ/Eclipse
2. Run the program (main in MessageBrokerApplication.java)

The IntelliJ IDE is recommended.

Note that you can customise parameters if needed (number of publishers and consumers, how many messages to publish etc.).

## Running the tests

The unit tests can be found in the MessageBroker/src/test folder. The JUnit testing framework was used.

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/kdbalabanov/message-broker/blob/master/LICENSE) file for details
