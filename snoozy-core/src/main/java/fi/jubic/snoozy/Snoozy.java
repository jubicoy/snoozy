package fi.jubic.snoozy;

import fi.jubic.snoozy.converters.Converters;
import fi.jubic.snoozy.filters.UrlRewrite;
import fi.jubic.snoozy.mappers.Mappers;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Snoozy {
    public static Set<Object> builtins() {
        return Stream.concat(
                Converters.builtins().stream(),
                Mappers.builtins().stream()
        ).collect(Collectors.toSet());
    }

    public static StaticFiles defaultFrontend() {
        UrlRewrite rewrite = UrlRewrite.of(
                "^\\/(?!(((api|assets|images|fonts|css).*)|.*\\.(html|js)$)).*$",
                "/index.html"
        );

        return StaticFiles.builder()
                .setPrefix("static")
                .setClassLoader(Application.class.getClassLoader())
                .setMethodAccess(MethodAccess.anonymous())
                .setRewrite(rewrite)
                .build();

    }
}
