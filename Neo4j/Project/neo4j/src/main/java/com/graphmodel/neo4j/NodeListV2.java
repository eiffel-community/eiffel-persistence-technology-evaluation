package com.graphmodel.neo4j;

import java.util.ArrayList;

public class NodeListV2 {
	private ArrayList<NodeV2> nodeList = new ArrayList<NodeV2>();

	public ArrayList<NodeV2> getNodeList() {
		return nodeList;
	}

	public void setNodeList(ArrayList<NodeV2> nodeList) {
		this.nodeList = nodeList;
	}
	
	public NodeV2 getNodeFromList(int i){
		return nodeList.get(i);
	}
	
	public void addToNodeList(NodeV2 node){
		nodeList.add(node);
	}
	
	public void removeFromNodeList(int i){
		nodeList.remove(i);
	}
	
	public int getSizeOfNodeList(){
		return nodeList.size();
	}
}
