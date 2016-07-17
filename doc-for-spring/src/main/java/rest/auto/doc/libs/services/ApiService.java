package rest.auto.doc.libs.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import rest.auto.doc.libs.ApiDocumented;
import rest.auto.doc.libs.dtos.Action;
import rest.auto.doc.libs.dtos.EndpointLibrary;
import rest.auto.doc.libs.providers.ResourceInterpreter;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
public class ApiService {

    @Autowired
    private Collection<ApiDocumented> controllers;

    @Autowired(required = false)
    private Collection<ResourceInterpreter> resourceInterpreters;

    private EndpointLibrary endpointLibrary = new EndpointLibrary();
    private List<Class> resourceInterfaces = new ArrayList<>();
    private List<Class> resourceAnnotations = new ArrayList<>();
    private Map<Class, BiConsumer<Field, Map<String, String>>> fieldModifiers = new HashMap<>();

    @PostConstruct
    private void initialize() {

        if (resourceInterpreters != null) {

            resourceInterpreters.stream()
                    .map(ResourceInterpreter::getResourceAnnotations)
                    .forEach(resourceAnnotations::addAll);

            resourceInterpreters.stream()
                    .map(ResourceInterpreter::getResourceInterfaces)
                    .forEach(resourceInterfaces::addAll);

            resourceInterpreters.stream()
                    .map(ResourceInterpreter::getFieldModifiers)
                    .forEach(fieldModifiers::putAll);
        }

        endpointLibrary = this.createEndpointLibrary(controllers);
    }

    private EndpointLibrary createEndpointLibrary(Collection<ApiDocumented> controllers) {
        EndpointLibrary endpointLibrary = new EndpointLibrary();
        Collection<Method> mappedMethods = getMappedMethods(controllers);
        Collection<Action> actions = toActions(mappedMethods);
        actions.forEach(endpointLibrary::addAction);
        return endpointLibrary;
    }

    public EndpointLibrary getEndpointLibrary() {
        return endpointLibrary;
    }

    public Collection<ApiDocumented> getControllers() {
        return controllers;
    }

    public Collection<Method> getMappedMethods(Collection<ApiDocumented> controllers) {
        return controllers.stream()
                .flatMap(c -> getMappedMethods(c).stream())
                .collect(Collectors.toList());
    }

    public Collection<Method> getMappedMethods(ApiDocumented controller) {
        return Arrays.asList(controller.getClass().getDeclaredMethods());
    }

    private Collection<Action> toActions(Collection<Method> mappedMethods) {
        return mappedMethods.stream()
                .flatMap(m -> toActions(m).stream())
                .collect(Collectors.toList());
    }

    // one mapped method could potentially map to several
    private List<Action> toActions(Method method) {
        String rootPath = toPath(method.getDeclaringClass());
        List<Action> actions = new ArrayList<>();

        List<String> httpMethods = toHttpMethods(method);
        for (String httpMethod : httpMethods) {

            String path = toPath(method);
            Map<String, ?> input = toRequestBodyMap(method);
            Map<String, ?> output = toResponseBodyMap(method);

            actions.add(new Action()
                    .path(rootPath + path)
                    .method(httpMethod)
                    .requestBody(input)
                    .responseBody(output));
        }
        return actions;
    }

    private String toPath(Method method) {
        RequestMapping[] mappings = method.getDeclaredAnnotationsByType(RequestMapping.class);
        if (mappings == null || mappings.length == 0) {
            return "";
        }
        if (mappings.length > 1) {
            throw new IllegalStateException("There should only be one RequestMapping");
        }
        String[] paths = mappings[0].value();
        if (paths != null && paths.length == 1) {
            return paths[0];
        }
        return "";
    }

