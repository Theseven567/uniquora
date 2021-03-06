package kz.codingwolves.uniquora.controllers;

import kz.codingwolves.uniquora.SpringRunner;
import kz.codingwolves.uniquora.dto.CourseDto;
import kz.codingwolves.uniquora.enums.Message;
import kz.codingwolves.uniquora.models.Course;
import kz.codingwolves.uniquora.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Date;
import java.util.List;

/**
 * Created by sagynysh on 12/27/16.
 */
@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<CourseDto> getCourses() {
        return CourseDto.fromList(courseRepository.findAll(), false);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Principal principal, HttpServletResponse response, @RequestBody List<CourseDto> list) {
        if (!SpringRunner.isAdmin(principal.getName())) {
            response.setStatus(403);
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            return Message.forbidden.toString();
        }
        for (CourseDto dto: list) {
            Course course = courseRepository.findByCode(dto.COURSECODE);
            if (course == null) {
                Course newCourse = new Course();
                newCourse.setTitle(dto.COURSETITLE);
                newCourse.setRegistrarId(dto.INSTANCEID);
                newCourse.setCode(dto.COURSECODE);
                newCourse.setCreatedDate(new Date());
                newCourse.setModifiedDate(new Date());
                newCourse.setRemoved(false);
                courseRepository.merge(newCourse);
            } else {
                course.setTitle(dto.COURSETITLE);
                course.setRegistrarId(dto.INSTANCEID);
                course.setRemoved(false);
                course.setModifiedDate(new Date());
                courseRepository.merge(course);
            }
        }
        return Message.success.toString();
    }
}
