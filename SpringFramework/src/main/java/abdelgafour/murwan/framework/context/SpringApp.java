package abdelgafour.murwan.framework.context;

import org.framework.annotation.*;
import org.reflections.Reflections;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class SpringApp {
    static Map<String, Object> serviceObjects = new HashMap<>();
    private static Map<Method, Object> eventListeners = new HashMap<>();

    public static void start(Class<?> application) {
        instantiateClassesAnnotatedWithServiceAnnotation(application);
        applyConstructorInjection();
        applyMethodInjection();
        applyFieldInjection();
        checkIfRunnable(application);

    }

    private static void checkIfRunnable(Class<?> application) {
        try {
            Object applicationObject = serviceObjects.get(application.getName());
            if (applicationObject instanceof Runnable){
                ((Runnable) applicationObject).run();
            }
        }catch (Exception e){
            new RuntimeException(e);
        }
    }

    private static void applyConstructorInjection() {
        try {
            for (Object serviceObject : serviceObjects.values()) {
                for (Constructor constructor : serviceObject.getClass().getDeclaredConstructors()) {
                    if (constructor.isAnnotationPresent(Autowired.class)) {
                        Parameter parameter = constructor.getParameters()[0];
                        Class<?> parameterType = parameter.getType();
                        Object service = getObjectFromMapByClass(parameterType);
                        constructor.setAccessible(true);
                        Object newInstance = constructor.newInstance(service);
                        serviceObjects.put(serviceObject.getClass().getName(), newInstance);
                    }
                }
            }
        } catch (Exception e) {
            new RuntimeException(e);
        }
    }

    private static void applyMethodInjection() {
        try {
            for (Object serviceObject : serviceObjects.values()) {
                for (Method method : serviceObject.getClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Autowired.class)) {
                        for (Parameter parameter : method.getParameters()) {
                            Class<?> parameterType = parameter.getType();
                            Object service = getObjectFromMapByClass(parameterType);
                            method.invoke(serviceObject, service);
                        }
                    }
                    if (method.isAnnotationPresent(Scheduled.class)) {
                        int period = method.getAnnotation(Scheduled.class).fixedRate();
                        int delay = method.getAnnotation(Scheduled.class).delay();
                        SchedulingTimerTask schedulingTimerTask = new SchedulingTimerTask(method, serviceObject);
                        Timer timer = new Timer();
                        if (!method.getAnnotation(Scheduled.class).cron().contentEquals("")) {
                            String cron = method.getAnnotation(Scheduled.class).cron();
                            int sec = Character.getNumericValue(cron.charAt(0));
                            int min = Character.getNumericValue(cron.charAt(2));
                            period = (min * 60 + sec) * 1000;
                        }
                        timer.scheduleAtFixedRate(schedulingTimerTask, delay, period);
                    }
                    if (method.isAnnotationPresent(EventListener.class)) {
                        eventListeners.put(method, serviceObject);
                    }
                }
            }
            {
                Constructor constructor = ApplicationEventPublisher.class.getDeclaredConstructors()[0];
                serviceObjects.put(ApplicationEventPublisher.class.getName(), (Object) constructor.newInstance(eventListeners));
            }
        } catch (Exception e) {
            new RuntimeException(e);
        }
    }

    private static void instantiateClassesAnnotatedWithServiceAnnotation(Class<?> application) {
        try {
            Object applicationObject = application.getDeclaredConstructor().newInstance();
            serviceObjects.put(application.getName(), applicationObject);
            Reflections reflections = new Reflections(application.getPackageName());
            Set<Class<?>> serviceClasses = reflections.getTypesAnnotatedWith(Service.class);
            for (Class<?> serviceClass : serviceClasses) {
                if (serviceClass.isAnnotationPresent(Profile.class)) {
                    String profile = serviceClass.getAnnotation(Profile.class).value();
                    String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(rootPath + "/application.properties"));
                    String variable = properties.getProperty("mySpring.profile.active");
                    if (profile.equals(variable)) {
                        Object serviceObject = serviceClass.getDeclaredConstructor().newInstance();
                        serviceObjects.put(serviceObject.getClass().getName(), serviceObject);
                    }
                    continue;
                }
                Object serviceObject = serviceClass.getDeclaredConstructor().newInstance();
                serviceObjects.put(serviceObject.getClass().getName(), serviceObject);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void applyFieldInjection() {
        try {
            for (Object serviceObject : serviceObjects.values()) {
                for (Field field : serviceObject.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Class<?> fieldType = field.getType();
                        Object service = null;
                        if (serviceObjects.get(fieldType.getName()) != null) {
                            service = serviceObjects.get(fieldType.getName());
                        } else {
                            if (field.isAnnotationPresent(Qualifier.class)) {
                                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                                for (String serviceObject1 : serviceObjects.keySet()) {
                                    String className = serviceObject1.split("\\.")[serviceObject1.split("\\.").length - 1];
                                    if (className.equals(qualifier.value())) {
                                        service = serviceObjects.get(serviceObject1);
                                    }
                                }
                            } else {
                                for (Object serviceObject1 : serviceObjects.values()) {
                                    if (service == null) {
                                        Class<?>[] interfaces = serviceObject1.getClass().getInterfaces();
                                        for (Class<?> classInterface : interfaces) {
                                            if (fieldType.getName() == classInterface.getName()) {
                                                service = serviceObject1;
                                                break;
                                            }
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                        field.setAccessible(true);
                        field.set(serviceObject, service);
                    }
                    if (field.isAnnotationPresent(Value.class)) {
                        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(rootPath + "/application.properties"));
                        String variable = properties.getProperty(field.getAnnotation(Value.class).value());
                        field.setAccessible(true);
                        field.set(serviceObject, variable);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getObjectFromMapByClass(Class<?> aClass){
        Object service = null;
        if (serviceObjects.get(aClass.getName()) != null) {
            service = serviceObjects.get(aClass.getName());
        } else {
            for (Object serviceObject1 : serviceObjects.values()) {
                if (service == null) {
                    for (Class<?> classInterface : serviceObject1.getClass().getInterfaces()) {
                        if (classInterface.getName() == aClass.getName()) {
                            service = serviceObject1;
                            break;
                        }
                    }
                } else {
                    break;
                }
            }
        }
        return service;
    }
}
