package by.gsu.mathan.beans;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.gson.Gson;
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
		Gson gson = new Gson();
		JsonElement jsonDefinitions = gson.toJsonTree(definitions);
		array.add(jsonDefinitions);
		JsonElement jsonTheorems = gson.toJsonTree(theorems);
		array.add(jsonTheorems);
		/*StringBuffer jsonBuffer = new StringBuffer("[{\"text\":\"Определения\",\"cls\":\"folder\",\"expanded\":true,\"children\":[{\"text\":\"Thhhh\",\"leaf\":true}]");
		createJsonTree(definitions, jsonBuffer);
		jsonBuffer.append("}, {\"text\":\"Теоремы\",\"cls\":\"folder\"");
		createJsonTree(theorems, jsonBuffer);
		jsonBuffer.append("}]");*/
		String stringArray = array.toString();
		return stringArray;
	}

	/*private void createJsonTree(List<Item> items, StringBuffer json) {
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
	}*/

	public void addItem(Item item, String parentItemId) {
		/*if ("definition".equals(parentItemId))
			definitions.add(item);
		else if ("theorem".equals(parentItemId)) {
			theorems.add(item);
		} else {
			Item parentItem = getItem(parentItemId);
			if (parentItem != null)
				parentItem.getChildren().add(item);
		}*/
	}

	public Item getItem(String id) {
		/*Item item = null;
		item = tryToFind(definitions.getDefinitions(), id);
		item = tryToFind(theorems, id);*/
		return null;
	}

	/*private Item tryToFind(List<Item> list, String id) {
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
	}*/

	public String toString() {
		return name;
	}
}
