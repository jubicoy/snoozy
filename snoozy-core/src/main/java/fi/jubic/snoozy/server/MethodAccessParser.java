package fi.jubic.snoozy.server;

import fi.jubic.snoozy.MethodAccess;

import java.lang.reflect.Method;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

class MethodAccessParser {
    private final boolean permitAllAnonymous;

    MethodAccessParser(boolean permitAllAnonymous) {
        this.permitAllAnonymous = permitAllAnonymous;
    }

    MethodAccess parseAccess(Method method) {
        if (method.isAnnotationPresent(DenyAll.class)) {
            return MethodAccess.denyAll();
        }

        RolesAllowed methodRolesAllowed = method.getAnnotation(RolesAllowed.class);
        if (methodRolesAllowed != null && methodRolesAllowed.value().length == 0) {
            return MethodAccess.denyAll();
        }
        else if (methodRolesAllowed != null) {
            return MethodAccess.roles(methodRolesAllowed.value());
        }

        if (permitAllAnonymous) {
            if (method.isAnnotationPresent(PermitAll.class)) {
                return MethodAccess.anonymous();
            }
        }
        else {
            if (method.isAnnotationPresent(PermitAll.class)) {
                return MethodAccess.authenticated();
            }
        }

        Class<?> resource = method.getDeclaringClass();
        if (resource.isAnnotationPresent(DenyAll.class)) {
            return MethodAccess.denyAll();
        }

        RolesAllowed resourceRolesAllowed = resource.getAnnotation(RolesAllowed.class);
        if (resourceRolesAllowed != null && resourceRolesAllowed.value().length == 0) {
            return MethodAccess.denyAll();
        }
        else if (resourceRolesAllowed != null) {
            return MethodAccess.roles(resourceRolesAllowed.value());
        }

        if (permitAllAnonymous) {
            if (resource.isAnnotationPresent(PermitAll.class)) {
                return MethodAccess.anonymous();
            }
        }
        else {
            if (resource.isAnnotationPresent(PermitAll.class)) {
                return MethodAccess.authenticated();
            }
        }

        if (permitAllAnonymous) {
            return MethodAccess.authenticated();
        }
        else {
            return MethodAccess.anonymous();
        }
    }
}
