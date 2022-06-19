package fi.jubic.snoozy.test;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION")
public interface UriBuilderConsumer {
    void consume(UriBuilder uriBuilder) throws Exception;
}
