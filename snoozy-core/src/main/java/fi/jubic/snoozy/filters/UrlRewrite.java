package fi.jubic.snoozy.filters;

public class UrlRewrite {
    private final String from;
    private final String to;

    private UrlRewrite(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public static UrlRewrite of(String from, String to) {
        return new UrlRewrite(from, to);
    }

    @Deprecated
    public String from() {
        return getFrom();
    }

    public String getFrom() {
        return from;
    }

    @Deprecated
    public String to() {
        return getTo();
    }

    public String getTo() {
        return to;
    }

    @Deprecated
    public Builder toBuilder() {
        return new Builder(from, to);
    }

    @Deprecated
    public static Builder builder() {
        return new Builder();
    }

    @Deprecated
    public static class Builder {
        private final String from;
        private final String to;

        private Builder() {
            this(null, null);
        }

        private Builder(String from, String to) {
            this.from = from;
            this.to = to;
        }

        public Builder setFrom(String from) {
            return new Builder(from, to);
        }

        public Builder setTo(String to) {
            return new Builder(from, to);
        }

        public UrlRewrite build() {
            return new UrlRewrite(from, to);
        }
    }
}
