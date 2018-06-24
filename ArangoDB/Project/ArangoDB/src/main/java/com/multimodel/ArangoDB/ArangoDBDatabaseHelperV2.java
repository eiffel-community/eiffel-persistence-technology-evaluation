package com.multimodel.ArangoDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

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
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.EdgeEntity;
import com.arangodb.entity.VertexEntity;

public class ArangoDBDatabaseHelperV2 implements DatabaseHelper {

	private ArangoDB arangoDB = new ArangoDB.Builder().build(); 
	private static ArangoDatabase myDatabase;
	private CollectionEntity myCollectionEntity;
	private ArangoCollection myCollection;
	protected static final String GRAPH_NAME = "traversalGraph";
	protected static final String EDGE_COLLECTION_NAME = "edges";
	protected static final String VERTEXT_COLLECTION_NAME = "nodes";
	
	private String dbName = "Eiffel";
	private String collectionName = "Events";
	
	/*public void arangoDBSetUp(){

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
	*/
	public void arangoDBGraphSetUp(){
		if (arangoDB == null) {
			arangoDB = new ArangoDB.Builder().build();
		}
		try {
			arangoDB.db(dbName).drop();
		} catch (final ArangoDBException e) {
		}
		//arangoDB.createDatabase(dbName);
		
		try {
			arangoDB.createDatabase(dbName);
		} catch (ArangoDBException e) {
			System.err.println("Failed to create database: " + dbName + "; " + e.getMessage());
		}
		
		myDatabase = arangoDB.db(dbName);

		final Collection<EdgeDefinition> edgeDefinitions = new ArrayList<EdgeDefinition>();
		/*final EdgeDefinition edgeDefinition = new EdgeDefinition().collection(EDGE_COLLECTION_NAME)
				.from(VERTEXT_COLLECTION_NAME).to(VERTEXT_COLLECTION_NAME);*/
		final EdgeDefinition edgeDefinition = new EdgeDefinition().collection(EDGE_COLLECTION_NAME)
				.from(collectionName).to(collectionName);
		edgeDefinitions.add(edgeDefinition);
		try {
			myDatabase.createGraph(GRAPH_NAME, edgeDefinitions, null);
			//addExampleElements();
		} catch (final ArangoDBException ex) {

		}
		/*
		try {
			myCollectionEntity = myDatabase.createCollection(collectionName);
		} catch (ArangoDBException e) {
			System.err.println("Failed to create collection: " + collectionName + "; " + e.getMessage());
		}*/
		
		myCollection = myDatabase.collection(collectionName);

	}
	
	/*private static void addExampleElements() throws ArangoDBException {

		// Add circle circles
		final VertexEntity vA = createVertex(new Node("A", "1"));
		final VertexEntity vB = createVertex(new Node("B", "2"));
		final VertexEntity vC = createVertex(new Node("C", "3"));
		final VertexEntity vD = createVertex(new Node("D", "4"));
		final VertexEntity vE = createVertex(new Node("E", "5"));
		final VertexEntity vF = createVertex(new Node("F", "6"));
		final VertexEntity vG = createVertex(new Node("G", "7"));
		final VertexEntity vH = createVertex(new Node("H", "8"));
		final VertexEntity vI = createVertex(new Node("I", "9"));
		final VertexEntity vJ = createVertex(new Node("J", "10"));
		final VertexEntity vK = createVertex(new Node("K", "11"));

		// Add relevant edges - left branch:
		saveEdge(new NodeEdge(vA.getId(), vB.getId(), false, true, "left_bar"));
		saveEdge(new NodeEdge(vB.getId(), vC.getId(), false, true, "left_blarg"));
		saveEdge(new NodeEdge(vC.getId(), vD.getId(), false, true, "left_blorg"));
		saveEdge(new NodeEdge(vB.getId(), vE.getId(), false, true, "left_blub"));
		saveEdge(new NodeEdge(vE.getId(), vF.getId(), false, true, "left_schubi"));

		// Add relevant edges - right branch:
		saveEdge(new NodeEdge(vA.getId(), vG.getId(), false, true, "right_foo"));
		saveEdge(new NodeEdge(vG.getId(), vH.getId(), false, true, "right_blob"));
		saveEdge(new NodeEdge(vH.getId(), vI.getId(), false, true, "right_blub"));
		saveEdge(new NodeEdge(vG.getId(), vJ.getId(), false, true, "right_zip"));
		saveEdge(new NodeEdge(vJ.getId(), vK.getId(), false, true, "right_zup"));
	}
*/
	private static EdgeEntity saveEdge(final NodeEdge edge) throws ArangoDBException {
		return myDatabase.graph(GRAPH_NAME).edgeCollection(EDGE_COLLECTION_NAME).insertEdge(edge);
		
	}

