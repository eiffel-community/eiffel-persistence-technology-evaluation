package com.multimodel.ArangoDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;










//import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.DocumentCreateEntity;
import com.arangodb.model.DocumentCreateOptions;

public class ArangoDBDatabaseHelperV1 implements DatabaseHelper{

	private ArangoDB arangoDB = new ArangoDB.Builder().build(); 
	private ArangoDatabase myDatabase;
	private CollectionEntity myCollectionEntity;
	private ArangoCollection myCollection;
	
	private String dbName = "Eiffel";
	private String collectionName = "Events";
	
	private long startTime = 0;
	private long endTime = 0;
	private long elapsedTime = 0;
	private ArrayList<Long> timeRes = new ArrayList<Long>();
	
	public void arangoDBSetUp(){
		if (arangoDB == null) {
			arangoDB = new ArangoDB.Builder().build();
		}
		try {
			arangoDB.db(dbName).drop();
		} catch (final ArangoDBException e) {
		}
		
		try {
			arangoDB.createDatabase(dbName);
		} catch (ArangoDBException e) {
			System.err.println("Failed to create database: " + dbName + "; " + e.getMessage());
		}
		
		myDatabase = arangoDB.db(dbName);

		try {
			myCollectionEntity = myDatabase.createCollection(collectionName);
		} catch (ArangoDBException e) {
			System.err.println("Failed to create collection: " + collectionName + "; " + e.getMessage());
		}
		
		myCollection = myDatabase.collection(collectionName);
	}
	
	public void removeEverything(){
		try {
			myDatabase.collection(collectionName).drop();
		} catch (ArangoDBException e) {
			System.err.println("Failed to delete collection. " + e.getMessage());
		}
		
		try {
			myDatabase.drop();
		} catch (ArangoDBException e) {
			System.err.println("Failed to delete database. " + e.getMessage());
		}
		
	}
	
	public void removeCollection(){
		try {
			ArangoCollection tempCollection = arangoDB.db(dbName).collection(collectionName);
			tempCollection.drop();
		} catch (ArangoDBException e) {
			System.err.println("Failed to delete collection. " + e.getMessage());
		}
	}
	
	public void removeDatabase(){
		try {
			arangoDB.db(dbName).drop();
		} catch (ArangoDBException e) {
			System.err.println("Failed to delete database. " + e.getMessage());
		}
	}
	
	public boolean storeManyEvents(List<JSONObject> jsonArr, int amount) {
    	int i = 0;
    	boolean work = false;
    	if(amount <= jsonArr.size()){
	    	for(; i < amount; i++){
	    		try {
					JSONObject json = (JSONObject) new JSONParser().parse(jsonArr.get(i).toString());
					work = store(json);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		//work = store((JSONObject)jsonArr.get(i));	
	    	}
    	}
    	//System.out.println(i);
    	return work;
	}
	
	@Override
	public boolean store(JSONObject json) {
		// TODO Auto-generated method stub
		String key = "";
		boolean added = false;
		try {
			String metaId = ((JSONObject) json.get("meta")).get("id").toString();
			json.put("_key", metaId);
			//DocumentCreateEntity<String> entity = myCollection.insertDocument(json.toString());
			
			startTime = System.nanoTime();
			
			DocumentCreateEntity<String> entity = arangoDB.db(dbName).collection(collectionName).insertDocument(json.toString());
			
			endTime = System.nanoTime();
			long time = (endTime - startTime);
			timeRes.add(time);
			
			added = true;
		} catch (ArangoDBException e) {
			System.err.println("Failed to add JSONObject. " + e.getMessage());
		}
		return added;
	}

	@Override
	public JSONObject getEvent(String metaId) {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
		JSONObject json = new JSONObject();
		JSONObject tempJson = new JSONObject();
		String rawJsonString = "";
		
		/*try {
			if(arangoDB.db(dbName).collection(collectionName).documentExists(metaId)){
				rawJsonString = arangoDB.db(dbName).collection(collectionName).getDocument(metaId, String.class);
			}else{
				throw new ArangoDBException("Document is not in Database");
			}
		} catch (ArangoDBException e){
			System.err.println("Failed to find document with metaId: " + metaId + " . " + e.getMessage());
		}
		
		try {
			tempJson = (JSONObject) parser.parse(rawJsonString);
		} catch (ParseException e) {
			System.err.println("Failed to parse string (" + rawJsonString + ") to json. " + e.getMessage());
		}
		json.put("meta", tempJson.get("meta"));
		json.put("data", tempJson.get("data"));
		json.put("links", tempJson.get("links"));*/
		
		String query = "FOR document IN " + collectionName + " FILTER document.meta.id == '" + metaId + "' RETURN document";
		try {	 
			
			long startTime1 = System.nanoTime();
			
			if(arangoDB.db(dbName).collection(collectionName).documentExists(metaId)){
				  ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, null, null,
				      BaseDocument.class);
				  cursor.forEachRemaining(aDocument -> {
				    json.putAll(new JSONObject(aDocument.getProperties()));
				  });
			}else{
				throw new ArangoDBException("Document is not in Database");
			}
			
			long endTime1 = System.nanoTime();
			elapsedTime = (endTime1 - startTime1);
			
		} catch (ArangoDBException e) {
			System.err.println("Failed to execute query. " + e.getMessage());
		}
		return json;
	}
	
