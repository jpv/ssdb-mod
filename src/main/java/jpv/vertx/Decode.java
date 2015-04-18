package jpv.vertx;

import org.vertx.java.core.json.JsonObject;

import java.util.Iterator;

/**
 * Created by @JPVay on 18/04/2015.
 */
public class Decode {

    static public JsonObject decode(final JsonObject in) {
        JsonObject result = in.getElement("result").asObject();
        Iterator<String> it = result.getFieldNames().iterator();
        String k,v;
        byte[] b;
        while (it.hasNext()) {
            k = it.next();
            b = result.getBinary(k);
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
            v = new String( b);
            if (b.length<=8) try {
                result.putNumber(k, Double.valueOf(v));
            }
            catch (Exception e) {
                result.putString(k, v );
            }
        }
        return in;
    }
}
