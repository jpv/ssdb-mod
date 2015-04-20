#### [VERTX](http://vertx.io/) module pour [SSDB](http://ssdb.io/).
##### Vertx Configuration
    {
        host: "your SSDB server",     //default :localhost"  
        port: 8888,                   //default SSDB port 8888  
        address: "vertx.ssdb",        //default vertx.ssdb)
        decode: true                  //default : false
    }
#### SSDB request format
    Example: {"command":"multi_set", "params":[ "mykey1", "myvalue1", "mykey2", 10 ] }  
    Example: {"command":"multi_get", "params":[ "mykey1", "mykey2" ] }
#### SSDB response format (multi_get) 
    if decode==true  
        Example : { "ok":true, result:{"mykey1" : "value1", "mykey2" : 10 } }  
    if decode==false  
        Example : { "ok":true, result:{"mykey1" : "dmFsdWUx", "mykey2" : "MTA=" }}
#### Special "norep" request parameter
If you need fast "fire & forget" (UDP like) set/put command (with no need to check the reply), use a "norep" (boolean) parameter in your JSON request.

    Example:{"norep":true,"command":"multi_set","params":["mykey1","myvalue1"]}  
You will never receive a response from the module, even for a get !   
#### For the complete documentation of the SSDB requests commands, see : [SSDB Commands](http://ssdb.io/docs/php/index.html)
#### Test in your IDE
>Idea/InteliJ   :  run ModuleIntegrationTest.java  
>Eclipse        :  ?  

More to come ......  

#### Java usage for vertx app 
    JsonObject data = new JsonObject("{ \"command\" : \"get\" , \"mykey\""}  
    vertx.eventBus().send("vertx.ssdb", data, new Handler<Message<JsonObject>>() {  
            public void handle(Message<JsonObject> message) {  
                if (message.body().containsField("err")) {  
                    container.logger().fatal("Error on "+ data);  
                }  
                container.logger().info("CLIENT RECEIVED:" 
                + message.body().encodePrettily());
             }
    }             
    
