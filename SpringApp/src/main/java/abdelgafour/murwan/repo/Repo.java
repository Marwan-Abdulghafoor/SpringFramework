package abdelgafour.murwan.repo;

import abdelgafour.murwan.domain.Course;
import abdelgafour.murwan.domain.Student;

import java.util.List;

public interface Repo {
    List<Course> getAllCourses();
    List<Student> getStudentsOfCourse(Course course);
    void enrollStudentInCourse(Course course, Student student);
    void createCourse(Course course);
}
