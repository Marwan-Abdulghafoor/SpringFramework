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
@Profile("production")
public class CourseRepository implements Repo{
    private static Map<Course, List<Student>> courseListMap = new HashMap<>();
    public CourseRepository() {
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
        if (courseListMap.containsKey(course)){
            courseListMap.get(course).add(student);
        }
    }
    @Override
    public void createCourse(Course course) {
        courseListMap.put(course, new ArrayList<>());
    }
}
