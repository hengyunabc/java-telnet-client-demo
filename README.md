# java-telnet-client-demo

## Features

* A pure java telnet client demo, connect to remote telnet server.

* Support `Tab`/`Ctrl + C`/`Ctrl + D`

* Using [jline](https://github.com/jline/jline2) and [commons-net](https://commons.apache.org/proper/commons-net/)


## How to run

```bash
mvn clean package

java -jar target/java-telnet-client-demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

You can use this demo connect to [Arthas](https://github.com/alibaba/arthas).

## Cygwin/Mingw

Under Cygwin/Mingw, need to add `-Djline.terminal=jline.UnixTerminal` vm arguments, refer to [https://github.com/jline/jline2/issues/62](https://github.com/jline/jline2/issues/62) .

Under Cygwin/Mingw, do not support `Tab`/`Ctrl + C`/`Ctrl + D`.

## License
Apache License V2