	private static VertexEntity createVertex(final Node vertex) throws ArangoDBException {
		return myDatabase.graph(GRAPH_NAME).vertexCollection(VERTEXT_COLLECTION_NAME).insertVertex(vertex);
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
    	System.out.println(i);
    	return work;
	}
	
	@Override
	public boolean store(JSONObject json) {
		// TODO Auto-generated method stub
		String key = "";
		boolean added = false;
		try {
			String metaId = ((JSONObject) json.get("meta")).get("id").toString();
			String metaType = ((JSONObject) json.get("meta")).get("type").toString();
			JSONArray links = (JSONArray) json.get("links");
			json.put("_key", metaId);
			DocumentCreateEntity<String> entity = arangoDB.db(dbName).collection(collectionName).insertDocument(json.toString());
			//VertexEntity node = createVertex(new Node(metaId, metaType));
			for(int i = 0; i < links.size(); i++){
				//String target = "nodes/" + ((JSONObject) links.get(i)).get("target").toString().toString();
				String target = collectionName + "/" + ((JSONObject) links.get(i)).get("target").toString().toString();
				String linkType = ((JSONObject) links.get(i)).get("type").toString().toString();
				//saveEdge(new NodeEdge(node.getId(), target, false, true, linkType));
				saveEdge(new NodeEdge(collectionName + "/" + metaId, target, false, true, linkType));

			}
			added = true;
		} catch (ArangoDBException e) {
			System.err.println("Failed to add JSONObject. " + e.getMessage());
		}
		return added;
	}

