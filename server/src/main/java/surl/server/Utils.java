package surl.server;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    private Utils() {
    }

    public enum ErrCode {
        E133, E134, E135, E291;

        public String oops() {
            return "Oops error. Please contact support@surl with id " + name();
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
