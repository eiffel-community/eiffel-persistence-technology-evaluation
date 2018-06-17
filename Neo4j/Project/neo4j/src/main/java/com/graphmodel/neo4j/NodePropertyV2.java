package com.graphmodel.neo4j;

import org.json.simple.JSONObject;



public class NodePropertyV2 {
	private JSONObject propertyJson = new JSONObject(); 
	private String specialKey = "";	
	private String linkType = "";
	private String linkValue = "";
	
	/*public DataStoreResult(JSONArray jsonArray, long number){
		this.eventsArray = jsonArray;
		this.count = number;
	}*/
	
	public void setPropertyJson(JSONObject jsonObject) {
		this.propertyJson = jsonObject;
	}
	
	public void setSpecialKey(String key) {
		this.specialKey = key;
	}
	
   public JSONObject getPropertyJson() {
		return propertyJson;
	}
   
   public String getSpecialKey() {
		return specialKey;
	}

	public String getLinkType() {
		return linkType;
	}
	
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	
	public String getLinkValue() {
		return linkValue;
	}
	
	public void setLinkValue(String linkValue) {
		this.linkValue = linkValue;
	}
	
	public void printPropertyInfo(){
		System.out.println("-----------------");
		System.out.println("Json: " + propertyJson);
		System.out.println("SpecialKey: " + specialKey);
		System.out.println("LinkType: " + linkType);
		System.out.println("LinkValue: " + linkValue);
		System.out.println("-----------------");
	}
}
