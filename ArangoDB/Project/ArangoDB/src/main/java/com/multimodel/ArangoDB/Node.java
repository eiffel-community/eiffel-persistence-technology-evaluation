package com.multimodel.ArangoDB;

import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;

public class Node {
	@DocumentField(Type.ID)
	private String id;

	@DocumentField(Type.KEY)
	private String key;

	@DocumentField(Type.REV)
	private String revision;

	private String type;

	public Node(String key, String type) {
		this.key = key;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
