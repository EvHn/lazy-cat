package lazy.cat.processor;

import com.squareup.javapoet.*;
import lazy.cat.annotations.LazyMethod;
import lazy.cat.annotations.LazyObject;
import lazy.cat.cache.ObjectCache;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LazyClassCreator {
    private final TypeElement type;
    private final RoundEnvironment roundEnv;
    private final List<TypeName> possibleTypes;

    public LazyClassCreator(TypeElement type, RoundEnvironment roundEnv) {
        this.type = type;
        this.roundEnv = roundEnv;
        possibleTypes = List.of(TypeName.get(Integer.class), TypeName.get(Long.class), TypeName.get(Double.class),
                TypeName.get(Float.class), TypeName.get(Short.class), TypeName.get(Byte.class),
                TypeName.get(Number.class), TypeName.get(String.class), TypeName.get(int.class),
                TypeName.get(long.class), TypeName.get(double.class), TypeName.get(float.class),
                TypeName.get(short.class), TypeName.get(byte.class), TypeName.get(void.class));
    }

    public boolean isPossibleReturnType(TypeMirror typeMirror) {
        return possibleTypes.contains(TypeName.get(typeMirror));
    }

    public TypeSpec create() throws LazyObjectProcessorException {
        LazyObject lazyObject = type.getAnnotation(LazyObject.class);
        List<? extends Element> methods = type.getEnclosedElements();
        Map<String, MethodSpec> methodSpecs = roundEnv.getElementsAnnotatedWith(LazyMethod.class).stream()
                .map(m -> (ExecutableElement) m)
                .filter(methods::contains)
                .collect(Collectors.toMap(Object::toString, this::createLazyMethod));
        List<MethodSpec> constructors = ElementFilter.constructorsIn(methods).stream()
                .map(this::createConstructors).collect(Collectors.toList());
        return TypeSpec.classBuilder(lazyObject.prefix() + type.getSimpleName() + lazyObject.postfix())
                .addModifiers(Modifier.PUBLIC)
                .superclass(type.asType())
                .addField(TypeName.get(ObjectCache.class), "cache", Modifier.PRIVATE)
                .addMethods(constructors)
                .addMethod(createInitMethod(methodSpecs.keySet()))
                .addMethod(createLazyCallMethod())
                .addMethods(methodSpecs.values()).build();
    }

    public MethodSpec createLazyMethod(ExecutableElement method) {
        TypeMirror typeMirror = method.getReturnType();
        if(!isPossibleReturnType(typeMirror))
            throw new LazyObjectProcessorException(String.format("Invalid return type: %s", TypeName.get(typeMirror)));
        String params = method.getParameters().stream().map(VariableElement::getSimpleName)
                .map(Object::toString).reduce(this::reduceParams).orElse("");
        return MethodSpec.overriding(method)
                .addStatement("java.util.List<Object> list = List.of($N)", params)
                .addStatement("return ($N)lazyCall($S, list, () -> super.$N($N))", typeMirror.toString(),
                        method.toString(), method.getSimpleName(), params)
                .build();
    }

    public MethodSpec createConstructors(ExecutableElement el) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder();
        String params = el.getParameters().stream()
                .peek(p -> builder.addParameter(TypeName.get(p.asType()), p.toString()))
                .map(Objects::toString).reduce(this::reduceParams).orElse("");
        return builder
                .addStatement("super($N)", params)
                .addStatement("init()").build();
    }

    public MethodSpec createInitMethod(Set<String> methodNames) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PRIVATE)
                .addStatement("cache = new lazy.cat.cache.ObjectCache()");
        methodNames.forEach(methodName -> builder.addStatement("cache.addMethod($S)", methodName));
        return builder.build();
    }

    public MethodSpec createLazyCallMethod() {
        return MethodSpec.methodBuilder("lazyCall")
                .addModifiers(Modifier.PRIVATE)
                .returns(TypeName.get(Object.class))
                .addParameter(ClassName.get(String.class), "methodName")
                .addParameter(ClassName.get(List.class), "list")
                .addParameter(ClassName.get(Supplier.class), "supplier")
                .addStatement("java.util.Optional<Object> opt = cache.get(methodName, list)")
                .addStatement("if(opt.isPresent()) return opt.get()")
                .addStatement("Object obj = supplier.get()")
                .addStatement("cache.put(methodName, list, obj)")
                .addStatement("return obj")
                .build();
    }

    public String reduceParams(String val1, String val2) {
        return val1 + ", " + val2;
    }
}
