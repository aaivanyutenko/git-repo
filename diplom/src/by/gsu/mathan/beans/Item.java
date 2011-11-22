package by.gsu.mathan.beans;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	@Expose private String text;
	private String xml;
	@Expose private String id;
	@Expose private String cls = "file";
	@Expose private boolean leaf = true;
	@Expose private boolean expanded = false;
	@Expose private List<Item> children = new ArrayList<Item>();

	public String getName() {
		return text;
	}

	public void setName(String text) {
		this.text = text;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCls() {
		return cls;
	}

	public void setCls(String cls) {
		this.cls = cls;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	public boolean isExpanded() {
		return expanded;
	}
	
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public List<Item> getChildren() {
		return children;
	}

	public void setChildren(List<Item> children) {
		this.children = children;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	public String toString() {
		return text;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	public void add(Item item) {
		children.add(item);
		
		if (hasChildren()) {
			setCls("folder");
			setLeaf(false);
			setExpanded(true);
		}
	}
}
