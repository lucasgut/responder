# Getting started with Responder POC

## About Responder POC

Responder POC is a simplified library allowing developers to create APIs using JSON and to implement their behaviour using a Java or Java-based language of their choice. It contains a basic JAX-RS application deployable to any servlet container implementing version 3.0 of the Servlet API specification.

## Deploying Responder POC

Responder POC uses the Gradle build tool to simplify the build and deployment experience. Artefacts can be deployed to embedded, local or remote Tomcat instances via the appropriate Gradle task.

### Minimum requirements

Responder POC requires the Java 8 JDK.

### Deploying Responder POC to an embedded Tomcat instance

To run Responder POC with the default embedded Tomcat instance, run the following Gradle task from the command line:

```
  gradlew build cargoRunLocal
```
  
### Deploying Responder POC to a remote Tomcat instance

To deploy Responder POC to a Tomcat instance that has been installed on another system, run the cargoDeployRemote Gradle task from the command line with additional system properties pointing to where the remote server is located:

```
  gradlew build cargoDeployRemote -Dtomcat.hostname=localhost -Dtomcat.port=8080 -Dtomcat.manager.username=tomcat -Dtomcat.manager.password=tomcat
```
  
### Deploying Responder POC to a local Tomcat instance

By default, the Gradle Cargo plugin will attempt to deploy Responder POC to a Tomcat instance located at localhost:8080, so the above task can be rewritten as follows:

```
  gradlew build cargoDeployRemote
```
  
## Using Responder POC

Responder POC is bundled with several core APIs defined in /src/main/resources/resources/core/core.json:

  GET /responder/version - return contents of generated versionInfo.json file
  
  POST /responder/add - add a list of numbers, e.g.
  
  ```
  POST /responder/add HTTP/1.1
  Host: localhost:8080
  Accept: application/json
  Content-Type: application/json
  Cache-Control: no-cache

  {
    "operands": [1,1]
  }
  ```
  
  GET /responder/customer/{id} - obtain data for a given customer

  GET /responder/dashboard/{customerId} - orchestration including a customer's name, addresses and any accounts and standing orders that are open.
  
  POST /responder/parallelTest - orchestration including the result of adding two or more given numbers (see /responder/add) and the version number of the deployed instance of Responder POC.
  
  GET /responder/address/{AddressId} - obtain a list of addresses for a given ID.
  
  GET /responder/complexCustomer/customer/{id}/address/{AddressId} - orchestration including a customer's name and an arbitrary list of addresses.
  
  GET /responder/customerAddressEmbed/customer/{id}/address/{AddressId} - orchestration returning a customer's name and an embedded list of addresses.