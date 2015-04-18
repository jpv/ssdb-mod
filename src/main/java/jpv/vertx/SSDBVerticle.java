package jpv.vertx;
/*
 * @author <a href="https://twitter.com/JPVay">@JPVay</a>
 */

import com.udpwork.ssdb.Link;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class SSDBVerticle extends Verticle {

  protected Link ssdb;



  public void start() {

    final JsonObject config = container.config();
    container.logger().info(config.encodePrettily());
    final int port = config.getInteger("port", 8888);
    final String host = config.getString("host", "localhost");
    final String address = config.getString("address", "vertx.ssdb");
    final boolean dodecode = config.getBoolean("decode", false);
    final SSDBVerticle _this = this;
    try {
      ssdb    = new Link(host,port);
      container.logger().info("Connected to host :"+ host +":"+ port+ " decode:"+dodecode);

      vertx.eventBus().registerHandler(address, new Handler<Message<JsonObject>>() {
        @Override
        public void handle(Message<JsonObject> message) {
          final JsonObject body = message.body();
          final Boolean noreply = body.getBoolean("norep");
          container.logger().debug("MODULE RECEIVED:" + body.encodePrettily());
          JsonObject response = SSDBCatalog.exec(_this,body);
          if (dodecode) {
            Decode.decode(response);
          }
          container.logger().debug("MODULE SEND:" + response.encodePrettily());
          if ((noreply==null) || (!noreply))
            message.reply(response);

        }
      });

    } catch (Exception e) {
      e.printStackTrace();
      container.logger().fatal("Error on SSDB host connection..");

    }
  }

  public void stop() {
    container.logger().info("Module SSDB is shuting down...");
    ssdb.close();
  }
}
