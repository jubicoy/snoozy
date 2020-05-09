package fi.jubic.snoozy.serversuite;

import fi.jubic.snoozy.Server;

/**
 * Test suite for verifying Server implementation compliance.
 *
 * @param <T> Server implementation under test
 */
public interface ServerTestSuite<T extends Server> extends
        ApplicationPathTest<T>,
        AuthenticatedApplicationPathTest<T>,
        MultipartTest<T>,
        SwaggerTest<T> {
}
