package by.gsu.mathan.servlets;

import by.gsu.mathan.data.CoursesManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/get-courses")
public class GetCourses extends HttpServlet {

	public GetCourses() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CoursesManager.setRealPath(getServletContext().getRealPath("courses"));
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(CoursesManager.getJsonCourses());
	}

	private static final long serialVersionUID = 1L;
}