	public String createQueryParameters(FilterParameterList filterList){
		String params = "";
		if(filterList.getFilterList().size() > 0){
			ArrayList<FilterParameter> tempList = filterList.getFilterList();
			
			for(int i = 0; i < tempList.size(); i++){
				boolean isTime = false;
				FilterParameter p = tempList.get(i);
				String[] keys = p.getKey().split("_");
				params += "document.";
				for(String s: keys){
					params += s + ".";
					if(s.equals("time")){
						isTime = true;
					}
				}
				params = params.substring(0, params.length() - 1);
				if(isTime){
					params += " " + p.getComparator() + " " + p.getValue() + " AND ";
				}else{
					params += " " + p.getComparator() + " '" + p.getValue() + "' AND ";
				}
			}
			params = params.substring(0, params.length() - 4);
		}
		return params;
	}

	@Override
	public DataStoreResult getEvents(FilterParameterList filterList,
			String comparator, int skip, int limit, boolean lazy) {
		// TODO Auto-generated method stub
		DataStoreResult result = new DataStoreResult();
		JSONArray jArr = new JSONArray();
		String sort = (comparator.equals("<"))? "ASC"  : "DESC";
		String query = "FOR document IN " + collectionName + " FILTER " 
						+ createQueryParameters(filterList) 
						+ " SORT document.meta.time " + sort 
						+ " LIMIT " + skip + ", " + limit 
						+ " RETURN document";
		//System.out.println(query);
		try {	
			
			long startTime1 = System.nanoTime();
			
			  ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, null, null,
			      BaseDocument.class);
			  cursor.forEachRemaining(aDocument -> {
			    jArr.add(new JSONObject(aDocument.getProperties()));
			  });
			  
			  long endTime1 = System.nanoTime();
			  elapsedTime = (endTime1 - startTime1);
				
		} catch (ArangoDBException e) {
			System.err.println("Failed to execute query. " + e.getMessage());
		}
		long number = (lazy)? -1:jArr.size();
		result.setEventsArray(jArr);
		if(number == 0){
			System.out.println(number);
			System.out.println(query);
		}
		
