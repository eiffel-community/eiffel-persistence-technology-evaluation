package com.graphmodel.neo4j;

import java.util.ArrayList;

public class NodeV2 {
	private ArrayList<NodePropertyV2> listOfProperties  = new ArrayList<NodePropertyV2>();

	public ArrayList<NodePropertyV2> getListOfProperties() {
		return listOfProperties;
	}
	
	public NodePropertyV2 getPropertyFromList(int index) {
		return listOfProperties.get(index);
	}
	
	public int getSizeOfPropertiesList(){
		return listOfProperties.size();
	}

	public void setListOfProperties(ArrayList<NodePropertyV2> listOfNodes) {
		this.listOfProperties = listOfNodes;
	}
	
	public void addToListOfProperties(NodePropertyV2 node){
		listOfProperties.add(node);
	}
	
	public void addListToListOfProperties(ArrayList<NodePropertyV2> tempListOfNodes){
		for(int i=0; i<tempListOfNodes.size();i++){
			listOfProperties.add(tempListOfNodes.get(i));
		}
	}
	
	public void printPropertiesList(){
		for(int i=0; i<listOfProperties.size(); i++){
			listOfProperties.get(i).printPropertyInfo();
		}
	}
}
