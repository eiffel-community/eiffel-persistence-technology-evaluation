package com.graphmodel.neo4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.exceptions.Neo4jException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Neo4jDatabaseHelperV1 implements AutoCloseable, DatabaseHelper  {
	
	private final Driver driver;

    public Neo4jDatabaseHelperV1( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		driver.close();	
	}

	public boolean execQuery(String query){
		try	(Session session = driver.session())
    	{
    		String greeting = session.writeTransaction( new TransactionWork<String>()
    		{
    			 @Override
                 public String execute( Transaction tx )
                 { 
    				 tx.run(query);
    				 return "done";
                 }
    		});
		//System.out.println( greeting );
    	}
		return true;
	}
	
	public NodeListV1 execGetQueryV1(String query){
		NodeListV1 listOfNodes = new NodeListV1();
		
		try	(Session session = driver.session())
    	{
    		String greeting = session.writeTransaction( new TransactionWork<String>()
    		{
    			 @Override
                 public String execute( Transaction tx )
                 { 
    				 
    				 StatementResult result = tx.run(query);
    				 List<Object> res = result.single().fields().get(0).value().asList();
    				 //System.out.println(res);

    				 for(Object o: res){
    					 listOfNodes.addToNodeList(mapToNode((Map<String, Object>) o));
    				 }
    				 
    				 return "" ;
                 }
    		});
		//System.out.println( greeting );
    	}
		return listOfNodes;
		
	}
	
	public NodeV1 mapToNode(Map<String,Object> map)
	{       
		NodeV1 node = new NodeV1();

        for (Entry<String, Object> entry : map.entrySet()) {
        	NodePropertyV1 prop = new NodePropertyV1();
        	JSONObject jsonObj=new JSONObject();
        	List<String> list = Arrays.asList(entry.getKey().split("_"));
    		ArrayList<String> keys = new ArrayList<String>(list);
    		String oldKey =  entry.getKey().replace("_" + keys.get(keys.size()-1), "");
    		String value = entry.getValue().toString();
            try {
            	jsonObj.put(keys.get(keys.size()-1),value);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            
            prop.setJsonProperty(jsonObj);
            prop.setPartOfpropertyOldKey(oldKey);
            prop.setPropertyPos(keys);
            node.addToPropertyList(prop);
        }

	    return node;
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
	
	public static boolean isLong(String s) {
	    try
	    {
	        Long.parseLong(s);
	    }
	    catch(NumberFormatException ex)
	    {
	        return false;
	    }
	    return true;
	}
	
	public static boolean isDouble(String s) {
	    try
	    {
	        Double.parseDouble(s);
	    }
	    catch(NumberFormatException ex)
	    {
	        return false;
	    }
	    return true;
	}
	
	public JSONObject createJsonObject(List<String> linkTypesList, List<String> linkValueList, JSONObject json){
		String linkType = "";
		String linkValue = "";
		if(! linkTypesList.isEmpty()){
			linkType = linkTypesList.remove(0);
			linkValue = linkValueList.remove(0);
		}
		JSONObject tempJson = new JSONObject();
		if(linkTypesList.isEmpty()){
			if(linkValue.equals("jsonObject")){
				tempJson.put(linkType, json);
			}else if(linkValue.equals("jsonArray")){
				tempJson.put(linkType, createJsonArray(linkTypesList, linkValueList, json));
			}
		}else{
			
			if(linkValue.equals("jsonObject")){
				tempJson.put(linkType, createJsonObject(linkTypesList, linkValueList, json));
			}else if(linkValue.equals("jsonArray")){
				tempJson.put(linkType, createJsonArray(linkTypesList, linkValueList, json));
			}
			
		}
		
		return tempJson;
	}
	
	public JSONArray createJsonArray(List<String> linkTypesList, List<String> linkValueList, JSONObject json){
		JSONArray jArr = new JSONArray();
		String linkType = "";
		String linkValue = "";
		if(! linkTypesList.isEmpty()){
			linkType = linkTypesList.remove(0);
			linkValue = linkValueList.remove(0);
		}
		if(linkType.isEmpty()){
			if(json.containsKey("tempParameterKey")){
				jArr.add(json.get("tempParameterKey"));
			}else if(json.containsKey("value")){
				String temp = (String) json.get("value");
				if(isInteger(temp)){
					int tempValue = Integer.parseInt(temp);
					json.remove("value");
					json.put("value", tempValue);
					jArr.add(json);
				}else{
					jArr.add(json);
				}
			}else{
				jArr.add(json);
			}
			
		}else{
			jArr.add(createJsonObject(linkTypesList, linkValueList, json));
		}

		return jArr;	
	}
	
	public JSONObject mergeTwoJson(JSONObject json1, JSONObject json2){
		Set keys1 = json1.keySet();
		Set keys2 = json2.keySet();
		String key = "";
		for (Object s : keys1) {
		  if (keys2.contains(s)) {
		    key = s.toString();
		  }
		}
		if(key.equals("")){
			json1.putAll(json2);
		}else{
			if((json1.get(key) instanceof JSONArray)){
				for(int i=0; i<((JSONArray)json2.get(key)).size(); i++){
					((JSONArray) json1.get(key)).add(((JSONArray) json2.get(key)).get(i));
				}
			}else if(json2.get(key) instanceof JSONArray){
				for(int i=0; i<((JSONArray)json2.get(key)).size(); i++){
					((JSONArray) json1.get(key)).add(((JSONArray) json2.get(key)).get(i));
				}
			}else{
				mergeTwoJson((JSONObject)json1.get(key), (JSONObject)json2.get(key));
			}
		}
		
		return json1;
	}
	
	public String createNodeProperties(String propertyType, JSONObject properties)
    {
    	String tmp = "";
    	Object[] jsonKeysList =  properties.keySet().toArray();
    	
    	for(Object o : jsonKeysList){
    		if (properties.get(o) instanceof JSONObject) {
    			tmp +=  createNodeProperties(propertyType + "_" + ((String) o), (JSONObject) properties.get(o)) + ",";
    		}else if(properties.get(o) instanceof JSONArray){
    			int i = 0;
    			for(Object c : (JSONArray) properties.get(o)){
					if(c.getClass().getName().toString().matches("java.lang.String")){
	        			tmp += " " + propertyType + "_"  + ((String) o)  + "_" +  i + "_tempParameterKey: '" + c + "',";
	    				i++;
					}else{
						Object[] jsonInsideKeysList =  ((JSONObject) c).keySet().toArray();
	    				for(Object d : jsonInsideKeysList){
	        				tmp += " " + propertyType + "_" + ((String) o) + "_" + i + "_" + ((String) d) + ": '" + ((JSONObject) c).get(d) + "',";
	    				}
	    				i++;
					}
    			}

    		}else{
    				tmp += " " + propertyType + "_" + ((String) o) + ": '" + properties.get(o) + "',";
    		}	
    	}
    	tmp = tmp.replaceAll(",$", "");
    	return tmp;
    }
	
	public JSONArray returnLinks(String metaId){
		JSONArray links = new JSONArray();
		try	(Session session = driver.session())
    	{
    		String greeting = session.writeTransaction( new TransactionWork<String>()
    		{
    			 @Override
                 public String execute( Transaction tx )
                 { 
    				 StatementResult linkResult = tx.run("MATCH (a {meta_id:'" + metaId + "'})-[r]->(endNode) "
    				 									+ "RETURN collect([type(r), endNode.meta_id])");

    				 List<Object> res = linkResult.single().fields().get(0).value().asList();
    				 
    				 for(Object e:res){

    					 JSONObject temp = new JSONObject();
    					 temp.put("type", ((List) e).get(0));
    					 temp.put("target", ((List) e).get(1));
    					 links.add(temp);
    					 
    				 }
    				 
    				 return "" ;
                 }
    		});
		//System.out.println( greeting );
    	}
		return links;
	}
	
     
	public DataStoreResult createResult(NodeListV1 nodeList, boolean lazy){
		
		DataStoreResult result = new DataStoreResult();
		int count = 0;
		JSONArray tempArray = new JSONArray();
		if(! (nodeList==null)){
			
			for(int i = 0; i< nodeList.getSizeOfNodeList(); i++){
				
				JSONObject eventMeta = new JSONObject();
				JSONObject eventData = new JSONObject();
				JSONObject eventJson = new JSONObject();
				
				NodeV1 node = nodeList.getNodeList().get(i);
				for(int c=0; c<node.getSizeOfPropertyList(); c++){
					NodePropertyV1 property = node.getPropertyList().get(c);
					List<String> propertyPos = property.getPropertyPos();
					List<String> posType = property.getPosType();
					
					JSONObject temp = new JSONObject();
					if(propertyPos.get(0).equals("meta")){
						temp = createJsonObject(propertyPos, posType, property.getJsonProperty());
						eventMeta = mergeTwoJson(eventMeta, temp);
						
					}else if(propertyPos.get(0).equals("data")){
						temp = createJsonObject(propertyPos, posType, property.getJsonProperty());
						eventData = mergeTwoJson(eventData, temp);
					}
				}
				
				if(((JSONObject) eventMeta.get("meta")).containsKey("time")){
					String temp = (String) ((JSONObject) eventMeta.get("meta")).get("time");
					if(isDouble(temp)){
						long tempValue = (long) Double.parseDouble(temp);
						((JSONObject) eventMeta.get("meta")).remove("time");
						((JSONObject) eventMeta.get("meta")).put("time", tempValue);
					}
				}
	
				eventJson.putAll(eventMeta);
				eventJson.putAll(eventData);
				eventJson.put("links", returnLinks(((JSONObject) eventMeta.get("meta")).get("id").toString()));
				tempArray.add(eventJson);
				count++;
			}
			
			count = 0;
			
			//tempArray = sortJsonArray(tempArray, "meta_time" , comparator);
			//int max = (skip+limit > tempArray.size()) ? tempArray.size() : skip+limit;
			/*int max = tempArray.size();
			for(int e = 0; e < max; e++){
				result.addToEventsArray((JSONObject) tempArray.get(e));
				count++;
			}*/
			result.getEventsArray().addAll(tempArray);
			count = tempArray.size();
			
			result.setCount(lazy ? -1: count);
			
			return result;
		}else{
			return result;
		}
	}
	
	@Override
	public boolean store(JSONObject json) {
		// TODO Auto-generated method stub
		JSONObject eventMeta = (JSONObject) json.get("meta");
    	JSONObject eventData = (JSONObject) json.get("data");
    	JSONArray eventLinks = (JSONArray) json.get("links");
    	
    	String temp = createNodeProperties("meta", eventMeta) + ", " + createNodeProperties("data", eventData);
    	
    	String metaType = eventMeta.get("type").toString();
    	String metaId = eventMeta.get("id").toString();
    	
    	try	(Session session = driver.session())
    	{
    		String greeting = session.writeTransaction( new TransactionWork<String>()
    		{
    			 @Override
                 public String execute( Transaction tx )
                 { 
    				 tx.run( "MERGE (a:" + metaType 
 						 	+ "{"  + temp
			    			+ " }) RETURN a.meta_id + ', from node ' + id(a)");
    				 
    				 for(Object o : eventLinks){
    					 
    					 String linkTarget = ((JSONObject) o).get("target").toString();
    					 String linkType = ((JSONObject) o).get("type").toString();
    					 tx.run("MATCH (a),(b) WHERE a.meta_id = '" 
                				+ metaId + "' AND b.meta_id = '" 
    							+ linkTarget 
    							+ "' MERGE (a)-[r:"+ linkType + "]->(b)" 
    							+ " RETURN r");
    				 }
                     return "added";
                 }
    		});
    		//System.out.println( greeting );
    	}
    	return true;
	}
	
	public boolean storeManyEvents(List<JSONObject> jsonArr, int amount) {
		// TODO Auto-generated method stub
    	int i = 0;
    	boolean work = false;
    	if(amount <= jsonArr.size()){
	    	for(; i < amount; i++){
	    		//JsonParser parser = new JsonParser();
	    		//JsonObject o = parser.parse(jsonArr.get(i).toString()).getAsJsonObject();
	    		//work = store((JSONObject) o);
	    		//JsonObject gson = new JsonParser().parse(jsonArr.get(i).toString()).getAsJsonObject();
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
    	System.out.println(i);
    	return work;
	}

	@Override
	public JSONObject getEvent(String metaId) {
		// TODO Auto-generated method stub
		String query = "MATCH (n) WHERE n.meta_id = '" + metaId + "' RETURN collect(properties(n))";
		JSONObject eventMeta = new JSONObject();
		JSONObject eventData = new JSONObject();
		JSONObject eventJson = new JSONObject();
		NodeListV1 nodeList = new NodeListV1();
		nodeList = execGetQueryV1(query);
		if(! nodeList.getNodeList().isEmpty()){
			NodeV1 node = nodeList.getNodeList().get(0);
			for(int c=0; c<node.getSizeOfPropertyList(); c++){
				NodePropertyV1 property = node.getPropertyList().get(c);
				List<String> propertyPos = property.getPropertyPos();
				List<String> posType = property.getPosType();
				
				JSONObject temp = new JSONObject();
				if(propertyPos.get(0).equals("meta")){
					temp = createJsonObject(propertyPos, posType, property.getJsonProperty());
					eventMeta = mergeTwoJson(eventMeta, temp);
					
				}else if(propertyPos.get(0).equals("data")){
					temp = createJsonObject(propertyPos, posType, property.getJsonProperty());
					eventData = mergeTwoJson(eventData, temp);
				}
			}
			
			if(((JSONObject) eventMeta.get("meta")).containsKey("time")){
				String temp = (String) ((JSONObject) eventMeta.get("meta")).get("time");
				if(isDouble(temp)){
					Long tempValue = (long) Double.parseDouble(temp);
					((JSONObject) eventMeta.get("meta")).remove("time");
					((JSONObject) eventMeta.get("meta")).put("time", tempValue);
				}
			}
			
			eventJson.putAll(eventMeta);
			eventJson.putAll(eventData);
			eventJson.put("links", returnLinks(metaId));
			
			
			return eventJson;
			
		}else{
			return null;
		}
	}

	@Override
	public DataStoreResult getEvents(FilterParameterList filterList,
			String comparator, int skip, int limit, boolean lazy) {
		// TODO Auto-generated method stub
		
		String params = "WHERE ";
		ArrayList<FilterParameter> tempFilterList = filterList.getFilterList();
		for(int par = 0; par < tempFilterList.size(); par++)
		{
			params += "n." + tempFilterList.get(par).getKey() + " " 
						+ tempFilterList.get(par).getComparator() +" '" 
						+ tempFilterList.get(par).getValue() + "' AND ";
		}
		params = (!tempFilterList.isEmpty()) ? params.substring(0, params.length() - 4) : "";
		String query = "";
		int tempLimit = limit + skip;
		if(comparator.equals("<")){
			query = "MATCH (n) " + params + "WITH n ORDER BY n.meta_time "
						+ "RETURN collect(properties(n))[" + skip + ".." + tempLimit + "]";
		}else if(comparator.equals(">")){
			query = "MATCH (n) " + params + "WITH n ORDER BY n.meta_time DESC "
						+ "RETURN collect(properties(n))[" + skip + ".." + tempLimit + "]";
		}
		System.out.println(query);

		NodeListV1 nodeList = execGetQueryV1(query);
		DataStoreResult res = createResult(nodeList, lazy);
		return res;
		
	}
	
	@Override
	public DataStoreResult getArtifactsByGroup(String groupId,
			FilterParameterList filterList, String comparator, int skip,
			int limit) {
		// TODO Auto-generated method stub
		
		String params = "WHERE ";
		ArrayList<FilterParameter> tempFilterList = filterList.getFilterList();
		for(int par = 0; par < tempFilterList.size(); par++)
		{
			params += "n." + tempFilterList.get(par).getKey() + " " 
						+ tempFilterList.get(par).getComparator() +" '" 
						+ tempFilterList.get(par).getValue() + "' AND ";
		}
		params = (!tempFilterList.isEmpty()) ? params.substring(0, params.length() - 4) : "";
		String query = "";
		int tempLimit = limit + skip;
		if(comparator.equals("<")){
			query = "MATCH (n {data_gav_groupId: '" + groupId + "' }) "
						+ params + "WITH n ORDER BY n.meta_time "
						+ "RETURN collect(properties(n))[" + skip + ".." + tempLimit + "]";
		}else if(comparator.equals(">")){
			query = "MATCH (n {data_gav_groupId: '" + groupId + "' }) "
						+ params + "WITH n ORDER BY n.meta_time DESC "
						+ "RETURN collect(properties(n))[" + skip + ".." + tempLimit + "]";
		}
		
		NodeListV1 nodeList = execGetQueryV1(query);
		DataStoreResult res = createResult(nodeList, false);
		return res;
		
	}

	@Override
	public DataStoreResult getArtifactsByGroupAndArtifactId(String groupId,
			String artifactId, FilterParameterList filterList,
			String comparator, int skip, int limit) {
		// TODO Auto-generated method stub
		String params = "WHERE ";
		ArrayList<FilterParameter> tempFilterList = filterList.getFilterList();
		for(int par = 0; par < tempFilterList.size(); par++)
		{
			params += "n." + tempFilterList.get(par).getKey() + " " 
						+ tempFilterList.get(par).getComparator() +" '" 
						+ tempFilterList.get(par).getValue() + "' AND ";
		}
		params = (!tempFilterList.isEmpty()) ? params.substring(0, params.length() - 4) : "";
		String query = "";
		int tempLimit = limit + skip;
		if(comparator.equals("<")){
			query = "MATCH (n {data_gav_groupId: '" + groupId 
						+ "', data_gav_artifactId: '" + artifactId + "' }) "
						+ params + "WITH n ORDER BY n.meta_time "
						+ "RETURN collect(properties(n))[" + skip + ".." + tempLimit + "]";
		}else if(comparator.equals(">")){
			query = "MATCH (n {data_gav_groupId: '" + groupId 
						+ ", data_gav_artifactId: '" + artifactId + "' }) "
						+ params + "WITH n ORDER BY n.meta_time DESC "
						+ "RETURN collect(properties(n))[" + skip + ".." + tempLimit + "]";
		}
		
		NodeListV1 nodeList = execGetQueryV1(query);
		DataStoreResult res = createResult(nodeList, false);
		return res;
	}
	
	@Override
	public JSONObject getArtifactByGAV(String groupId, String artifactId,
			String version) {
		// TODO Auto-generated method stub
		
		String query = "MATCH (n {data_gav_groupId: '" + groupId 
							+ "', data_gav_artifactId: '" + artifactId 
							+ "', data_gav_version: '" + version + "' }) "
							+ "RETURN collect(properties(n))";
		
		NodeListV1 nodeList = execGetQueryV1(query);
		if(nodeList.getSizeOfNodeList() != 0){
			JSONObject res = createResult(nodeList, false).getEventFromEventsArray(0);
			return res;
		}else{
			return null;
		}
	}
	
	
	@Override
	public List<Object> getUpstreamEvents(String eventId,
			List<String> linkTypes, ConcurrentMap<String, String> visitedMap,
			int limit, int levels) {
		// TODO Auto-generated method stub
		final List<Object> upstreamEvents = new ArrayList<>();
		JSONObject startEvent = new JSONObject();
		startEvent = getEvent(eventId);
		if(!(startEvent == null)){
			upstreamEvents.add(startEvent);
			performUpstreamSearch(startEvent, linkTypes, visitedMap, limit, levels, upstreamEvents);
			
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
		String metaId = ((JSONObject) event.get("meta")).get("id").toString();
		String linkTypesParams = "";
		if(linkTypes.size()>0){
			linkTypesParams += ":";
		}
		for(int i=0; i< linkTypes.size(); i++){
			if(i >= linkTypes.size()-1){
				linkTypesParams += linkTypes.get(i);
			}else{
				linkTypesParams += linkTypes.get(i) + "|";
			}	
		}
		int tempLimit = limit -1;
		String query = "MATCH (a {meta_id: '" + metaId + "'})-[r" + linkTypesParams + "*.." + levels + "]->(endNode) "
					+"WITH endNode ORDER BY endNode.meta_time DESC RETURN collect( DISTINCT properties(endNode))[.." + tempLimit +"]";
		
		
		NodeListV1 listOfNodes = new NodeListV1();
		listOfNodes = execGetQueryV1(query);
		DataStoreResult res = createResult(listOfNodes, false);
		events.addAll(res.getEventsArray());
		return false;
	}
	
	@Override
	public List<Object> getDownstreamEvents(String eventId,
			List<String> linkTypes, ConcurrentMap<String, String> visitedMap,
			int limit, int levels) {
		// TODO Auto-generated method stub
		final List<Object> downstreamEvents = new ArrayList<>();
		JSONObject startEvent = new JSONObject();
		startEvent = getEvent(eventId);
		if(!(startEvent == null)){
			downstreamEvents.add(startEvent);
			performDownstreamSearch(eventId, linkTypes, visitedMap, limit, levels, downstreamEvents);
			
			return downstreamEvents;
		}else{
			return null;
		}
	}

	@Override
	public boolean performDownstreamSearch(String eventId,
			List<String> linkTypes, ConcurrentMap<String, String> visitedMap,
			int limit, int levels, List<Object> events) {
		// TODO Auto-generated method stub
		
		String linkTypesParams = "";
		if(linkTypes.size()>0){
			linkTypesParams += ":";
		}
		for(int i=0; i< linkTypes.size(); i++){
			if(i >= linkTypes.size()-1){
				linkTypesParams += linkTypes.get(i);
			}else{
				linkTypesParams += linkTypes.get(i) + "|";
			}	
		}
		int tempLimit = limit -1;
		String query = "MATCH (a {meta_id: '" + eventId + "'})<-[r" + linkTypesParams + "*.." + levels + "]-(endNode) "
					+"WITH endNode ORDER BY endNode.meta_time RETURN collect( DISTINCT properties(endNode))[.." + tempLimit + "]";
		
		
		NodeListV1 listOfNodes = new NodeListV1();
		listOfNodes = execGetQueryV1(query);

		DataStoreResult res = createResult(listOfNodes, false);
		events.addAll(res.getEventsArray());
		return false;
	}
	
	@Override
	public void removeAllNodes() {
		// TODO Auto-generated method stub
		execQuery("MATCH (n) DETACH DELETE n");	
	}

}

