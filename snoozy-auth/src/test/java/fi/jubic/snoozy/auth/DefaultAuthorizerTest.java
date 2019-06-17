package fi.jubic.snoozy.auth;

import fi.jubic.snoozy.auth.implementation.DefaultAuthorizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultAuthorizerTest {
    private UserPrincipal userPrincipal;
    private DefaultAuthorizer<UserPrincipal> defaultAuthorizer;

    DefaultAuthorizerTest() {
        userPrincipal = new User("Role", "Name");
        defaultAuthorizer = new DefaultAuthorizer<>();
    }

    @Test
    void shouldReturnTrueOnMatchingRole() {
        assertTrue(defaultAuthorizer.authorize(userPrincipal, "Role"));
    }

    @Test
    void shouldReturnFalseOnDifferingRoles() {
        assertFalse(defaultAuthorizer.authorize(userPrincipal, "Role2"));
    }

    @Test
    void shouldReturnFalseOnMissingRole() {
        UserPrincipal user = new User(null, "");

        assertFalse(defaultAuthorizer.authorize(user, "Role"));
    }

    private class User implements UserPrincipal {
        private String role;
        private String name;

        User(String role, String name) {
            this.role = role;
            this.name = name;
        }

        @Override
        public String getRole() {
            return role;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
