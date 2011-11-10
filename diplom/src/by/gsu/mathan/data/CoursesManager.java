package by.gsu.mathan.data;

import by.gsu.mathan.beans.Course;
import by.gsu.mathan.beans.Item;
import by.gsu.mathan.xml.MathDOCXXMLHandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.tomcat.util.http.fileupload.FileItem;

public class CoursesManager {
	private static Map<String, Course> coursesBeansCash = null;
	private static Map<String, Item> itemsCash = null;
	private static File realPath;

	public CoursesManager() {
	}

	public static void setRealPath(String path) {
		realPath = new File(path);
	}

	public static void addCourse(String courseName) {
		try {
			if (isValidCourseName(courseName)) {
				String id = UUID.randomUUID().toString();
				File file = new File((new StringBuilder()).append(realPath).append("\\").append(id).toString());
				Course course = new Course(courseName, id, file);
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(course);
				objectOutputStream.close();
				fileOutputStream.close();
				coursesBeansCash = null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isValidCourseName(String courseName) {
		return true;
	}

	public static void deleteCourse(String courseId) {
		File file = new File((new StringBuilder()).append(realPath).append("\\").append(courseId).toString());
		file.delete();
		coursesBeansCash = null;
	}

	public static void addItem(String courseId, FileItem fileItem, String parentItemId) {
		try {
			String fileName = fileItem.getName();
			fileName = fileName.substring(fileName.lastIndexOf('\\') != -1 ? fileName.lastIndexOf('\\') : 0, fileName.length());
			fileName = fileName.substring(0, fileName.length() - 5);
			Item item = new Item();
			item.setName(fileName);
			item.setId(UUID.randomUUID().toString());
			File file = new File("tmpFile");
			fileItem.write(file);
			ZipFile zipFile = new ZipFile(file);
			for (Enumeration<? extends ZipEntry> enumeration = zipFile.entries(); enumeration.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) enumeration.nextElement();
				if ("word/document.xml".equals(entry.getName())) {
					InputStream inputStream = zipFile.getInputStream(entry);
					byte b[] = new byte[inputStream.available()];
					inputStream.read(b);
					inputStream.close();
					file.delete();
					SAXParserFactory parserFactory = SAXParserFactory.newInstance();
					SAXParser parser = parserFactory.newSAXParser();
					StringWriter stringWriter = new StringWriter();
					MathDOCXXMLHandler handler = new MathDOCXXMLHandler(fileName, stringWriter);
					parser.parse(new ByteArrayInputStream(b), handler);
					String xml = stringWriter.toString();
					xml = xml.replaceAll("\\r\\n", "");
					item.setXml(xml);
					break;
				}
			}

			Course course = getCourseById(courseId);
			course.addItem(item, parentItemId);
			updateCourse(course);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Item getItem(String courseId, String itemId) {
		Item item = (Item) getItemsCash().get(itemId);
		if (item == null) {
			Course course = getCourseById(courseId);
			item = course.getItem(itemId);
			getItemsCash().put(itemId, item);
		}
		return item;
	}

	private static void fillCoursesBeansCash() {
		coursesBeansCash = new HashMap<String, Course>();
		File coursesFilePath[] = realPath.listFiles();
		for (int i = 0; i < coursesFilePath.length; i++) {
			Course course = getCourseByPath(coursesFilePath[i].getAbsolutePath());
			coursesBeansCash.put(course.getId(), course);
		}

	}

	public static String getJsonCourses() {
		StringBuffer json = new StringBuffer("[");
		Course course;
		for (Iterator<Course> iterator = getCoursesCash().values().iterator(); iterator.hasNext(); json.append((new StringBuilder("['")).append(course.getName()).append("', '").append(course.getId()).append("'], ").toString()))
			course = (Course) iterator.next();

		if (json.length() > 1)
			json.setLength(json.length() - 2);
		json.append("]");
		return json.toString();
	}

	private static Course getCourseByPath(String path) {
		Course course = null;
		try {
			File file = new File(path);
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			course = (Course) objectInputStream.readObject();
			objectInputStream.close();
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return course;
	}

	public static Course getCourseById(String id) {
		/*Map<String, Course> courses = getCoursesCash();
		return (Course) courses.get(id);*/
		return new Course("Math", "123", new File("c:\\ff"));
	}

	private static void updateCourse(Course course) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(course.getCoursePath());
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(course);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Map<String, Course> getCoursesCash() {
		if (coursesBeansCash == null)
			fillCoursesBeansCash();
		return coursesBeansCash;
	}

	private static Map<String, Item> getItemsCash() {
		if (itemsCash == null)
			itemsCash = new HashMap<String, Item>();
		return itemsCash;
	}
}
