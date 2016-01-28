%%%%%%%%%%%%%%%%
Developers Guide
%%%%%%%%%%%%%%%%

This documents provides information how to utilize the jsonrpc-ws-android library for your project.


Setup the developing environment by importing the project to Android Studio.
If you want to build the project from source, you need to import the third-party libraries via Maven by adding the following lines to the module's build.gradle file

.. code:: java
   compile 'fi.vtt.nubomedia:utilities-android:1.0.0'
   compile 'org.java-websocket:Java-WebSocket:1.3.0'
   compile 'com.thetransactioncompany:jsonrpc2-base:1.38'



Android application code

.. code:: java

   import java.net.URI;
   import fi.vtt.nubomedia.jsonrpcwsandroid.JsonRpcNotification;
   import fi.vtt.nubomedia.jsonrpcwsandroid.JsonRpcRequest;
   import fi.vtt.nubomedia.jsonrpcwsandroid.JsonRpcResponse;
   import fi.vtt.nubomedia.jsonrpcwsandroid.JsonRpcWebSocketClient;
   import fi.vtt.nubomedia.utilitiesandroid.LooperExecutor;
   
   JsonRpcWebSocketClient client = null;
   LooperExecutor executor = null;
   JsonRpcWebSocketClient.WebSocketConnectionEvents eventHandler = null;
   
   URI uri = new URI("ws://web_socket_url");
   client = new JsonRpcWebSocketClient(uri, eventHandler ,executor);

   /* connect */
   executor.execute(new Runnable() {
     public void run() {
       client.connect();
     }
   });
   
   /* send message */  
   JsonRpcRequest request = new JsonRpcRequest();
   HashMap<String, Object> namedParameters = ...;
   Integer id = new Integer(1);
   String method = "myMethod";
   
   request.setId(id);
   request.setMethod(method);
   request.setNamedParams(namedParameters);
   
   executor.execute(new Runnable() {
     public void run() {
       client.sendRequest(request);
     }
   });


   /* disconnect */
   executor.execute(new Runnable() {
     public void run() {
       client.disconnect(false);
     }
   });


Source code is available at
https://github.com/nubomedia-vtt/jsonrpc-ws-android

Support is provided through the Nubomedia VTT Public Mailing List available at
https://groups.google.com/forum/#!forum/nubomedia-vtt




