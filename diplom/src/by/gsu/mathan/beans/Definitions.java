package by.gsu.mathan.beans;

import java.io.Serializable;
import java.util.List;

import by.gsu.mathan.data.OwnConstants;

public class Definitions implements Serializable {
	private static final long serialVersionUID = 1L;
	private String text = "Определения";
	private boolean leaf = true;
	private String cls = null;
	private List<Item> definitions = null;
	private String id = OwnConstants.DEFINITIONS_ROOT_ID;

	public Definitions() {

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public List<Item> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<Item> definitions) {
		this.definitions = definitions;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return "Definitions [definitions=" + definitions + "]";
	}
}
