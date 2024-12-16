package fi.jubic.snoozy;

import fi.jubic.snoozy.converters.Converters;
import fi.jubic.snoozy.filters.UrlRewrite;
import fi.jubic.snoozy.mappers.Mappers;
import fi.jubic.snoozy.staticfiles.StaticFiles;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Snoozy {
    /**
     * Provide built-in extensions.
     *
     * <ul>
     *     <li>Parameter converters</li>
     *     <li>Mappers</li>
     * </ul>
     */
    public static Set<Object> builtins() {
        return Stream.concat(
                Converters.builtins().stream(),
                Mappers.builtins().stream()
        ).collect(Collectors.toSet());
    }

    /**
     * {@link StaticFiles} definition for the default frontend server from
     * static directory under resources.
     *
     * <ul>
     *     <li>api, assets, images, fonts and css directories are server normally</li>
     *     <li>Requests for html files are rewritten to index.html</li>
     * </ul>
     */
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
