package surl.server;

public class Utils {

    public enum ErrCode {
        E133, E134, E135;

        public String oops() {
            return OOPS_ERROR + name();
        }

        private static final String OOPS_ERROR = "Oops error. Please contact support@surl.com with id ";
    }

    private Utils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
