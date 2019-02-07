package fi.jubic.resteasy.auth;

import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@EasyValue
public abstract class Auth<U extends UserPrincipal> {
    @EasyProperty
    public abstract IAuthenticator<U> authenticator();
    @EasyProperty
    @Nullable
    public abstract IAuthorizer<U> authorizer();
    @EasyProperty
    @Nullable
    public abstract ITokenParser tokenParser();
    @EasyProperty
    public abstract Class<U> userClass();
    @EasyProperty
    public abstract List<String> includes();
    @EasyProperty
    public abstract List<String> excludes();

    public abstract Builder toBuilder();

    public static <U extends UserPrincipal> Builder<U> builder() {
        return new Builder<U>()
                .setIncludes(Collections.emptyList())
                .setExcludes(Collections.emptyList());
    }

    public static class Builder<U extends UserPrincipal> extends EasyValue_Auth.Builder<U> {

    }
}