    private Map<String, ?> toRequestBodyMap(Method method) {
        Class<?> parameterType = extractResourceParameters(method);
        if (parameterType == null) {
            return new HashMap<>();
        }

        if (Collection.class.isAssignableFrom(method.getReturnType())) {
            Type[] actualTypeArguments = ((ParameterizedTypeImpl) method.getGenericParameterTypes()[0]).getActualTypeArguments();
            return asCollection(fieldsToMap(((Class) actualTypeArguments[0]).getDeclaredFields()));
        } else {
            return fieldsToMap(parameterType.getDeclaredFields());
        }
    }

    private Map<String, Object> asCollection(HashMap<String, Object> fieldsMap) {
        Map<String, Object> wrapper = new LinkedHashMap<>();
        wrapper.put("type", "collection");
        wrapper.put("instance", fieldsMap);
        return wrapper;
    }

    private Class extractResourceParameters(Method method) {
        return Arrays.asList(method.getParameters()).stream()
                .filter(p -> isResourceClass(p.getType()) || hasResourceAnnotations(p.getAnnotations()))
                .map(Parameter::getType)
                .findFirst().orElse(null);
        // TODO: should this take into account that there may be more than one, even in a REST context?
    }

    private boolean isResourceClass(Class clazz) {
        if (resourceInterfaces.stream().anyMatch(i -> i.isAssignableFrom(clazz))) {
            return true;
        }
        if (hasResourceAnnotations(clazz.getAnnotations())) {
            return true;
        }
        return false;
    }

    private boolean hasResourceAnnotations(Annotation[] annotations) {
        return hasResourceAnnotations(Arrays.asList(annotations));
    }

    private boolean hasResourceAnnotations(Collection<Annotation> annotations) {
        return resourceAnnotations.stream()
                .anyMatch(annotations::contains);
    }

    private Map<String, ?> toResponseBodyMap(Method method) {
        Class<?> returnType = method.getReturnType();

        if (Collection.class.isAssignableFrom(method.getReturnType())) {
            Type[] actualTypeArguments = ((ParameterizedTypeImpl) method.getGenericReturnType()).getActualTypeArguments();
            return asCollection(fieldsToMap(((Class) actualTypeArguments[0]).getDeclaredFields()));
        } else {
            return fieldsToMap(returnType.getDeclaredFields());
        }
    }

    private HashMap<String, Object> fieldsToMap(Field[] declaredFields) {
        HashMap<String, Object> fieldsMap = new LinkedHashMap<>();
        for (Field field : declaredFields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            if (annotations == null || annotations.length == 0) {
                fieldsMap.put(field.getName(), field.getType().getSimpleName());
            } else {
                Map<String, String> fieldMap = new HashMap<>();
                fieldMap.put("type", field.getType().getSimpleName());

                for (Map.Entry<Class, BiConsumer<Field, Map<String, String>>> fieldModifier : fieldModifiers.entrySet()) {
                    if (field.isAnnotationPresent(fieldModifier.getKey())){
                        fieldModifier.getValue().accept(field, fieldMap);
                    }
                }
                fieldsMap.put(field.getName(), fieldMap);
            }
        }
        return fieldsMap;
    }

    private String toPath(Class clazz) {
        RequestMapping[] mappings = (RequestMapping[]) clazz.getDeclaredAnnotationsByType(RequestMapping.class);
        if (mappings == null || mappings.length == 0) {
            return "";
        }
        if (mappings.length > 1) {
            throw new IllegalStateException("There should only be one RequestMapping");
        }
        String[] paths = mappings[0].value();
        if (paths != null && paths.length == 1) {
            return paths[0];
        }
        return "";
    }

    private List<String> toHttpMethods(Method method) {
        RequestMapping[] mappings = method.getDeclaredAnnotationsByType(RequestMapping.class);
        if (mappings == null || mappings.length == 0) {
            return Collections.EMPTY_LIST;
        }
        if (mappings.length > 1) {
            throw new IllegalStateException("There should only be one RequestMapping");
        }
        RequestMethod[] methods = mappings[0].method();
        return Arrays.asList(methods).stream().map(RequestMethod::name).collect(Collectors.toList());
    }
}
