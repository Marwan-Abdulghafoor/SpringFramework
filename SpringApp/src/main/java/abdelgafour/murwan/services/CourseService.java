package abdelgafour.murwan.services;

import abdelgafour.murwan.domain.Course;
import abdelgafour.murwan.domain.Student;
import abdelgafour.murwan.framework.annotation.Autowired;
import abdelgafour.murwan.framework.annotation.Service;
import abdelgafour.murwan.framework.context.ApplicationEventPublisher;
import abdelgafour.murwan.repo.CourseRepository;
import abdelgafour.murwan.repo.MockCourseRepository;
import abdelgafour.murwan.repo.Repo;


import java.util.List;

@Service
public class CourseService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private Repo courseRepository;


    public void createCourse(Course course) {
        courseRepository.createCourse(course);
        applicationEventPublisher.publishEvent(course);
    }

    public void enrollStudentInCourse(Course course, Student student) {
        courseRepository.enrollStudentInCourse(course, student);
    }
    public List<Course> getAllCourses(){
        return courseRepository.getAllCourses();
    }
}