		result.setCount(number);
		return result;
	}

	@Override
	public void removeAllEvents() {
		// TODO Auto-generated method stub
		String query = "FOR document IN " + collectionName + " REMOVE document IN " + collectionName;
		try {	  
			  ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, null, null,
			      BaseDocument.class);
		} catch (ArangoDBException e) {
			System.err.println("Failed to execute query. " + e.getMessage());
		}
	}

	
	 public void sort(List<Object> inputArr) {
         
	        if (inputArr == null || inputArr.size() == 0) {
	            return;
	        }
	        int length = inputArr.size();
	        quickSort(inputArr, 0, length - 1);
	    }
	 
	    private void quickSort(List<Object> list, int lowerIndex, int higherIndex) {
	         
	        int i = lowerIndex;
	        int j = higherIndex;
	        
	        long pivot = (long)(double) ((Map) ((Map) list.get(lowerIndex+(higherIndex-lowerIndex)/2)).get("meta")).get("time");
	        
	        while (i <= j) {

	        	long tempI = (long)(double) ((Map) ((Map) list.get(i)).get("meta")).get("time");
	            while (tempI < pivot) {
	                i++;
	                tempI = (long)(double) ((Map) ((Map) list.get(i)).get("meta")).get("time");
	            }
	            long tempJ = (long)(double) ((Map) ((Map) list.get(j)).get("meta")).get("time");
	            while (tempJ > pivot) {
	                j--;
	                tempJ = (long)(double) ((Map) ((Map) list.get(j)).get("meta")).get("time");
	            }
	            if (i <= j) {
	                exchangeNumbers(list, i, j);
	                
	                i++;
	                j--;
	            }
	        }
	        if (lowerIndex < j)
	            quickSort(list, lowerIndex, j);
	        if (i < higherIndex)
	            quickSort(list, i, higherIndex);
	    }
	 
	    private void exchangeNumbers(List<Object> list, int i, int j) {
	        Object temp = list.get(i);
	        list.set(i, list.get(j));
	        list.set(j, temp);
	    }
	
	
	@Override
	public List<Object> getUpstreamEvents(String eventId,
			List<String> linkTypes, ConcurrentMap<String, String> visitedMap,
			int limit, int levels) {
		// TODO Auto-generated method stub
		final List<Object> upstreamEvents = new ArrayList<>();
		List<Object> tempUpstreamEvents = new ArrayList<>();
		JSONObject startEvent = new JSONObject();
		long startTime1 = System.nanoTime();
		
		startEvent = getEvent(eventId);
		
		
		if(!(startEvent == null)){
			upstreamEvents.add(startEvent);
			visitedMap.put(eventId, "true");
			System.out.println(levels);
			performUpstreamSearch(startEvent, linkTypes, visitedMap, limit-1, levels, tempUpstreamEvents);
			System.out.println(tempUpstreamEvents.size());
			//sort(tempUpstreamEvents);
			limit = (limit < tempUpstreamEvents.size()) ? limit-1 : tempUpstreamEvents.size();
			for(int i = tempUpstreamEvents.size()-1; i >= (tempUpstreamEvents.size() - limit); i--){
				upstreamEvents.add(tempUpstreamEvents.get(i));
			}
			
			long endTime1 = System.nanoTime();
			elapsedTime = (endTime1 - startTime1);
			
			return upstreamEvents;
		}else{
			return null;
		}
	}

	@Override
	public boolean performUpstreamSearch(JSONObject event,
			List<String> linkTypes, ConcurrentMap<String, String> visitedMap,
			int limit, int levels, List<Object> events) {
		// TODO Auto-generated method stub
		//System.out.println("Link size: " + linkTypes);
        String query = "";
        ArrayList links =  (ArrayList) event.get("links");
        if(levels == 0){
        	//System.out.println("levels");
        	return true;
        }
        if (limit == events.size()) {
        	//System.out.println("limit");
            return true;
        }
        for(Object o: links){
        	for(int i= 0; i< linkTypes.size(); i++){
        		
        		if(((Map) o).get("type").equals(linkTypes.get(i))){
        			JSONObject tempEvent = getEvent(((Map)o).get("target").toString());
        			if(!visitedMap.containsKey(((Map) o).get("target"))){
	        			visitedMap.put(((Map) o).get("target").toString(), "true");	
	        			events.add(tempEvent);
	        			
        			}
        			performUpstreamSearch(tempEvent, linkTypes, visitedMap, limit, levels - 1, events);
        		}
        	}
        }

        return false;
	}

	@Override
	public List<Object> getDownstreamEvents(String eventId,
			List<String> linkTypes, ConcurrentMap<String, String> visitedMap,
			int limit, int levels) {
		// TODO Auto-generated method stub
		final List<Object> downstreamEvents = new ArrayList<>();
		List<Object> tempDownstreamEvents = new ArrayList<>();
		JSONObject startEvent = new JSONObject();
		
		long startTime1 = System.nanoTime();
		
		startEvent = getEvent(eventId);
		if(!(startEvent == null)){
			visitedMap.putIfAbsent(eventId, "true");
			downstreamEvents.add(startEvent);
			performDownstreamSearch(eventId, linkTypes, visitedMap, limit, levels, tempDownstreamEvents);
			limit = (limit < tempDownstreamEvents.size()) ? limit-1 : tempDownstreamEvents.size();
			sort(tempDownstreamEvents);
			for(int i = 0; i < limit; i++){
				downstreamEvents.add(tempDownstreamEvents.get(i));
			}
			
			long endTime1 = System.nanoTime();
			elapsedTime = (endTime1 - startTime1);
			
			return downstreamEvents;
		}else{
			return null;
		}
	}
	
	public String createDownstreamQuery(List<String> linkTypes){
		String filter = "";
		for(int i = 0; i < linkTypes.size(); i++){
			filter += " l.type == '" + linkTypes.get(i) + "' OR";
		}
		filter = filter.substring(0, filter.length()-2);
		return filter;	
	}

	@Override
	public boolean performDownstreamSearch(String eventId,
			List<String> linkTypes, ConcurrentMap<String, String> visitedMap,
			int limit, int levels, List<Object> events) {
		// TODO Auto-generated method stub
		if(levels == 0){
        	return true;
        }
        if (limit == events.size()) {
            return true;
        }
        
		JSONArray jArr = new JSONArray();
		String query = "FOR r IN " + collectionName 
					+ " FOR l IN r.links FILTER l.target == '" 
					+ eventId + "' AND (" + createDownstreamQuery(linkTypes) + ") RETURN r";
		try {	  
			  ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, null, null,
			      BaseDocument.class);
			  cursor.forEachRemaining(aDocument -> {
				  jArr.add(new JSONObject(aDocument.getProperties()));
			  });
		} catch (ArangoDBException e) {
			System.err.println("Failed to execute query. " + e.getMessage());
		}
		for(Object o: jArr){
			String tempId = ((Map) ((Map) o).get("meta")).get("id").toString();
			if(!visitedMap.containsKey(tempId)){
				visitedMap.putIfAbsent(tempId, "true");
				events.add(o);
			}
			performDownstreamSearch(tempId, linkTypes, visitedMap, limit, levels - 1, events);
		}
		
		
		return false;
	}

	@Override
	public DataStoreResult getArtifactsByGroup(String groupId,
			FilterParameterList filterList, String comparator, int skip,
			int limit) {
		// TODO Auto-generated method stub
		DataStoreResult result = new DataStoreResult();
		JSONArray jArr = new JSONArray();
		String gId = "document.data.gav.groupId == '" + groupId + "' ";
		gId += (filterList.getFilterList().isEmpty())? "" : "AND ";
		String sort = (comparator.equals("<"))? "ASC"  : "DESC";
		String query = "FOR document IN " + collectionName + " FILTER " 
						+ gId
						+ createQueryParameters(filterList) 
						+ " SORT document.meta.time " + sort 
						+ " LIMIT " + skip + ", " + limit 
						+ " RETURN document";
		//System.out.println(query);

		try {	  
			
			long startTime1 = System.nanoTime();
			
			ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, null, null,
			      BaseDocument.class);
			
			cursor.forEachRemaining(aDocument -> {
			    jArr.add(new JSONObject(aDocument.getProperties()));
			   	});
			  
			long endTime1 = System.nanoTime();
			elapsedTime = (endTime1 - startTime1);
				
		} catch (ArangoDBException e) {
			System.err.println("Failed to execute query. " + e.getMessage());
		}
		
		result.setEventsArray(jArr);
		result.setCount(jArr.size());
		return result;
	}

	@Override
	public DataStoreResult getArtifactsByGroupAndArtifactId(String groupId,
			String artifactId, FilterParameterList filterList,
			String comparator, int skip, int limit) {
		// TODO Auto-generated method stub
		DataStoreResult result = new DataStoreResult();
		JSONArray jArr = new JSONArray();
		String gIdAId = "document.data.gav.groupId == '" + groupId + "' AND document.data.gav.artifactId == '" + artifactId + "' ";
		gIdAId += (filterList.getFilterList().isEmpty())? "" : "AND ";
		String sort = (comparator.equals("<"))? "ASC"  : "DESC";
		String query = "FOR document IN " + collectionName + " FILTER " 
						+ gIdAId
						+ createQueryParameters(filterList) 
						+ " SORT document.meta.time " + sort 
						+ " LIMIT " + skip + ", " + limit 
						+ " RETURN document";
		//System.out.println(query);
	
		try {
			
			long startTime1 = System.nanoTime();
			
			ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, null, null,
			      BaseDocument.class);
			cursor.forEachRemaining(aDocument -> {
			    jArr.add(new JSONObject(aDocument.getProperties()));
			  });
			
			long endTime1 = System.nanoTime();
			elapsedTime = (endTime1 - startTime1);
			
		} catch (ArangoDBException e) {
			System.err.println("Failed to execute query. " + e.getMessage());
		}
		
		result.setEventsArray(jArr);
		result.setCount(jArr.size());
		return result;
	}

	@Override
	public JSONObject getArtifactByGAV(String groupId, String artifactId,
			String version) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		String query = "FOR document IN " + collectionName + " FILTER document.data.gav.groupId == '" + groupId 
						+ "' AND document.data.gav.artifactId == '" + artifactId 
						+ "' AND document.data.gav.version == '" + version
						+ "'  RETURN document";

		try {	 
			
			long startTime1 = System.nanoTime();
			
			ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, null, null,
			      BaseDocument.class);
			cursor.forEachRemaining(aDocument -> {
			    json.putAll(new JSONObject(aDocument.getProperties()));
			  });
			
			long endTime1 = System.nanoTime();
			elapsedTime = (endTime1 - startTime1);
			
		} catch (ArangoDBException e) {
			System.err.println("Failed to execute query. " + e.getMessage());
		}
		
		return json;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public ArrayList<Long> getTimeRes() {
		return timeRes;
	}

	public void setTimeRes(ArrayList<Long> timeRes) {
		this.timeRes = timeRes;
	}
	
	public void timeResClear() {
		timeRes.clear();
	}

}
