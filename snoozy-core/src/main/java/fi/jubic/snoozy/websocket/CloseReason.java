package fi.jubic.snoozy.websocket;

public class CloseReason {
    private final int code;
    private final String reason;

    private CloseReason(
            int code,
            String reason
    ) {
        this.code = code;
        this.reason = reason;
    }

    public static CloseReason of(int code, String reason) {
        return new CloseReason(code, reason);
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    /**
     * Close event status codes
     *
     * https://developer.mozilla.org/en-US/docs/Web/API/CloseEvent
     */
    public static class StatusCode {
        /**
         * Normal closure; the connection successfully completed whatever
         * purpose for which it was created.
         */
        public static final int NORMAL_CLOSURE = 1000;
        /**
         * The endpoint is going away, either because of a server failure or
         * because the browser is navigating away from the page that opened
         * the connection.
         */
        public static final int GOING_AWAY = 1001;
        /**
         * The endpoint is terminating the connection due to a protocol error.
         */
        public static final int PROTOCOL_ERROR = 1002;
        /**
         * The connection is being terminated because the endpoint received
         * data of a type it cannot accept (for example, a text-only endpoint
         * received binary data).
         */
        public static final int UNSUPPORTED_DATA = 1003;
        /**
         * Reserved. Indicates that no status status was provided even though
         * one was expected.
         */
        public static final int NO_STATUS_RECVD = 1005;
        /**
         * Reserved. Used to indicate that a connection was closed abnormally
         * (that is, with no close frame being sent) when a status status is
         * expected.
         */
        public static final int ABNORMAL_CLOSE = 1006;
        /**
         * The endpoint is terminating the connection because a message was
         * received that contained inconsistent data (e.g., non-UTF-8 data
         * within a text message).
         */
        public static final int INVALID_PAYLOAD_DATA = 1007;
        /**
         * The endpoint is terminating the connection because it received a
         * message that violates its policy. This is a generic status status,
         * used when codes 1003 and 1009 are not suitable.
         */
        public static final int POLICY_VIOLATION = 1008;
        /**
         * The endpoint is terminating the connection because a data frame was
         * received that is too large.
         */
        public static final int MESSAGE_TOO_BIG = 1009;
        /**
         * The client is terminating the connection because it expected the
         * server to negotiate one or more extension, but the server didn't.
         */
        public static final int MISSING_EXTENSION = 1010;
        /**
         * The server is terminating the connection because it encountered an
         * unexpected condition that prevented it from fulfilling the request.
         */
        public static final int INTERNAL_ERROR = 1011;
        /**
         * The server is terminating the connection because it is restarting.
         */
        public static final int SERVICE_RESTART = 1012;
        /**
         * The server is terminating the connection due to a temporary
         * condition, e.g. it is overloaded and is casting off some of its
         * clients.
         */
        public static final int TRY_AGAIN_LATER = 1013;
        /**
         * The server was acting as a gateway or proxy and received an invalid
         * response from the upstream server. This is similar to 502 HTTP
         * Status Code.
         */
        public static final int BAD_GATEWAY = 1014;
        /**
         * Reserved. Indicates that the connection was closed due to a failure
         * to perform a TLS handshake (e.g., the server certificate can't be
         * verified).
         */
        public static final int FAILED_TLS_HANDSHAKE = 1015;
    }

}
