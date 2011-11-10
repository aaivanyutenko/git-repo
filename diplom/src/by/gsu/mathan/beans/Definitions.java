package by.gsu.mathan.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import by.gsu.mathan.data.OwnConstants;

public class Definitions implements Serializable {
	private static final long serialVersionUID = 1L;
	@Expose private final String text = "Определения";
	@Expose private final String cls = "folder";
	@Expose private final boolean expanded = true;
	@Expose private final String id = OwnConstants.DEFINITIONS_ROOT_ID;
	@Expose private List<Item> children = new ArrayList<>();

	public String getText() {
		return text;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public String getCls() {
		return cls;
	}

	public List<Item> getChildren() {
		return children;
	}
	
	public String getId() {
		return id;
	}

	public void setChildren(List<Item> children) {
		this.children = children;
	}
	
	public String toString() {
		return "Definitions = " + children;
	}
	
	public void add(Item item) {
		children.add(item);
	}
}
