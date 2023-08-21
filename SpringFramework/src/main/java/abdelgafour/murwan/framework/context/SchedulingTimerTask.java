package abdelgafour.murwan.framework.context;

import java.lang.reflect.Method;
import java.util.TimerTask;

public class SchedulingTimerTask extends TimerTask {
    private Method method;
    private Object object;
    public SchedulingTimerTask(Method method, Object object) {
        this.method = method;
        this.object = object;
    }

    @Override
    public void run() {
        try {
            method.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
