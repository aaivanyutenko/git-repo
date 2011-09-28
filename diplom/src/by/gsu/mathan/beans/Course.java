package by.gsu.mathan.beans;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Course implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String id;
	private File coursePath;
	private List<Item> theorems;
	private List<Item> definitions;

	public Course() {
		definitions = new ArrayList<Item>();
		theorems = new ArrayList<Item>();
	}

	public Course(String name, String id, File coursePath) {
		definitions = new ArrayList<Item>();
		theorems = new ArrayList<Item>();
		this.name = name;
		this.id = id;
		this.coursePath = coursePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public File getCoursePath() {
		return coursePath;
	}

	public void setCoursePath(File coursePath) {
		this.coursePath = coursePath;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	public String getJsonTree() {
		StringBuffer jsonBuffer = new StringBuffer("[{'text':'Определения','fuck':'definition','expanded':true,'cls':'folder'");
		createJsonTree(definitions, jsonBuffer);
		jsonBuffer.append("}, {'text':'\u0422\u0435\u043E\u0440\u0435\u043C\u044B','fuck':'theorem','expanded':true,'cls':'folder'");
		createJsonTree(theorems, jsonBuffer);
		jsonBuffer.append("}]");
		return jsonBuffer.toString();
	}

	private void createJsonTree(List<Item> items, StringBuffer json) {
		if (items.size() > 0) {
			json.append(",'children':[");
			Iterator<Item> iterator = items.iterator();
			for (int i = 1; iterator.hasNext(); i++) {
				Item item = (Item) iterator.next();
				String itemName = item.getName();
				json.append((new StringBuilder(String.valueOf(i != 1 ? ",{'text':'" : "{'text':'"))).append(itemName).append("','fuck':'").append(item.getId()).append("'").toString());
				if (item.getChildren().size() > 0) {
					json.append(",'cls':'folder','expanded':true");
					createJsonTree(item.getChildren(), json);
					json.append("}");
				} else {
					json.append(",'cls':'file','leaf':true}");
				}
			}

			json.append("]");
		}
	}

	public void addItem(Item item, String parentItemId) {
		if ("definition".equals(parentItemId))
			definitions.add(item);
		else if ("theorem".equals(parentItemId)) {
			theorems.add(item);
		} else {
			Item parentItem = getItem(parentItemId);
			if (parentItem != null)
				parentItem.getChildren().add(item);
		}
	}

	public Item getItem(String id) {
		Item item = null;
		item = tryToFind(definitions, id);
		item = tryToFind(theorems, id);
		return item;
	}

	private Item tryToFind(List<Item> list, String id) {
		Item item = null;
		Iterator<Item> iterator = list.iterator();
		while (iterator.hasNext()) {
			Item iterItem = (Item) iterator.next();
			if (iterItem.getId().equals(id)) {
				item = iterItem;
				break;
			}
			if (!iterItem.hasChildren())
				continue;
			item = tryToFind(iterItem.getChildren(), id);
			if (item != null)
				break;
		}
		return item;
	}

	public String toString() {
		return name;
	}
}
