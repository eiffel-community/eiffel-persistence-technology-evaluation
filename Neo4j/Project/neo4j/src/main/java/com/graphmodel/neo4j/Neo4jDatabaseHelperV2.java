package com.graphmodel.neo4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

public class Neo4jDatabaseHelperV2 implements AutoCloseable, DatabaseHelper {

	private final Driver driver;

    public Neo4jDatabaseHelperV2( String uri, String user, String password )
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
	
	public NodeV2 execGetQueryV2(String query){
		NodeV2 listOfNodes = new NodeV2();
		
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
    					 Map<String,String> map = (Map<String,String>)((List<Object>) o).get(2);
    					 NodePropertyV2 n = new NodePropertyV2();
        				 n = mapToNode(map);
        				 n.setLinkType(((List<Object>)o).get(0).toString());
        				 n.setLinkValue(((List<Object>)o).get(1).toString());
        				 listOfNodes.addToListOfProperties(n);
    				 }
    				 
    				 return "" ;
                 }
    		});
		//System.out.println( greeting );
    	}
		return listOfNodes;
		
	}
	
	public NodeListV2 execGetQueryV2_2(String query){
		NodeV2 listOfNodeProperties = new NodeV2();
		NodeListV2 listOfNodes = new NodeListV2();
		try	(Session session = driver.session())
    	{
    		String greeting = session.writeTransaction( new TransactionWork<String>()
    		{
    			 @Override
                 public String execute( Transaction tx )
                 { 
    				 
    				 StatementResult result = tx.run(query);
    				 List<Object> res = result.single().fields().get(0).value().asList();
    				
    				 
    				 for(Object o: res){
    					 for(Object e: (List<Object>) o){
	    					 Map<String,String> map = (Map<String,String>)((List<Object>) o).get(2);
	    					 NodePropertyV2 n = new NodePropertyV2();
	        				 n = mapToNode(map);
	        				 n.setLinkType(((List<Object>)o).get(0).toString());
	        				 n.setLinkValue(((List<Object>)o).get(1).toString());
	        				 listOfNodeProperties.addToListOfProperties(n);
    					 }
    					 listOfNodes.addToNodeList(listOfNodeProperties);
    				 }
    				 
    				 return "" ;
                 }
    		});
		//System.out.println( greeting );
    	}
		return listOfNodes;
		
	}
	
	public List<String> execGetIdWithQueryV2(String query, boolean isMeta){
		List<String> id = new ArrayList<String>();
		try	(Session session = driver.session())
    	{
    		String greeting = session.writeTransaction( new TransactionWork<String>()
    		{
    			 @Override
                 public String execute( Transaction tx )
                 { 
    				 StatementResult result = tx.run(query);
    				 List<Object> res = result.single().fields().get(0).value().asList();
    				 id.addAll(toId(res, isMeta));
    				 
				 
				 return "" ;
             }
		});
	//System.out.println( greeting );
	}
	return id;
		
	}
	
	public List<String> toId(List<Object> list, boolean isMeta){
		List<String> id = new ArrayList<String>();
		if(isMeta){
			for (Object value : list) {
				id.add(value.toString());
			}
		}else{
			for (Object value : list) {
				String[] idList = ((String) value).substring(0).split("__");
				id.add(idList[0].toString());
			}
		}
		return id;
	}
	
	protected boolean extraNodeObject(String metaId, String oldSpecialKey, int number, String linkType, String linkValue, JSONObject json) {
		// TODO Auto-generated method stub
		String specialKey = metaId + "__" + linkType.toString() + "__" + number;
		
		if(oldSpecialKey.equals("")){
			execQuery("CREATE (a:"+ linkType + "{specialKey: '" + specialKey 
					+ "'}) WITH a MATCH (b) WHERE b.id='"  + metaId + "' CREATE (b)-[r:" 
					+ linkType.toString() + "{type:'" + linkValue + "'}]->(a) RETURN ID(a)");
		}else{
			execQuery("CREATE (a:"+ linkType + "{specialKey: '" + specialKey 
					+ "'}) WITH a MATCH (b) WHERE b.specialKey='"  + oldSpecialKey + "' CREATE (b)-[r:" 
					+ linkType.toString() + "{type:'" + linkValue + "'}]->(a) RETURN ID(a)");
		}
		number++;	
		
		
		Object[] keysList =  json.keySet().toArray();
		for(Object e: keysList){
			if(json.get(e) instanceof JSONObject){
				 extraNodeObject(metaId, specialKey , number, e.toString(), "jsonObject", (JSONObject) json.get(e));
			}else if (json.get(e) instanceof JSONArray){
				 extraNodeArray(metaId, specialKey, number, e, (JSONArray) json.get(e));
			}else{
				 execQuery("MATCH (b) WHERE b.specialKey= '" + specialKey + "' SET b." + e + " = '" + json.get(e) + "' RETURN b");
			}
		}
		//execQuery("MATCH (a) WHERE a.specialKey='" + specialKey + "' REMOVE a.specialKey RETURN a");
		return true;
	}
	
	protected boolean extraNodeArray(String metaId, String oldSpecialKey, int number, Object linkType, JSONArray jsonArray) {
		// TODO Auto-generated method stub
		for(int i=0; i<jsonArray.size(); i++){
			if(jsonArray.get(i) instanceof String){
				JSONObject tempJson = new JSONObject();
				tempJson.put("tempParameterKey", jsonArray.get(i));
				extraNodeObject(metaId, oldSpecialKey , number, linkType.toString(), "jsonArray", tempJson);
				number++;
			}else{
				extraNodeObject(metaId, oldSpecialKey , number, linkType.toString(), "jsonArray", (JSONObject) jsonArray.get(i));
				number++;
			}
			
		}
		return true;
	}

	public NodePropertyV2 mapToNode(Map<String,String> map)
	{       
		NodePropertyV2 node = new NodePropertyV2();
        JSONObject jsonObj=new JSONObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
            	if(key.equals("specialKey")){
            		node.setSpecialKey(value);
            	}else{
            		jsonObj.put(key,value);
            	}
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }                           
        }
        node.setPropertyJson(jsonObj);
	    return node;
	}
	
	public JSONObject mapToJson(Map<String, Object> map)
	{       
        JSONObject jsonObj=new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            try {
            	if(key.equals("specialKey")){
            	}else{
            		jsonObj.put(key,value);
            	}
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }                           
        }
	    return jsonObj;
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
    				 StatementResult linkResult = tx.run("MATCH (a {id: '" + metaId + "'})-[r]->(endNode) "
    				 					+ "WHERE r.type='Connection' RETURN collect([type(r), endNode.id])");

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
	
	public ArrayList<String> stringToList(String str){
		str = str.replace("[", "");
		str = str.replace("]", "");
		List<String> list = Arrays.asList(str.split("\\s*,\\s*"));
		ArrayList<String> items = new ArrayList<String>(list);
		return items;
	}
	
	public JSONObject createJsonObject(List<String> linkTypesList, ArrayList<String> linkValueList, JSONObject json){
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
				//System.out.println("hej");
				tempJson.put(linkType, createJsonArray(linkTypesList, linkValueList, json));
			}
			
		}
		
		return tempJson;
	}
	
	public JSONArray createJsonArray(List<String> linkTypesList, ArrayList<String> linkValueList, JSONObject json){
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
	
	public JSONArray sortJsonArray(JSONArray jsonArr, String parameter, String comparator){
    	
        JSONArray sortedJsonArray = new JSONArray();
        String[] param = parameter.substring(0).split("_");
        
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.size(); i++) {
            jsonValues.add((JSONObject) jsonArr.get(i));
        }
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "meta_id";

            @Override
            public int compare(JSONObject a, JSONObject b) {
            	JSONObject jA = new JSONObject();
            	JSONObject jB = new JSONObject();
                String valA = new String();
                String valB = new String();

                try {
                	jA = a;
                	jB = b;
                	for(int c=0; c< param.length-1; c++){
                		jA = (JSONObject) jA.get(param[c]);
                		jB = (JSONObject) jB.get(param[c]);
                	}
                    //valA = (String) ((JSONObject) a.get("meta")).get("id");
                   // valB = (String) ((JSONObject) b.get("meta")).get("id");
                	//System.out.println(jA.get(param[param.length-1]));
                	valA = (jA.get(param[param.length-1]) instanceof Long) ? Long.toString((long) jA.get(param[param.length-1])) : (String) jA.get(param[param.length-1]);
                	
                    valB = (jB.get(param[param.length-1]) instanceof Long) ? Long.toString((long) jB.get(param[param.length-1])) : (String) jB.get(param[param.length-1]);
                } 
                catch (JSONException e) {
                    //do something
                }

                return valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });
        
        switch (comparator) {
        case "<":
        	for (int i = 0; i < jsonArr.size(); i++) {
                sortedJsonArray.add(jsonValues.get(i));
            }
        	return sortedJsonArray;
        case ">":
        	for (int i = jsonArr.size()-1; i >= 0; i--) {
                sortedJsonArray.add(jsonValues.get(i));
            }
        	return sortedJsonArray;
        default:
            throw new IllegalArgumentException("Invalid comparator: " + comparator);
        }
        
        
    }
	
	@Override
	public boolean store(JSONObject json) {
		// TODO Auto-generated method stub
		JSONObject eventMeta = (JSONObject) json.get("meta");
    	JSONObject eventData = (JSONObject) json.get("data");
    	JSONArray eventLinks = (JSONArray) json.get("links");
    	
    	String metaType = eventMeta.get("type").toString();
    	String metaId = eventMeta.get("id").toString();
    	
    	Object[] metaKeysList =  eventMeta.keySet().toArray();
    	Object[] dataKeysList =  eventData.keySet().toArray();
    	
    	execQuery("CREATE (a:" + metaType + "{ id:'"  + metaId + "' }) RETURN a");
    	
    	for(Object e: metaKeysList){
			if(eventMeta.get(e) instanceof JSONObject){
				 extraNodeObject(metaId, "", 0, e.toString(), "jsonObject" , (JSONObject) eventMeta.get(e));
			}else if (eventMeta.get(e) instanceof JSONArray){
				 extraNodeArray(metaId, "", 0, e, (JSONArray) eventMeta.get(e));
			}else if(e.toString().matches("id")){
			}else{
				 execQuery("MATCH (a:" + metaType + ") WHERE a.id = '" + metaId + "' SET a." +  e + "= '" + eventMeta.get(e) + "' RETURN a");
			}
    	}
    	if(dataKeysList.length != 0){
    		extraNodeObject(metaId, "", 0, "data", "jsonObject", eventData);
    	}
    	
    	if(eventLinks.size() != 0){
    		for(Object o : eventLinks){
   			 
   			 String linkTarget = ((JSONObject) o).get("target").toString();
   			 String linkType = ((JSONObject) o).get("type").toString();
   			 execQuery("MATCH (a),(b) WHERE a.id = '" 
       				+ metaId + "' AND b.id = '" 
    					+ linkTarget 
    					+ "' CREATE (a)-[r:"+ linkType +  "{type:'Connection'}]->(b)" 
    					+ " RETURN r");
   			 
   		 	}
    	}
    	return true;
	}
	
	public boolean storeManyEvents(List<JSONObject> jsonArr, int amount){
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
    	System.out.println(i);
    	return work;
    }
	
	@Override
	public JSONObject getEvent(String metaId) {
		// TODO Auto-generated method stub
		JSONObject eventMeta = new JSONObject();
		JSONObject tempEventMeta = new JSONObject();
		JSONObject eventData = new JSONObject();
		NodeV2 listOfNodes = new NodeV2();
		JSONObject eventJson = new JSONObject();
		String query1 = "MATCH (a {id: '" + metaId + "'})-[r]->(endNode) WHERE r.type='jsonObject' "
						+ "or r.type='jsonArray' RETURN DISTINCT collect([type(r),r.type, properties(a)])";
		String query2 = "MATCH (a {id: '" + metaId + "'})-[r*..6]->(endNode) "+
						"WHERE ALL(rel IN r WHERE rel.type='jsonObject' or rel.type='jsonArray')" + 
						"RETURN DISTINCT collect([extract (rel in r | type(rel) ),extract (rel in r | rel.type ), properties(endNode)])";
		
		listOfNodes = execGetQueryV2(query1);
		
		
		tempEventMeta = listOfNodes.getPropertyFromList(0).getPropertyJson();
		
		if(tempEventMeta.containsKey("time")){
			String temp = (String) tempEventMeta.get("time");
			if(isLong(temp)){
				Long tempValue = Long.parseLong(temp);
				tempEventMeta.remove("time");
				tempEventMeta.put("time", tempValue);
			}
		}
		
		listOfNodes = execGetQueryV2(query2);
		
		
		for(int c=0; c<listOfNodes.getSizeOfPropertiesList(); c++){
			ArrayList<String> linkTypesList = stringToList(listOfNodes.getPropertyFromList(c).getLinkType());
			ArrayList<String> linkValueList = stringToList(listOfNodes.getPropertyFromList(c).getLinkValue());
			JSONObject temp = new JSONObject();
			if(! linkTypesList.get(0).equals("data")){
				temp = createJsonObject(linkTypesList, linkValueList , listOfNodes.getPropertyFromList(c).getPropertyJson());
				tempEventMeta = mergeTwoJson(tempEventMeta, temp);
				
			}else if(linkTypesList.get(0).equals("data")){
				temp = createJsonObject(linkTypesList, linkValueList , listOfNodes.getPropertyFromList(c).getPropertyJson());
				eventData = mergeTwoJson(eventData, temp);
			}
		}
		eventMeta.put("meta", tempEventMeta);
		eventJson.putAll(eventMeta);
		eventJson.putAll(eventData);
		
		
		eventJson.put("links", returnLinks(metaId));
    			
		return eventJson;
	}
	
	
	public String getInnerParamAsString(FilterParameterListV2 parametersList, String key){
		String res = "";
		ArrayList<FilterParameter> tempList = parametersList.getListOfSpecificParameters(key).getFilterList();
		for(int i = 0; i < tempList.size(); i++){
			FilterParameter param = tempList.get(i);
			res += key + "." + param.getKey() + " " + param.getComparator() + " '" + param.getValue() + "' AND ";
		}
		res = res.substring(0, res.length() - 4);
		//System.out.println(res);
		return res;
	}
	
	public String createGetQuery(FilterParameterList filterList, FilterParameterListV2 paramList, boolean isMeta){
		
		Set<String> keys = paramList.getParametersList().keySet();
		String nodesLabels = "";
		String nodeConections = "";
		String nodeParams = " WHERE ";
		String conectionParameters = "";
		String tempStartNode = (!isMeta && !keys.isEmpty()) ? keys.iterator().next() : "";
		String specialKey = (!isMeta && !keys.isEmpty()) ? tempStartNode  + ".specialKey" : "";
		specialKey = (!filterList.getFilterList().isEmpty()) ? specialKey : "n.id";
		int r = 0;
		for(String e: keys){
			if(isMeta){
				nodesLabels += "(" + e + "), ";
				nodeParams += getInnerParamAsString(paramList, e) + " AND ";
				if(! e.equals("meta")){
					nodeConections += "MATCH (meta)-[r_" + r + "*]-(" + e + ") ";
					conectionParameters += "ALL(rel_" + r + " IN r_" + r 
											+ " WHERE rel_" + r + ".type='jsonObject' or rel_" 
											+ r + ".type='jsonArray') AND ";
				}
				r++;
			}else{
				nodesLabels += "(" + e + "), ";
				nodeParams += getInnerParamAsString(paramList, e) + " AND ";
				if(! e.equals(tempStartNode)){
					nodeConections += "MATCH (" + tempStartNode + ")-[r_" + r + "*]-(" + e + ") ";
					conectionParameters += "ALL(rel_" + r + " IN r_" + r 
											+ " WHERE rel_" + r + ".type='jsonObject' or rel_" 
											+ r + ".type='jsonArray') AND ";
				}
				r++;
			}
		}
		
		String returnInfo = (isMeta) ? "meta.id" : specialKey;
		nodesLabels = (!filterList.getFilterList().isEmpty()) ? nodesLabels.substring(0, nodesLabels.length() - 2) : "(n)";
		nodeParams = (!nodeConections.isEmpty()) ? nodeParams : nodeParams.substring(0, nodeParams.length() - 4) ;
		nodeParams = (!filterList.getFilterList().isEmpty()) ? nodeParams : "";
		conectionParameters = (!nodeConections.isEmpty()) ? conectionParameters.substring(0, conectionParameters.length() - 4) : "" ;
		conectionParameters = (!filterList.getFilterList().isEmpty()) ? conectionParameters : "WHERE NOT EXISTS(n.`specialKey`)";
		
		String query = "MATCH " + nodesLabels
					+ " " + nodeConections
					+ nodeParams
					+ conectionParameters
					+ " Return distinct collect(" + returnInfo + ")";
		
		//System.out.println(query);
		return query;
	}

	public DataStoreResult createResult(NodeListV2 nodeList, int skip, int limit, boolean lazy){
		DataStoreResult result = new DataStoreResult();
		int count = 0;
		JSONArray tempArray = new JSONArray();
		if(! (nodeList==null)){
			
			
			return result;
		}else{
			return result;
		}
	}
	
	@Override
	public DataStoreResult getEvents(FilterParameterList filterList,
			String comparator, int skip, int limit, boolean lazy) {
		// TODO Auto-generated method stub
		
		JSONArray tempArray = new JSONArray();
		DataStoreResult result = new DataStoreResult();
		int count = 0;
		
		NodeListV1 nodeList = new NodeListV1();
		FilterParameterListV2 paramList = new FilterParameterListV2();
		paramList.addParameterListToList(filterList);
		Boolean isMeta = paramList.getParametersList().containsKey("meta");
		
		String query = createGetQuery(filterList, paramList, isMeta);
		List<String> idList = execGetIdWithQueryV2(query, isMeta);
		//System.out.println(idList);
		for(int i=0; i<idList.size(); i++){
			tempArray.add(getEvent(idList.get(i)));
		}
		tempArray = sortJsonArray(tempArray, "meta_time", comparator);
		//System.out.println(tempArray);

		int max = (skip+limit > tempArray.size()) ? tempArray.size() : skip+limit;
		for(int c=skip;c<max;c++){
			result.addToEventsArray((JSONObject) tempArray.get(c));
			count++;
		}
		
		result.setCount(lazy ? -1: count);
		
		return result;
		
	}

	@Override
	public DataStoreResult getArtifactsByGroup(String groupId,
			FilterParameterList filterList, String comparator, int skip,
			int limit) {
		// TODO Auto-generated method stub
		DataStoreResult result = new DataStoreResult();
		try {
			filterList.addFilterParameter("data_gav_groupId", groupId, "=");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = getEvents(filterList, comparator, skip, limit, false);
		
		
		return result;
	}

	@Override
	public DataStoreResult getArtifactsByGroupAndArtifactId(String groupId,
			String artifactId, FilterParameterList filterList,
			String comparator, int skip, int limit) {
		// TODO Auto-generated method stub
		DataStoreResult result = new DataStoreResult();
		try {
			filterList.addFilterParameter("data_gav_groupId", groupId, "=");
			filterList.addFilterParameter("data_gav_artifactId", artifactId, "=");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = getEvents(filterList, comparator, skip, limit, false);

		return result;
	}
	
	@Override
	public JSONObject getArtifactByGAV(String groupId, String artifactId,
			String version) {
		// TODO Auto-generated method stub
		DataStoreResult result = new DataStoreResult();
		JSONArray tempArray = new JSONArray();
		int count = 0;
		String query = "MATCH (n {groupId: '" + groupId 
						+ "', artifactId: '" + artifactId 
						+ "', version: '" + version + "' }) "
						+ "RETURN collect(n.specialKey)";
		List<String> idList = execGetIdWithQueryV2(query, false);
		for(int i=0; i<idList.size(); i++){
			tempArray.add(getEvent(idList.get(i)));
			count++;
		}
		result.setEventsArray(tempArray);
		result.setCount(count);
		return result.getEventFromEventsArray(0);
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

		String query = "MATCH (a {id: '" + metaId + "'})-[r" + linkTypesParams + "*.." + levels + "]->(endNode) "
					+"WITH endNode ORDER BY endNode.time DESC RETURN collect( DISTINCT endNode.id)";
		
		
		List<String> idList = execGetIdWithQueryV2(query, false);
		limit = (limit < idList.size()) ? limit-1 : idList.size();
		for(int i=0; i<limit; i++){
			events.add(getEvent(idList.get(i)));
		}
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

		String query = "MATCH (a {id: '" + eventId + "'})<-[r" + linkTypesParams + "*.." + levels + "]-(endNode) "
					+"WITH endNode ORDER BY endNode.time RETURN collect( DISTINCT endNode.id)";
		
		
		List<String> idList = execGetIdWithQueryV2(query, false);
		System.out.println("List");
		System.out.println(idList);
		limit = (limit < idList.size()) ? limit-1 : idList.size();
		for(int i=0; i<limit; i++){
			events.add(getEvent(idList.get(i)));
		}
		return false;
	}
	
	@Override
	public void removeAllNodes() {
		// TODO Auto-generated method stub
		execQuery("MATCH (n) DETACH DELETE n");
	}

}


/*
MATCH (n { id: '3ae8debd-9cd4-4895-8fc3-2aaa45c5c415' })-[r*..6]->(endNode)
WHERE ALL(rel IN r WHERE (rel.type='jsonObject' Or rel.type='jsonArray'))
RETURN DISTINCT [properties(n), collect([extract (rel in r | type(rel) ),extract (rel in r | rel.type ), properties(endNode)])]
*/
/*
MATCH (n {type:'EiffelSourceChangeSubmittedEvent', version:'1.0.0'})-[r*..6]-(m)
WHERE ALL(rel IN r WHERE rel.type='jsonObject' or rel.type='jsonArray') AND (m.name='John Doe' or m.value='1' or m.id='johnxxx')
Return distinct m.specialKey
*/
/*
MATCH (n {type:'EiffelArtifactCreatedEvent', version:'1.0.0'})-[r*..6]-(m)
WHERE ALL(rel IN r WHERE rel.type='jsonObject' or rel.type='jsonArray') AND ((m.artifactId ='third-party-library' and m.groupId = 'com.othercompany.library' and m.version='3.2.4') or m.value='0')
Return distinct coalesce(m.specialKey, m.id)
*/
/*
MATCH (a {type:'EiffelArtifactCreatedEvent', version:'1.0.0', time:'1490777046670'}), (b {artifactId :'component-1' ,groupId : 'com.mycompany.myproduct' ,version:'1.0.0'}), (c {value:'0'})
MATCH (a)-[r*]-(b) MATCH (a)-[s*]-(c) 
WHERE ALL(rel IN r WHERE rel.type='jsonObject' or rel.type='jsonArray') AND ALL(rela IN s WHERE rela.type='jsonObject' or rela.type='jsonArray')
Return distinct collect(a.id)
*/

//MATCH (n) WHERE n.version='1.0.0' OR n.name = 'John Doe' RETURN DISTINCT COALESCE(n.specialKey, n.id)
