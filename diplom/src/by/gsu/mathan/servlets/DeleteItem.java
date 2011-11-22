package by.gsu.mathan.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.gsu.mathan.beans.Course;
import by.gsu.mathan.data.CoursesManager;

import static by.gsu.mathan.utils.StringHandler.isEmpty;

/**
 * Servlet implementation class DeleteItem
 */
@WebServlet("/delete-item")
public class DeleteItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteItem() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String itemId = request.getParameter("itemId");
		String courseId = request.getParameter("courseId");
		
		if (!isEmpty(itemId) && !isEmpty(courseId)) {
			Course course = CoursesManager.getCourseById(courseId);
			course.deleteItem(itemId);
		}
	}

}
