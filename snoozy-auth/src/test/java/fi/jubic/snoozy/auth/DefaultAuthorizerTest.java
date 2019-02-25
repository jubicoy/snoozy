package fi.jubic.snoozy.auth;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import fi.jubic.snoozy.auth.implementation.DefaultAuthorizer;
import org.junit.Test;

public class DefaultAuthorizerTest {
    private UserPrincipal userPrincipal;
    private DefaultAuthorizer<UserPrincipal> defaultAuthorizer;

    public DefaultAuthorizerTest() {
        userPrincipal = new User("Role", "Name");
        defaultAuthorizer = new DefaultAuthorizer<>();
    }

    @Test
    public void shouldReturnTrueOnMatchingRole() {
        assertTrue(defaultAuthorizer.authorize(userPrincipal, "Role"));
    }

    @Test
    public void shouldReturnFalseOnDifferingRoles() {
        assertFalse(defaultAuthorizer.authorize(userPrincipal, "Role2"));
    }

    @Test
    public void shouldReturnFalseOnMissingRole() {
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
