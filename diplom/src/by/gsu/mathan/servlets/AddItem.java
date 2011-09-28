package by.gsu.mathan.servlets;

import by.gsu.mathan.data.CoursesManager;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

@WebServlet("/add-item")
public class AddItem extends HttpServlet {

	public AddItem() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String courseId = null;
		String parentItemId = null;
		FileItem docxFile = null;
		try {
			DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
			List<FileItem> fileItems = fileUpload.parseRequest(request);
			for (Iterator<FileItem> iterator = fileItems.iterator(); iterator.hasNext();) {
				FileItem fileItem = (FileItem) iterator.next();
				if (!fileItem.isFormField()) {
					docxFile = fileItem;
				} else {
					if ("courseId".equals(fileItem.getFieldName()))
						courseId = new String(fileItem.get());
					if ("parentId".equals(fileItem.getFieldName()))
						parentItemId = new String(fileItem.get());
				}
			}

			CoursesManager.addItem(courseId, docxFile, parentItemId);
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().print((new StringBuilder("<html><head></head><body>{success:true, file:'")).append(docxFile.getFieldName()).append("'}</body></html>").toString());
	}

	private static final long serialVersionUID = 1L;
}
