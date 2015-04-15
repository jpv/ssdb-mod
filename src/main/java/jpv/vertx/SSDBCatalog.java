package jpv.vertx;

import com.udpwork.ssdb.Response;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * Created by jpv on 10/04/2015.
 */
public class SSDBCatalog {


    public final static JsonObject exec(final SSDBVerticle ssdb, final JsonObject messageBody) {

        final String command = messageBody.getString("command");
        final JsonObject out = new JsonObject();
        final JsonObject result = new JsonObject();
        out.putObject("result", result);
        final JsonArray params = messageBody.getArray("params");
        final int n = (params == null) ? 0 : params.size();
        int ok = 0;
        try {
            final byte[][] bytes = new byte[n][];
            JsonObject json;
            Object o;
            for (int i = 0; i < n; i++) {
                o = params.get(i);
                if (o instanceof String)
                    bytes[i] = ((String) o).getBytes();
                else if (o instanceof Integer)
                    bytes[i] = new Buffer().appendInt((int) o).getBytes();
                else if (o instanceof Double)
                    bytes[i] = new Buffer().appendDouble((double) params.get(i)).getBytes();
                else if (o instanceof Long)
                    bytes[i] = new Buffer().appendLong((long) params.get(i)).getBytes();
                else if (o instanceof Boolean)
                    bytes[i] =   params.get(i) ? new byte[]{1} : new byte[]{0};

                else
                    bytes[i] = params.get(i);
            }
            Response res = ssdb.ssdb.request(command, bytes);
            if (res.raw.size() == 2) {
                result.putBinary("value", res.raw.get(1));
                ok++;

            }
            else for (int i = 1; i < res.raw.size() - 1; i += 2) {
                result.putBinary(new String(res.raw.get(i)), res.raw.get(i + 1));
                ok++;
            }
            out.putNumber("ok", ok);
            return out;
        } catch (Exception e) {
            out.putNumber("ok", -1);
            e.printStackTrace();
            return out;
        }
    }

}
