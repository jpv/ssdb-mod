## [VERTX](http://vertx.io/) module pour [SSDB](http://ssdb.io/)

### Still in development : Java 8 !

### Vertx Configuration

<p>{</p>
<p>host : "your SSDB server"; //default :localhost"</p>
<p>port : 8888;  // default SSDB port 8888</p>
<p>address : "vertx.ssdb" //the module will receive SSDB request at (default vertx.ssdb)</p>
<p>}</p>

### SSDB request format

<p>Example : { "command" : "multi_set", "params" : [ "mykey1", "myvalue1", "mykey2", "myvalue2 ] }</p>
<p>Example : { "command" : "multi_get", "params" : [ "mykey1", "mykey2" ] }</p>

### SSDB response format

<p>Example : { "ok" : true, result : { "mykey1" : "myvalue1", "mykey2" : "myvalue2" }}</p>

#### For the complete documentation of the SSDB requests commands, see : [SSDB Commands](http://ssdb.io/docs/php/index.html)

#### Test in your IDE
#### Idea/InteliJ   :  run ModuleIntegrationTest.java
#### Eclipse        :  ?


#### Code is not really commented, yet. More to come ......

