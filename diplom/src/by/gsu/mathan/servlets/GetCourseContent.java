package by.gsu.mathan.servlets;

import by.gsu.mathan.beans.Course;
import by.gsu.mathan.data.CoursesManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/get-course-content")
public class GetCourseContent extends HttpServlet {

	public GetCourseContent() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String courseId = request.getParameter("course_id");
		Course course = CoursesManager.getCourseById(courseId);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(course.getJsonTree());
	}

	private static final long serialVersionUID = 1L;
}
