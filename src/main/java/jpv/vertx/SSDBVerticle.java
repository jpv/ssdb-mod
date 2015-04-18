package jpv.vertx;
/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

import com.udpwork.ssdb.Link;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.util.Iterator;


/*
This is a simple Java verticle which receives `ping` messages on the event bus and sends back `pong` replies
 */
public class SSDBVerticle extends Verticle {

  protected Link ssdb;
  private SSDBVerticle _this;

  public JsonObject decode(final JsonObject in) {
    JsonObject result = in.getElement("result").asObject();
    Iterator<String> it = result.getFieldNames().iterator();
    String k,v;
    byte[] b;
    while (it.hasNext()) {
      k = it.next();
      v = new String( b = result.getBinary(k));
      if (b.length == 1) {
        if (b[0] == 1) {
          result.putBoolean(k, true);
          continue;
        }
        if (b[0] == 0) {
          result.putBoolean(k, false);
          continue;
        }
      }
      try {
        result.putNumber(k, Double.valueOf(v));
      }
      catch (Exception e) {
        result.putString(k, v );
      }
    }
    return in;
  }

  public void start() {

    final JsonObject config = container.config();
    container.logger().info(config.encodePrettily());
    final int port = config.getInteger("port", 8888);
    final String host = config.getString("host", "localhost");
    final String address = config.getString("address", "vertx.ssdb");
    final boolean dodecode = config.getBoolean("decode",false);
    _this = this;
    try {
      ssdb    = new Link(host,port);
      container.logger().info("Connected to host :"+ host +":"+ port+ " decode:"+dodecode);

      vertx.eventBus().registerHandler(address, new Handler<Message<JsonObject>>() {
        @Override
        public void handle(Message<JsonObject> message) {
          //container.logger().info("MODULE RECEIVED:"+message.body().encodePrettily());
          JsonObject response = SSDBCatalog.exec(_this,message.body());
          if (dodecode) {
            container.logger().info("decode");
            decode(response);
          }
          container.logger().info("MODULE SEND:"+ response.encodePrettily());
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
