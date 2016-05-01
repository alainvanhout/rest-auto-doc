package rest.auto.doc.providers;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;

public interface ResourceInterpreter {

    default Collection<Class> getResourceInterfaces(){
        return Collections.EMPTY_LIST;
    }

    default Collection<Class> getResourceAnnotations(){
        return Collections.EMPTY_LIST;
    }

    default Map<Class, BiConsumer<Field, Map<String, String>>> getFieldModifiers() {
        return Collections.EMPTY_MAP;
    }

    default <T> T getAnnotation(Field field, Class clazz) {
        return (T)(field.getAnnotationsByType(clazz)[0]);
    }
}
