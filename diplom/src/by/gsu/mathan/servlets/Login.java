package by.gsu.mathan.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.gsu.mathan.data.CoursesManager;
import static by.gsu.mathan.utils.StringHandler.isEmpty;

@WebServlet("/login")
public class Login extends HttpServlet {

	public Login() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CoursesManager.setRealPath(getServletContext().getRealPath("courses"));
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (!isEmpty(username) && !isEmpty(password)) {
			request.getSession().setAttribute("user", "'any_user_name'");
		}
	}

	private static final long serialVersionUID = 1L;
}
