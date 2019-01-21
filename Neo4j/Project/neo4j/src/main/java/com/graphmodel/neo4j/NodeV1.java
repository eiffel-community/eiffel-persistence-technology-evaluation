package com.graphmodel.neo4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONObject;

public class NodeV1 {
	private ArrayList<NodePropertyV1> propertyList = new ArrayList<NodePropertyV1>();

	public ArrayList<NodePropertyV1> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(ArrayList<NodePropertyV1> propertyList) {
		this.propertyList = propertyList;
	}
	
	public void addToPropertyList(NodePropertyV1 property) {
		this.propertyList.add(checkIfPropertyExist(property));
	}
	
	public int getSizeOfPropertyList(){
		return propertyList.size();
	}
	
	public NodePropertyV1 checkIfPropertyExist(NodePropertyV1 property){
		NodePropertyV1 prop = new NodePropertyV1();
		
		prop = property;
		
		String oldKey1 = property.getPartOfpropertyOldKey();
		List<Integer> c = null;
		for(int i=0; i<propertyList.size(); i++){
			String oldKey2 = propertyList.get(i).getPartOfpropertyOldKey();
			if(oldKey2.equals(oldKey1)){
				NodePropertyV1 propNew = new NodePropertyV1();
				JSONObject json = propertyList.get(i).getJsonProperty();
				json.putAll(property.getJsonProperty());
				
	    		propNew.setJsonProperty(json);
	    		propNew.setPropertyPos2(property.getPropertyPos());
	    		propNew.setPartOfpropertyOldKey(property.getPartOfpropertyOldKey());
	    		
	    		propertyList.remove(i);
	    		prop = propNew;

	    		break;
			}
		}
		keysToType(prop);
		return prop;
	}
	
	public void keysToType(NodePropertyV1 prop){
		List<String> type = new ArrayList<String>();
		 
		for(int i=0; i< prop.getPropertyPos().size();i++){
			if(isInteger(prop.getPropertyPos().get(i))){
				type.remove(i-1);
				type.add("jsonArray");
			}else{
				type.add("jsonObject");
			}
		}
		
		for (Iterator<String> iterator = prop.getPropertyPos().iterator(); iterator.hasNext();) {
		    String string = iterator.next();
		    if (isInteger(string)) {
		        iterator.remove();
		    }
		}

		prop.setPosType(type);

	}
	
	public static boolean isInteger(String s) {
	    try
	    {
	        Integer.parseInt(s);
	    }
	    catch(NumberFormatException ex)
	    {
	        return false;
	    }
	    return true;
	}
	
	public void printList(){
		for(int i = 0; i< propertyList.size(); i++){
			System.out.println("--------");
			System.out.println(propertyList.get(i).getJsonProperty());
			System.out.println(propertyList.get(i).getPropertyPos());
			System.out.println(propertyList.get(i).getPosType());
			System.out.println("--------");
		}
	}
}
