# Roo-Envers

## What is Roo-Envers?

Roo-Envers is an addon for the rapid application development tool Spring Roo. It adds Hibernate Envers support to entities of your Roo project. New controllers, finders and views are generated to access audited objects.

It is being developed in a Microsoft Windows environment. You will probably make Roo-Envers work on a different OS.

If there are any questions or problems please send me a message: haertel.dev@gmail.com

## Usage

At the moment Roo-Envers depends on Web MVC; follow these Roo commands:

```
web mvc setup
entity jpa --class SomeEntity
[add attributes and more entities]
web mvc all --package some.package.path
envers setup
envers add --type ~.SomeEntity --package some.package.path
[repeat for all entities]
```

## Requirements

You need Apache Maven.
Get it here: http://maven.apache.org/download.cgi

Roo-Envers is being developed with Spring Roo version 1.2.6.BUILD-SNAPSHOT.
Get it here: http://docs.spring.io/downloads/nightly/snapshot-download.php?project=ROO

Unzip the archive and you are ready to go.

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

Open a command prompt in Roo-Envers' project folder and run `mvn install`.

Open the file startPlugin.roo. Modify the file path so it matches the Roo-Envers .jar file path on your system. You can find the .jar in the target folder inside the Roo-Envers project folder.

(Hint: If Roo-Envers' version number changes during development you will need to modify the file path since the name of the .jar file changes.)

Run startPlugin.bat.

Done!

## Demo

Inside the Roo-Envers project folder exists a very simple demo project. Run update.bat and afterwards run in a command prompt `mvn jetty:run` or `mvn tomcat:run`. Open a browser and have a look at the application: http://localhost:8080

## ToDo

There are many many many things to do!

* Implement controller generation
* Implement finder generation
* Implement new commands, apply Roo-Envers to all entities
* Make it fool-proof
* Make it work without web mvc, basic database support only
* Detect the existing web mvc package to omit the Roo-Envers package attribute
* ...