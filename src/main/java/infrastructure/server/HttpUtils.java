package infrastructure.server;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class HttpUtils {
    private static final String DELIMITER = "/";
    private static final String AND= "&";
    private static final String EQUALS = "=";

    public static void sendResponse(HttpExchange exchange, byte[] bytes) throws IOException {
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }


    public static List<String> getAndCleanPathVariables(String path) {
        var pathVariables = new ArrayList<>(List.of(path.split(DELIMITER)));
        pathVariables.removeAll(Arrays.asList("", null));
        return pathVariables;
    }

    public static Map<String, String> getQueryParametersMap(String queryParameters) {
        var params = queryParameters.split(AND);
        var map = new HashMap<String, String>();
        for (var param : params) {
            var name = param.split(EQUALS)[0];
            var value = param.split(EQUALS)[1];
            map.put(name, value);

        }
        return map;
    }
}
