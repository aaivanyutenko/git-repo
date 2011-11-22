package by.gsu.mathan.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.annotations.Expose;

import by.gsu.mathan.data.OwnConstants;

public class Theorems implements Serializable {
	private static final long serialVersionUID = 1L;
	@Expose private final String text = "Теоремы";
	@Expose private final String cls = "folder";
	@Expose private final boolean expanded = true;
	@Expose private final String id = OwnConstants.THEOREMS_ROOT_ID;
	@Expose private List<Item> children = new ArrayList<>();
	
	public Theorems() {
		children = new ArrayList<>();
	}

	public String getText() {
		return text;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public String getCls() {
		return cls;
	}
	
	public String getId() {
		return id;
	}

	public List<Item> getChildren() {
		return children;
	}
	
	public void setChildren(List<Item> children) {
		this.children = children;
	}
	
	public String toString() {
		return "Children = " + children;
	}
	
	public void add(Item item) {
		children.add(item);
	}
	
	public void delete(String itemId) {
		deleteItem(itemId, children);
	}
	
	private void deleteItem(String itemId, List<Item> items) {
		Iterator<Item> iterator = items.iterator();
		
		while (iterator.hasNext()) {
			Item item = (Item) iterator.next();
			
			if (itemId.equals(item.getId())) {
				items.remove(item);
				return;
			} else {
				if (item.hasChildren()) {
					deleteItem(itemId, item.getChildren());
				}
			}
		}
	}
}
