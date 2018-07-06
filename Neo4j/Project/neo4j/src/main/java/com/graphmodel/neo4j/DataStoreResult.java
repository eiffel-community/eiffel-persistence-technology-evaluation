package com.graphmodel.neo4j;

import java.io.PrintStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataStoreResult {
	private JSONArray eventsArray = new JSONArray(); 
	private long count = 0;	
	
	/*public DataStoreResult(JSONArray jsonArray, long number){
		this.eventsArray = jsonArray;
		this.count = number;
	}*/
	
	public void setEventsArray(JSONArray jsonArray) {
		this.eventsArray = jsonArray;
	}
	
	public void addToEventsArray(JSONObject json){
		this.eventsArray.add(json);
	}
	
	public JSONObject getEventFromEventsArray(int i){
		return (JSONObject) this.eventsArray.get(i);
	}
	
	public void setCount(long number) {
		this.count = number;
	}
	
   public JSONArray getEventsArray() {
		return eventsArray;
	}
   
   public long getCount() {
		return count;
	}
   
   public void printResult(PrintStream p){
	   if(!(eventsArray == null)){
		   int i = 0;
		   System.out.println("Events: ");
		   //p.append("Events: \r\n");
		   for(Object e: eventsArray){
			   i++;
			   System.out.println(e);
			  // p.append(i + ": " + e + "\r\n");
			 //  p.append("\r\n");
		   }
		   System.out.println("Count: " + count);
		  // p.append("Count: " + count + "\r\n");
		  // p.append("\r\n");
	   }
   }
}
