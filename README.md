### [VERTX](http://vertx.io/) module pour [SSDB](http://ssdb.io/)

#### Still in development : Java 8 !

#### Vertx Configuration

<p>{</p>
>><p>host>: "your SSDB server"; //default :localhost"</p>
>><p>port>: 8888;  // default SSDB port 8888</p>
>><p>address>: "vertx.ssdb" //the module will receive SSDB request at (default vertx.ssdb)</p>
>><p>decode>: true // default : false
>><p>}</p>

#### SSDB request format


<p>Example : { "command" : "multi_set", "params" : [ "mykey1", "myvalue1", "mykey2", 10 ] }</p>
<p>Example : { "command" : "multi_get", "params" : [ "mykey1", "mykey2" ] }</p>

#### SSDB response format

if decode==true
>><p>Example : { "ok" : true, result : { "mykey1" : "value1", "mykey2" : 10 }}</p>

if decode==false
>><p>Example : { "ok" : true, result : { "mykey1" : "dmFsdWUx", "mykey2" : "MTA=" }}</p>


#### For the complete documentation of the SSDB requests commands, see : [SSDB Commands](http://ssdb.io/docs/php/index.html)

#### Test in your IDE
<p>Idea/InteliJ   :  run ModuleIntegrationTest.java</p>
<p>Eclipse        :  ?</p>


<p>Code is not really commented, yet. More to come ......</p>

#### Java usage for vertx app 

JsonObject data = new JsonObject("{ \"command\" : \"get\" , \"mykey\""}
vertx.eventBus().send("vertx.ssdb", dataToSend, new Handler<Message<JsonObject>>() {
            public void handle(Message<JsonObject> message) {
                if (message.body().containsField("err")) {
                    container.logger().fatal("Error on "+ dataToSend);
                    assertEquals(true, false);
                }
                container.logger().info("CLIENT RECEIVED:[" + n + "/" + m + "] \n"
                        + COMMANDTOSEND[n-1]
                        + "\n"   + message.body().encodePrettily());