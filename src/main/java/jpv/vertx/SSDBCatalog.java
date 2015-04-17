package jpv.vertx;

import com.udpwork.ssdb.Response;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;



/**
 * Created by jpv on 10/04/2015.
 */
class SSDBCatalog {


    public static JsonObject exec(final SSDBVerticle ssdb, final JsonObject messageBody) {

        final String command = messageBody.getString("command");
        final JsonObject out = new JsonObject();
        final JsonObject result = new JsonObject();
        final JsonArray params = messageBody.getArray("params");
        final int n = (params == null) ? 0 : params.size();
        int ok = 0;
        final byte[][] bytes = new byte[n][];
        out.putObject("result", result);
            Object o;
        try {
            for (int i = 0; i < n; i++) {
                o = params.get(i);
                if (o instanceof String) {
                    bytes[i] = ((String) o).getBytes();
                }
                else if (o instanceof Boolean) {
                    bytes[i] = params.get(i) ? new byte[]{1} : new byte[]{0};
                }
                else if (o instanceof Character) {
                    bytes[i] = params.get(i);
                }
                else
                    bytes[i] = String.valueOf(params.get(i)).getBytes(); //pas terrible mon gars

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
