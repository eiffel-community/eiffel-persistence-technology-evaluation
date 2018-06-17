package com.graphmodel.neo4j;

import java.util.ArrayList;

public class NodeListV1 {
	private ArrayList<NodeV1> nodeList = new ArrayList<NodeV1>();

	public ArrayList<NodeV1> getNodeList() {
		return nodeList;
	}

	public void setNodeList(ArrayList<NodeV1> nodeList) {
		this.nodeList = nodeList;
	}
	
	public NodeV1 getNodeFromList(int i){
		return nodeList.get(i);
	}
	
	public void addToNodeList(NodeV1 node){
		nodeList.add(node);
	}
	
	public void removeFromNodeList(int i){
		nodeList.remove(i);
	}
	
	public int getSizeOfNodeList(){
		return nodeList.size();
	}
}
