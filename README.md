# TwinoTask

## What you’ll need

* JDK 1.8 or later (http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Gradle 2.3+  (https://gradle.org/gradle-download/)

## Build jar
Build an executable JAR:
<pre>$ gradle build</pre>

## Run application
You can run the JAR file:
<pre>$ java -jar build/libs/TwinoTask-0.0.1.jar</pre>

If you are using Gradle, you can run the application using:
<pre>$ gradle bootRun</pre>

By default the application starts on *http://localhost:8080*.

## Test
Run tests:
<pre>$ gradle test</pre>

Try working with the application by curl.<br>
See curl's scrips in the folder **curl**.
