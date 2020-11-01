package lazy.cat.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lazy.cat.annotations.LazyMethod;
import lazy.cat.annotations.LazyObject;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import java.io.IOException;
import java.util.*;

@AutoService(Processor.class)
public class LazyObjectProcessor extends AbstractProcessor {

    private Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(LazyObject.class.getCanonicalName(), LazyMethod.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(LazyObject.class).forEach(type -> {
            TypeElement typeElement = (TypeElement) type;
            TypeSpec typeSpec = new LazyClassCreator(typeElement, roundEnv).create();
            String packageName = typeElement.getAnnotation(LazyObject.class).packageName();
            try {
                JavaFile.builder(packageName, typeSpec).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return true;
    }
}