	@Override
	public JSONObject getEvent(String metaId) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		String query = "FOR document IN " + collectionName + " FILTER document.meta.id == '" + metaId + "' RETURN document";
		//String query = "FOR document IN traversalGraph FILTER document.meta.id == '" + metaId + "' RETURN document";
		try {	 
			if(arangoDB.db(dbName).collection(collectionName).documentExists(metaId)){
				  ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query, null, null,
				      BaseDocument.class);
				  cursor.forEachRemaining(aDocument -> {
				    json.putAll(new JSONObject(aDocument.getProperties()));	  
				  });
			}else{
				throw new ArangoDBException("Document is not in Database");
			}
		} catch (ArangoDBException e) {
			System.err.println("Failed to execute query. " + e.getMessage());
		}
		return json;
	}
	
	public String createQueryParameters(FilterParameterList filterList){
		String params = "";
		ArrayList<FilterParameter> tempList = filterList.getFilterList();
		for(int i = 0; i < tempList.size(); i++){
			FilterParameter p = tempList.get(i);
			String[] keys = p.getKey().split("_");
			params += "document.";
			for(String s: keys){
				params += s + ".";
			}
			params = params.substring(0, params.length() - 1);
			params += " " + p.getComparator() + " '" + p.getValue() + "' AND ";
		}
		params = params.substring(0, params.length() - 4);
		return params;
	}

	@Override
	public DataStoreResult getEvents(FilterParameterList filterList,
			String comparator, int skip, int limit, boolean lazy) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public void removeAllEvents() {
		// TODO Auto-generated method stub
		
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
			performUpstreamSearch(startEvent, linkTypes, visitedMap, limit-1, levels, upstreamEvents);
			
			return upstreamEvents;
		}else{
			return null;
		}
	}
	
	public String createLinkTypes(List<String> linkTypes){
		String filter = "";
		for(int i = 0; i < linkTypes.size(); i++){
			filter += " e.type == '" + linkTypes.get(i) + "' OR";
		}
		filter = filter.substring(0, filter.length()-2);
		return filter;	
	}

	@Override
	public boolean performUpstreamSearch(JSONObject event,
			List<String> linkTypes, ConcurrentMap<String, String> visitedMap,
			int limit, int levels, List<Object> events) {
		// TODO Auto-generated method stub
		HashMap<String, String> meta = (HashMap<String, String>) event.get("meta");
		String metaId = meta.get("id").toString();
		//String query = "FOR doc IN " + collectionName + " FILTER doc.meta.id == '" + metaId + "' FOR v IN 1..2 OUTBOUND doc " + EDGE_COLLECTION_NAME + " RETURN v";
		String query1 = "FOR doc IN Events FILTER doc.meta.id == '" + metaId 
						+ "' FOR v, e, p IN 1.." + levels + " OUTBOUND doc GRAPH '" 
						+ GRAPH_NAME + "' FILTER " + createLinkTypes(linkTypes) 
						+ " UPDATE e with {theTruth: false} IN edges";
		String query2 = "FOR doc IN Events FILTER doc.meta.id == '" + metaId 
						+ "' FOR v, e, p IN 1.." + levels + " OUTBOUND doc GRAPH '" 
						+ GRAPH_NAME + "' FILTER p.edges[*].theTruth All == false "
						+ "COLLECT res = v SORT res.meta.time DESC LIMIT " 
						+ limit + " RETURN res";
		String query3 = "FOR doc IN Events FILTER doc.meta.id == '" + metaId 
						+ "' FOR v, e, p IN 1.." + levels + " OUTBOUND doc GRAPH '" 
						+ GRAPH_NAME + "' FILTER " + createLinkTypes(linkTypes) 
						+ " UPDATE e with {theTruth: true} IN edges";
		try {	 
			if(arangoDB.db(dbName).collection(collectionName).documentExists(metaId)){
				  	ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query1, null, null,
					      BaseDocument.class);
					ArangoCursor<BaseDocument> cursor2 = arangoDB.db(dbName).query(query2, null, null,
						      BaseDocument.class);
						cursor2.forEachRemaining(aDocument -> {
							events.add(new JSONObject(aDocument.getProperties()));	  
					});
					ArangoCursor<BaseDocument> cursor3 = arangoDB.db(dbName).query(query3, null, null,
						      BaseDocument.class);
			}else{
				throw new ArangoDBException("Document is not in Database");
			}
		} catch (ArangoDBException e) {
			System.err.println("Failed to execute query. " + e.getMessage());
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
			performDownstreamSearch(eventId, linkTypes, visitedMap, limit-1, levels, downstreamEvents);
			
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
		//String query = "FOR doc IN " + collectionName + " FILTER doc.meta.id == '" + metaId + "' FOR v IN 1..2 OUTBOUND doc " + EDGE_COLLECTION_NAME + " RETURN v";
		String query1 = "FOR doc IN Events FILTER doc.meta.id == '" + eventId 
						+ "' FOR v, e, p IN 1.." + levels + " INBOUND doc GRAPH '" 
						+ GRAPH_NAME + "' FILTER " + createLinkTypes(linkTypes) 
						+ " UPDATE e with {theTruth: false} IN edges";
		String query2 = "FOR doc IN Events FILTER doc.meta.id == '" 
						+ eventId + "' FOR v, e, p IN 1.." + levels 
						+ " INBOUND doc GRAPH '" + GRAPH_NAME + "' FILTER "
						+ "p.edges[*].theTruth All == false COLLECT res = v"
						+ " SORT res.meta.time ASC LIMIT " + limit + " RETURN res";
		String query3 = "FOR doc IN Events FILTER doc.meta.id == '" + eventId 
						+ "' FOR v, e, p IN 1.." + levels + " INBOUND doc GRAPH '" 
						+ GRAPH_NAME + "' FILTER " + createLinkTypes(linkTypes) 
						+ " UPDATE e with {theTruth: true} IN edges";
		System.out.println(query);
		try {	 
			if(arangoDB.db(dbName).collection(collectionName).documentExists(eventId)){
				ArangoCursor<BaseDocument> cursor = arangoDB.db(dbName).query(query1, null, null,
				      BaseDocument.class);
				ArangoCursor<BaseDocument> cursor2 = arangoDB.db(dbName).query(query2, null, null,
					      BaseDocument.class);
					cursor2.forEachRemaining(aDocument -> {
						events.add(new JSONObject(aDocument.getProperties()));	  
					});
				ArangoCursor<BaseDocument> cursor3 = arangoDB.db(dbName).query(query3, null, null,
					      BaseDocument.class);
			}else{
				throw new ArangoDBException("Document is not in Database");
			}
		} catch (ArangoDBException e) {
			System.err.println("Failed to execute query. " + e.getMessage());
		}
		
		return false;
	}

	@Override
	public DataStoreResult getArtifactsByGroup(String groupId,
			FilterParameterList filterList, String comparator, int skip,
			int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataStoreResult getArtifactsByGroupAndArtifactId(String groupId,
			String artifactId, FilterParameterList filterList,
			String comparator, int skip, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getArtifactByGAV(String groupId, String artifactId,
			String version) {
		// TODO Auto-generated method stub
		return null;
	}

}
