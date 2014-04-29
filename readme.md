# Roo-Envers

## What is Roo-Envers?

Roo-Envers is an addon for the rapid application development tool Spring Roo.

It is being developed in a Microsoft Windows environment. You will probably make Roo-Envers work on a different OS.

## Requirements

You need Apache Maven.
Get it here: http://maven.apache.org/download.cgi

Add the binary folder path to your OS' path environment variable.

Roo-Envers is being developed with Spring Roo version 1.2.6.BUILD-SNAPSHOT.
Get it here: http://docs.spring.io/downloads/nightly/snapshot-download.php?project=ROO

Unzip the archive and you are ready to go. Add the binary folder path to your OS' path environment variable.

(At this point a restart of your system or a quick signout/login might be necessary.)

You need Roo's source code version 1.2.6.BUILD-SNAPSHOT.
Get it here: https://github.com/spring-projects/spring-roo

Open the Roo source code folder and edit the pom.xml file.

Find the following code; delete or comment (`<!-- -->`) it! The PGP signing procedure is not necessary for local development.

```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-gpg-plugin</artifactId>
	<version>1.3</version>
	<configuration>
		<useAgent>true</useAgent>
	</configuration>
	<executions>
		<execution>
			<id>sign-artifacts</id>
			<phase>verify</phase>
			<goals>
				<goal>sign</goal>
			</goals>
		</execution>
	</executions>
</plugin>
```

Open a command prompt an build Roo. Skip the tests!
```
mvn install -DskipTests
```

## Setup

## Workflow

## Demo

## ToDo

There are many many many things to do!