package by.gsu.mathan.beans;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import by.gsu.mathan.data.OwnConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Course implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String id;
	private File coursePath;
	private Definitions definitions;
	private Theorems theorems;

	public Course() {
		definitions = new Definitions();
		theorems = new Theorems();
	}

	public Course(String name, String id, File coursePath) {
		definitions = new Definitions();
		theorems = new Theorems();
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
		JsonArray array = new JsonArray();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		JsonElement jsonDefinitions = gson.toJsonTree(definitions);
		array.add(jsonDefinitions);
		JsonElement jsonTheorems = gson.toJsonTree(theorems);
		array.add(jsonTheorems);
		String stringArray = array.toString();
		return stringArray;
	}

	public void addItem(Item item, String parentItemId) {
		if (OwnConstants.DEFINITIONS_ROOT_ID.equals(parentItemId)) {
			definitions.add(item);
		} else if (OwnConstants.THEOREMS_ROOT_ID.equals(parentItemId)) {
			theorems.add(item);
		} else {
			Item parentItem = getItem(parentItemId);
			if (parentItem != null) {
				parentItem.add(item);
			}
		}
	}
	
	public void deleteItem(String itemId) {
		definitions.delete(itemId);
		theorems.delete(itemId);
	}

	public Item getItem(String id) {
		Item item = null;
		item = tryToFind(definitions.getChildren(), id);
		item = tryToFind(theorems.getChildren(), id);
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
