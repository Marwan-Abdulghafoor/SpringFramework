package abdelgafour.murwan;

import abdelgafour.murwan.domain.Course;
import abdelgafour.murwan.domain.Student;
import abdelgafour.murwan.framework.annotation.Autowired;
import abdelgafour.murwan.framework.annotation.Value;
import abdelgafour.murwan.framework.context.SpringApp;
import abdelgafour.murwan.services.CourseService;
import abdelgafour.murwan.services.ReminderService;


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