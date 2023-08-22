package abdelgafour.murwan.events;

import abdelgafour.murwan.domain.Course;
import abdelgafour.murwan.framework.annotation.EventListener;
import abdelgafour.murwan.framework.annotation.Service;


@Service
public class CourseEventListener {
    @EventListener
    public void handleNewCourseEvent(Course course) {
        System.out.println("Attention new course has been added : " + course.getName());
    }
}
