package abdelgafour.murwan.repo;

import abdelgafour.murwan.domain.Course;
import abdelgafour.murwan.domain.Student;
import abdelgafour.murwan.framework.annotation.Profile;
import abdelgafour.murwan.framework.annotation.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Profile("testing")
public class MockCourseRepository implements Repo {
    private static Map<Course, List<Student>> courseListMap = new HashMap<>();
    public MockCourseRepository() {
        Course math = new Course(1, "Math", "This is math course");
        Student studentMath1 = new Student(1, "Math Student 1", "stu1@math.com");
        Student studentMath2 = new Student(1, "Math Student 2", "stu2@math.com");
        createCourse(math);
        enrollStudentInCourse(math, studentMath1);
        enrollStudentInCourse(math, studentMath2);
        Course science = new  Course(2, "Science", "This is science course");
        Student scienceMath1 = new Student(1, "Science Student 1", "stu1@science.com");
        Student scienceMath2 = new Student(1, "Science Student 2", "stu2@science.com");
        createCourse(science);
        enrollStudentInCourse(science, scienceMath1);
        enrollStudentInCourse(science, scienceMath2);
    }
    @Override
    public List<Course> getAllCourses() {
        return courseListMap.keySet().stream().collect(Collectors.toList());
    }
    @Override
    public List<Student> getStudentsOfCourse(Course course) {
        return courseListMap.get(course);
    }
    @Override
    public void enrollStudentInCourse(Course course, Student student){
        if (courseListMap.containsKey(course.getId())){
            courseListMap.get(course).add(student);
        }
    }
    @Override
    public void createCourse(Course course) {
        courseListMap.put(course, new ArrayList<>());
    }
}
