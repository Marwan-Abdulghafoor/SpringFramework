package abdelgafour.murwan;

import abdelgafour.murwan.domain.Course;
import abdelgafour.murwan.domain.Student;
import abdelgafour.murwan.services.CourseService;
import abdelgafour.murwan.services.ReminderService;
import org.framework.annotation.Autowired;
import org.framework.annotation.Value;
import org.framework.context.SpringApp;

public class Main implements Runnable{
    @Autowired
    private CourseService courseService;
    @Value("ValueFromProperties")
    private String valueFromProperties;
    @Autowired
    private ReminderService reminderService;


    public static void main(String[] args) {
        SpringApp.start(Main.class);
    }

    @Override
    public void run() {
        Course java = new Course(11, "Java", "Java is awesome");
        courseService.createCourse(java);
        courseService.enrollStudentInCourse(java, new Student(99, "Marwan", "marwan@gmail.com"));
        System.out.println(courseService.getAllCourses());
        System.out.println(valueFromProperties);
    }
}