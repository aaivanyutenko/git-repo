package by.gsu.mathan.beans;

import java.io.Serializable;
import java.util.List;

import by.gsu.mathan.data.OwnConstants;

public class Theorems implements Serializable {
	private static final long serialVersionUID = 1L;
	private String text = "Теоремы";
	private boolean leaf = true;
	private String cls = null;
	private List<Theorem> theorems = null;
	private String id = OwnConstants.THEOREMS_ROOT_ID;
	
	public Theorems() {
		
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

	public List<Theorem> getTheorems() {
		return theorems;
	}

	public void setTheorems(List<Theorem> theorems) {
		this.theorems = theorems;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Theorems [theorems=" + theorems + "]";
	}
}
