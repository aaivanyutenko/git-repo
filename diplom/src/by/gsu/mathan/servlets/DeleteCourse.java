package by.gsu.mathan.servlets;

import by.gsu.mathan.data.CoursesManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/delete-course")
public class DeleteCourse extends HttpServlet {

	public DeleteCourse() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String courseId = request.getParameter("courseId");
		if (courseId != null && courseId.length() > 0)
			CoursesManager.deleteCourse(courseId);
	}

	private static final long serialVersionUID = 1L;
}
