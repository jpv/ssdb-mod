# [VERTX](http://vertx.io/) module pour [SSDB](http://ssdb.io/)

## Still in development. It's Java 8 baby !

# Vertx Configuration

>{
>>host : "your SSDB server"; //default :localhost"
>>port : 8888;  // default SSDB port 8888
>>address : "vertx.ssdb" //the module will receive SSDB request at the address (default vertx.ssdb)
>}

# SSDB request format

>>Example : { "command" : "multi_set", "params" : [ "mykey1", "myvalue1", "mykey2", "myvalue2 ] }
>>Example : { "command" : "multi_get", "params" : [ "mykey1", "mykey2" ] }

# SSDB response format

>>Example : { "ok" : true, result : { "mykey1" : "myvalue1", "mykey2" : "myvalue2" }}

### For the complete documentation of the SSDB request commands, see : [SSDB Commands](http://ssdb.io/docs/php/index.html)

### Test in your IDE (Idea, Eclipce i dunno) : run ModuleIntegrationTest.java

Code is not really commented, yet.
More to come ......

