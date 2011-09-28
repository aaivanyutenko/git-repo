package by.gsu.mathan.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.gsu.mathan.beans.User;
import by.gsu.mathan.data.CoursesManager;

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
		if (username != null && password != null && username.length() > 0 && password.length() > 0)
			request.getSession().setAttribute("user", new User(username, password));
	}

	private static final long serialVersionUID = 1L;
}
