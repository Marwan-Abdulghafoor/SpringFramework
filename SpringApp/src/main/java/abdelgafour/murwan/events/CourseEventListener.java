package abdelgafour.murwan.events;

import abdelgafour.murwan.domain.Course;
import org.framework.annotation.EventListener;
import org.framework.annotation.Service;

@Service
public class CourseEventListener {
    @EventListener
    public void handleNewCourseEvent(Course course) {
        System.out.println("Attention new course has been added : " + course.getName());
    }
}
