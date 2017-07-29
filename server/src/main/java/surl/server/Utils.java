package surl.server;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    private Utils() {
    }

    interface TrioConsumer<T1, T2, T3> {
        void accept(T1 p1, T2 p2, T3 p3);
    }

    interface TrioHandler<T> extends TrioConsumer<Boolean, Throwable, T> {
    }

    public enum ErrCode {
        E133, E134, E135, E291;

        public String oops() {
            return OOPS_ERROR + name();
        }

        private static final String OOPS_ERROR = "Oops error. Please contact support@surl.com with id ";
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
