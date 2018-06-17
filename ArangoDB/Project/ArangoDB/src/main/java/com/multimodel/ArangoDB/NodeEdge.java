package com.multimodel.ArangoDB;

import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;

public class NodeEdge {
	@DocumentField(Type.ID)
	private String id;

	@DocumentField(Type.KEY)
	private String key;

	@DocumentField(Type.REV)
	private String revision;

	@DocumentField(Type.FROM)
	private String from;

	@DocumentField(Type.TO)
	private String to;

	private Boolean theFalse;
	private Boolean theTruth;
	private String type;

	public NodeEdge(final String from, final String to, final Boolean theFalse, final Boolean theTruth,
		final String type) {
		this.from = from;
		this.to = to;
		this.theFalse = theFalse;
		this.theTruth = theTruth;
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

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Boolean getTheFalse() {
		return theFalse;
	}

	public void setTheFalse(Boolean theFalse) {
		this.theFalse = theFalse;
	}

	public Boolean getTheTruth() {
		return theTruth;
	}

	public void setTheTruth(Boolean theTruth) {
		this.theTruth = theTruth;
	}

	public String getEdgeType() {
		return type;
	}

	public void setEdgeType(String type) {
		this.type = type;
	}
}
