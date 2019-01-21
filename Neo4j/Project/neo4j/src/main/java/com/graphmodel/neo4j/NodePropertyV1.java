package com.graphmodel.neo4j;

import java.util.List;

import org.json.simple.JSONObject;

public class NodePropertyV1 {
	private JSONObject jsonProperty = new JSONObject();
	private List<String> propertyPos;
	private List<String> posType;
	private String partOfpropertyOldKey;
	
	public JSONObject getJsonProperty() {
		return jsonProperty;
	}
	public void setJsonProperty(JSONObject jsonProperty) {
		this.jsonProperty = jsonProperty;
	}
	
	public List<String> getPropertyPos() {
		return propertyPos;
	}
	public void setPropertyPos(List<String> tempPropertyPos) {
		if(tempPropertyPos.size() > 1){
			tempPropertyPos.remove(tempPropertyPos.size()-1);
		}
		this.propertyPos = tempPropertyPos;
	}
	public void setPropertyPos2(List<String> tempPropertyPos) {
		this.propertyPos = tempPropertyPos;
	}

	public void addToPropertyPos(String s){
		propertyPos.add(s);	
	}
	public String getPartOfpropertyOldKey() {
		return partOfpropertyOldKey;
	}
	public void setPartOfpropertyOldKey(String partOfpropertyOldKey) {
		this.partOfpropertyOldKey = partOfpropertyOldKey;
	}
	public List<String> getPosType() {
		return posType;
	}
	public void setPosType(List<String> posType) {
		this.posType = posType;
	}
}
