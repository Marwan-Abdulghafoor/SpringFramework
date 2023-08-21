package abdelgafour.murwan.framework.context;

import java.lang.reflect.Method;
import java.util.Map;

public class ApplicationEventPublisher {
    Map<Method, Object> listeners;

    public ApplicationEventPublisher(Map<Method, Object> listeners) {
        this.listeners = listeners;
    }

    public void publishEvent(Object object) {
        for (Method method: listeners.keySet()){
            try {
                if (method.getParameterTypes()[0].getName().contentEquals(object.getClass().getName())) {
                    method.invoke(listeners.get(method), object);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
