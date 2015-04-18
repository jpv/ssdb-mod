package jpv.vertx.integration.java;
/*
 * @author <a href="https://twitter.com/JPVay">@JPVay</a>
 */

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.testtools.TestVerticle;

import java.util.Iterator;

import static org.vertx.testtools.VertxAssert.*;

public class ModuleIntegrationTest extends TestVerticle {


    //some SSDB commands to test
    private static String[]  COMMANDTOSEND = {
/*
            "{ \"command\" : \"set\", \"params\" : [ \"key1\", 100000.000002] }",
            "{ \"command\" : \"multi_set\", \"params\" : [ \"key2\", 10,\"key3\", 3.14, \"key4\", false] }",
            "{ \"command\" : \"get\", \"params\" : [ \"key1\" ]}" ,
            "{ \"command\" : \"multi_get\", \"params\" : [ \"key1\", \"key2\",\"key3\",\"key4\" ] }",
            "{ \"command\" : \"multi_del\", \"params\" : [ \"key1\", \"key2\",\"key3\" ] }",
            "{ \"command\" : \"incr\", \"params\"  : [  \"key3\", 100 ] }",
            "{ \"command\" : \"scan\", \"params\" : [  \"key\",\"kez\" ,10 ] }",

            "{ \"command\" : \"hset\", \"params\" : [ \"hash\", \"key1\", \"value1\"] }",
            "{ \"command\" : \"multi_hset\", \"params\" : [ \"hash\",\"key2\", 99,\"key3\", 3, \"key4\", false] }",
            "{ \"command\" : \"hget\", \"params\" : [ \"hash\",\"key1\" ]}" ,
            "{ \"command\" : \"multi_hget\", \"params\" : [ \"hash\",\"key1\", \"key2\",\"key3\" ] }",
            "{ \"command\" : \"multi_hdel\", \"params\" : [ \"hash\", \"key2\" ] }",
            "{ \"command\" : \"hscan\", \"params\" : [ \"hash\", \"key\",\"kez\" ,10 ] }",
            "{ \"command\" : \"hincr\", \"params\" : [  \"hash\",\"key3\", \"100\" ] }",
            "{ \"command\" : \"hget\", \"params\" : [  \"hash\",\"key3\" ] }",

            "{ \"command\" : \"qpush\", \"params\" : [  \"q1\",\"q1\" ] }",
            "{ \"command\" : \"qpush\", \"params\" : [  \"q2\",\"q2\" ] }",
            "{ \"command\" : \"qpop\", \"params\" : [  \"q1\" ] }",
            "{ \"command\" : \"qpop\", \"params\" : [  \"q2\" ] }",
            "{ \"command\" : \"info\", \"params\" : [   ] }",

            "{ \"command\" : \"multi_set\", \"params\" : [ \"key1\", \"value1\",\"key2\", 10] }",
     */
            "{ \"command\" : \"multi_get\", \"params\" : [ \"key100\",\"key2\"] }",

            "{ \"norep\":false, \"command\" : \"get\", \"params\" : [ \"key100\"] }"


    };


    public void send(final int m, final int n, final JsonObject dataToSend) {
        container.logger().info("CLIENT SEND:[" + n + "/" + m + "] " + dataToSend.encodePrettily());
        vertx.eventBus().send("vertx.ssdb", dataToSend, new Handler<Message<JsonObject>>() {
            public void handle(Message<JsonObject> message) {
                if (message.body().containsField("err")) {
                    container.logger().fatal("Error on "+ dataToSend);
    //                assertEquals(true, false);
                }
                container.logger().info("CLIENT RECEIVED:[" + n + "/" + m + "] \n"
                        + COMMANDTOSEND[n-1]
                        + "\n"   + message.body().encodePrettily());
               // if (n==m)
               //     testComplete();

            }
        });
    }

    @Test
    public void testSSDB() {
        JsonObject dataToSend;
        int n = 1;
        for (int i = 0; i< COMMANDTOSEND.length; i++) {
            dataToSend = new JsonObject(COMMANDTOSEND[i]);
            send(COMMANDTOSEND.length,n++,dataToSend);
        }
    }





    @Override
    public void start() {
        // Make sure we call initialize() - this sets up the assert stuff so assert functionality works correctly
        initialize();
        // Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
        // don't have to hardecode it in your tests
        container.deployModule(System.getProperty("vertx.modulename"), new AsyncResultHandler<String>() {
            @Override
            public void handle(AsyncResult<String> asyncResult) {
                // Deployment is asynchronous and this this handler will be called when it's complete (or failed)
                assertTrue(asyncResult.succeeded());
                assertNotNull("deploymentID should not be null", asyncResult.result());
                // If deployed correctly then start the tests!
                startTests();
            }
        });
    }

}
