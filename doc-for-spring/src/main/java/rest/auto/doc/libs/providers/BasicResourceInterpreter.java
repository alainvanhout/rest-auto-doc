package rest.auto.doc.libs.providers;

import org.springframework.stereotype.Component;
import rest.auto.doc.libs.annotations.Resource;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;

@Component
public class BasicResourceInterpreter implements ResourceInterpreter {

    @Override
    public Collection<Class> getResourceInterfaces() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<Class> getResourceAnnotations() {
        return Arrays.asList(Resource.class);
    }

    @Override
    public Map<Class, BiConsumer<Field, Map<String, String>>> getFieldModifiers() {
        Map<Class, BiConsumer<Field, Map<String, String>>> map = new HashMap<>();
        map.put(NotNull.class, (f, m) -> m.put("required", "true"));
        map.put(Min.class, (f, m) -> {
            Min min = getAnnotation(f, Min.class);
            m.put("min", String.valueOf(min.value()));
        });
        map.put(Max.class, (f, m) -> {
            Max max = getAnnotation(f, Max.class);
            m.put("max", String.valueOf(max.value()));
        });
        map.put(Size.class, (f, m) -> {
            Size size = getAnnotation(f, Size.class);
            m.put("min", String.valueOf(size.min()));
            m.put("max", String.valueOf(size.max()));
        });
        return map;
    }

}
