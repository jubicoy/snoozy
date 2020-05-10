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

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
