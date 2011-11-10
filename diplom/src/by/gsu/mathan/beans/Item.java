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
	@Expose private String name;
	private String xml;
	@Expose private String ownId;
	@Expose private List<Item> children;

	public Item() {
		children = new ArrayList<Item>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getId() {
		return ownId;
	}

	public void setId(String id) {
		this.ownId = id;
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
		return name;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}
}
