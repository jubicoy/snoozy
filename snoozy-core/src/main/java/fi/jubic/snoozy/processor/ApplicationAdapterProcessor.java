package fi.jubic.snoozy.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import fi.jubic.snoozy.Application;
import fi.jubic.snoozy.auth.AuthenticatedApplication;
import fi.jubic.snoozy.server.ApplicationAdapter;
import fi.jubic.snoozy.server.AuthFilterAdapter;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.ws.rs.ApplicationPath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"javax.ws.rs.ApplicationPath"})
public class ApplicationAdapterProcessor extends AbstractProcessor {
    private Types types;
    private Elements elements;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment environment) {
        super.init(environment);

        types = environment.getTypeUtils();
        elements = environment.getElementUtils();
        filer = environment.getFiler();
    }

    @Override
    public boolean process(
            Set<? extends TypeElement> set,
            RoundEnvironment roundEnvironment
    ) {
        TypeMirror applicationType = elements.getTypeElement(
                Application.class.getName()
        ).asType();

        roundEnvironment.getElementsAnnotatedWith(ApplicationPath.class)
                .stream()
                .filter(
                        e -> e.getKind().isClass()
                                && types.isSubtype(e.asType(), applicationType)
                )
                .forEach(this::writeAnnotation);

        return true;
    }

    private TypeSpec generateAdapter(String name, String path) {
        String className = name + "Adapter";

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(
                        AnnotationSpec.builder(ApplicationPath.class)
                                .addMember(
                                        "value",
                                        "$S",
                                        path
                                )
                                .build()
                )
                .superclass(ClassName.get(ApplicationAdapter.class))
                .addMethod(
                        MethodSpec.constructorBuilder()
                                .addParameter(
                                        ClassName.get(Application.class),
                                        "application"
                                )
                                .addModifiers(Modifier.PUBLIC)
                                .addStatement("super($L)", "application")
                                .build()
                )
                .addMethod(
                        MethodSpec.constructorBuilder()
                                .addParameter(
                                        ClassName.get(
                                                AuthenticatedApplication.class
                                        ),
                                        "application"
                                )
                                .addParameter(
                                        ClassName.get(
                                                AuthFilterAdapter.class
                                        ),
                                        "authFilterAdapter"
                                )
                                .addModifiers(Modifier.PUBLIC)
                                .addStatement(
                                        "super($L, $L)",
                                        "application",
                                        "authFilterAdapter"
                                )
                                .build()
                )
                .build();
    }

    private <T extends Element> void writeAnnotation(T element) {
        String name;
        {
            List<String> elementPath = new ArrayList<>();
            Element enclosing = element.getEnclosingElement();
            ElementKind enclosingKind = enclosing.getKind();
            while (
                    enclosingKind == ElementKind.CLASS
                            || enclosingKind == ElementKind.INTERFACE
                            || enclosingKind == ElementKind.ENUM
            ) {
                elementPath.add(enclosing.getSimpleName().toString());
                enclosing = enclosing.getEnclosingElement();
                enclosingKind = enclosing.getKind();
            }

            elementPath.add(element.getSimpleName().toString());

            name = String.join("$", elementPath);
        }

        String path = element.getAnnotation(ApplicationPath.class).value();
        TypeSpec typeSpec = generateAdapter(name, path);
        String packageName = elements.getPackageOf(element)
                .getQualifiedName()
                .toString();

        try {
            JavaFile.builder(packageName, typeSpec)
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}