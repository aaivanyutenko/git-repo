package by.gsu.mathan.servlets;

import by.gsu.mathan.beans.Item;
import by.gsu.mathan.data.CoursesManager;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/get-item")
public class GetItem extends HttpServlet {

	public GetItem() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String courseId = request.getParameter("course_id");
		String itemId = request.getParameter("item_id");
		Item item = CoursesManager.getItem(courseId, itemId);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(item.getXml());
	}

	private static final long serialVersionUID = 1L;
}
