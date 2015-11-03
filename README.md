jsonrpc-ws-android
=================
This repository contains an Android library for sending JSON-RPC messages over WebSocket connection.

This project is part of [NUBOMEDIA](http://www.nubomedia.eu).

Repository structure
--------------------
This repository consists of an Android Studio library project.

Building
--------
You can import this project to your own Android Studio module using Maven by adding the following line to your module's' `build.gradle` file:
```
compile 'TBD:TBD:TBD'
```

If you want to build the project from source, you need to import add these projects:
* [https://github.com/TooTallNate/Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket)
* [http://software.dzhuvinov.com/json-rpc-2.0-base.html](http://software.dzhuvinov.com/json-rpc-2.0-base.html)

This is done via your module's `build.gradle` file by adding these lines:
```
compile 'org.java-websocket:Java-WebSocket:1.3.0'
compile 'com.thetransactioncompany:jsonrpc2-base:1.38'
```

Licensing
---------
This repository is licensed under a BSD license. See the `LICENSE` file for more information.

<!-- TODO
Contributions
-------------
-->

Support
-------
Support is provided through the [NUBOMEDIA VTT Public Mailing List](https://groups.google.com/forum/#!forum/nubomedia-vtt).

