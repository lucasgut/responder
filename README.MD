# Getting started with Responder POC

## About Responder POC

Responder POC is a simplified library allowing developers to create APIs using JSON and to implement their behaviour using a Java or Java-based language of their choice. It contains a basic JAX-RS application that is deployable to any servlet container conforming to version 3.0 of the Servlet API specification.

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

Responder POC is bundled with three core APIs defined in /src/main/resources/resources/core/core.json:

  GET /version
  
  POST /add
  
  GET /customer{id}

