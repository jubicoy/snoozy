package fi.jubic.snoozy.auth;

import fi.jubic.easyvalue.EasyProperty;
import fi.jubic.easyvalue.EasyValue;

@EasyValue(excludeJson = true)
public abstract class Authentication<P extends UserPrincipal> {
    @EasyProperty
    public abstract Authenticator<P> authenticator();
    @EasyProperty
    public abstract Authorizer<P> authorizer();
    @EasyProperty
    public abstract TokenParser tokenParser();
    @EasyProperty
    public abstract Class<P> userClass();

    public static <P extends UserPrincipal> Builder<P> builder() {
        return new Builder<>();
    }

    public static class Builder<P extends UserPrincipal> extends EasyValue_Authentication.Builder<P> {

    }
}
