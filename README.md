# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

### Phase 2 Sequence Diagram URL

https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBkInIZSqjxDKbPkQArmGHKQY3BMbTmM7mADiwALYo9Ko2qbwoKfimt2HB1ZJcAUhBIaF72So70-qYwACIoCChg4SK+TtGksfzAAIIgBlxcmAAm2QBGwFwoMMVlmJjsUNrYMADEaMBUAJ6sHBZ8AmhtAO4AFkjJmIiopAC0AHzkSpQAXDAA2gAKAPJkACoAujAA9FpVUAA6aADeZ5QddgA0MLiFw9DFzyg2wEgIAL6YPSUGALXqcHgDJCCHwoNZuCHJKAACjuUAeKGery47ygnxg31+CAAlJg2IioTCIgZQYssmBcvkUIU1uwkgBVc6o84Y0n0xkFLi0mplNZkACiMnFcD2MDRGJgADMmjY5edMPy8oLQWDyf1+NDFKk4TA0FoEAgyX1IQaqcbhZqmSyYCA3B5OZRufdbJiXpUcR8+dktcyhWDamsAJIAOQlLFl8p9WP9uPxhL+MBje22atoeptg1hDuDTq4a1d7mSOR0oy96J9QYZIcKwojmdj4vjufrTxgwBrewgAGt0O3s32axqS9rZrrrbxbUb9Cb+2BRoOR2grRTF0XZ4tgVA1qv18P0EDlrB9-BkOgwGsAEwABifV2uJ436EB6GK9UazTaaBpDZGA5AUEZxkmaY72FQ81i2XZDhOKpCkNN9E17bFU0BQ9hTAkJYTWBB5BCOsMWTN5A0wfCl1EYsm1LVkkk2FMPjIhspwYmdFjbCUpRlP1KLxJUVW7TiBVDHVFhowianKSoUBYoTfxk6lqjBVTjTWGgoCQRUuiUgM8WRLCPmeUoGQqKpDNTUlNOXeiJOdCsPGrNd2LsRsnLDHjRXbOMEx5JMJzXT8hizHMT3E5sww0kjaIMY8BzPLd7Lo684JC09NwvagQWvaCMEfF90KC3sPxS780F-Bomi0FpWkaFBRzA7RmFaMYJjEQrmDBTL1liSVxT2cUjmOFCuDQjpkpy3C4vYNrZOIhadGRCrNzszQdD3Olp1DNYQnOMA3Nrdb0C8mLWz8mMAu7cisrCsdIsnR0Z3mxa1LWM0LWorbdDUqSljyo9vstOawR64qn0wH8-zqhq3GKUCLBgMIhQ6yDutvOYD0veDLGGsb2B9K4zq3ObpIsMJZLZKwfXMHg1pm87qKpn0driPbnVptGmdClKLtLK6xSGgTibsIpXsk-cGbp7xPpgcXmVZnhqYB-dMqVopwcWSGYGfV8bi1-4YBh6q4YA1psC0KBsESVxK2qMIIK6qZsd63HgfgnZ9iJknpv5zc3yV6MfRwy9hTgN1kjVrTFaSMJkRDjio8d2OHLBKXuYTn0k59UPPOioXwz8vjpVlJXI1iESIFVLWi7e3Xo88dmFcr2Ipmb9P0oWTWfSr3KdMBvWDeD-vYhNs2av-eq2i6BJiOGGAghCVGfTaMoEFAIc3ZmD2gZ0732SQ44ldJ5m0DfHq4AgYioGedvw+B4UV9CVu4553Pk8L1-u5pTOuZlhdKMFAIAhxhHFAADwsFwPOdgC4oEFtxEUIt+IV3HjXOuPpJaAMBr-d+y5WQ+igTAzA+D5b2gWFnMswBijFA9PgCAid27PAwr6EAt9oBIOlr5FYN1OyBW9L2Dhd8nqK3Hg3aWCxyEpEIaDMhwQ34UIzr3PG8idY3j3lDKeFtZ6NT4D0RA7hYDAGwHbQgi4XZQXdrBPGGxBpShGmNWog98rzGoWsYoCQkgoGRNwlsJc1gOOGuKcREtWHnC4M8MmOCuJSPcd45Isl5HxESEk9Wu04nOWbidDyiDJEBN4f5ARd1gpkzEVFahgNUk+OSeaS0NT0mUL6nY9REcCru20bDIAA
