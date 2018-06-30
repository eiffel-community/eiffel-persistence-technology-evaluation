package com.multimodel.ArangoDB;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


/**
 * Hello world!
 *
 */
public class App 
{
	public JSONArray readJSONFromFile(String path){
		 JSONParser parser = new JSONParser();
		 JSONArray jsonArr = new JSONArray();  
		 
	        try {
	 
	            Object obj = parser.parse(new InputStreamReader(new FileInputStream(path), "utf8"));
	            jsonArr = (JSONArray) obj;
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return jsonArr;
	}
	
	public static List<JSONObject> readJsonStream(InputStream in) throws IOException{
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		List<JSONObject> messages = new ArrayList<JSONObject>();
		Gson gson = new Gson();
		reader.beginArray();
		while (reader.hasNext()) {
			JSONObject message = gson.fromJson(reader, JSONObject.class);
			messages.add(message);
		}
		reader.endArray();
		reader.close();
		return messages;
		
	}
	
	public static void testV1(ArangoDBDatabaseHelperV1 dbV1, List<JSONObject> jsonArr, FilterParameterList filterList, int amount, PrintStream p) throws Exception{
        
		dbV1.arangoDBSetUp();
		
		// Storing test part
        p.append ("--------------------- V1 --------------------- \r\n");
        p.append ("\r\n");
    	// Storing test part
        p.append ("Storing test part \r\n");
    	Instant start = Instant.now();
    	dbV1.storeManyEvents(jsonArr, amount);
    	Instant end = Instant.now();
        System.out.println("ArangoDB V1: Duration of storing " + amount + " event(s): " + Duration.between(start, end));
        p.append ("ArangoDB V1: Duration of storing " + amount + " event(s): " + Duration.between(start, end) + "\r\n");
        p.append ("\r\n");
		
		// Get event by id test part
        p.append ("Get event by id test part \r\n");
    	//JSONObject test1 = (JSONObject) jsonArr.get(2);
        JSONObject test1 = (JSONObject) new JSONParser().parse(jsonArr.get(40).toString());
    	Instant start2 = Instant.now();
    	JSONObject test2 = dbV1.getEvent(((JSONObject) test1.get("meta")).get("id").toString());
    	Instant end2 = Instant.now();
        System.out.println("ArangoDB V1: Duration of geting " + "1" + " event(s): " + Duration.between(start2, end2));
        p.append ("ArangoDB V1: Duration of geting " + "1" + " event(s): " + Duration.between(start2, end2) + "\r\n");
        p.append ("\r\n");
        
    	System.out.println();
    	System.out.println(test1);
    	p.append (test1 + "\r\n");
    	p.append("\r\n");
    	System.out.println(test2);
    	p.append (test2 + "\r\n"); 
    	p.append("\r\n");
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Get events test part
    	p.append ("Get events test part \r\n");
    	DataStoreResult result = new DataStoreResult();
    	int skip = 2;
    	Instant start3 = Instant.now();
    	result = dbV1.getEvents(filterList, "<", skip, 5, false);
    	Instant end3 = Instant.now();
    	System.out.println("ArangoDB V1: Duration of getEvents with " + amount + " events in database: " + Duration.between(start3, end3));
    	p.append ("ArangoDB V1: Duration of getEvents with " + amount + " events in database: " + Duration.between(start3, end3) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	result.printResult(p);
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// GAV test part
    	p.append ("GAV test part \r\n");
    	String groupId = "com.mycompany.myproduct";
    	String artifactId = "component-3";
    	String version = "1.0.0";
    	int gavLimit = 30;
    	
    	// Get events by groupId
    	p.append ("Get events by groupId \r\n");
    	Instant start4 = Instant.now();
    	result = dbV1.getArtifactsByGroup(groupId, filterList, "<", skip, gavLimit);
    	Instant end4 = Instant.now();
    	System.out.println("ArangoDB V1: Duration of getArtifactsByGroup with " + amount + " events in database: " + Duration.between(start4, end4));
    	p.append ("ArangoDB V1: Duration of getArtifactsByGroup with " + amount + " events in database: " + Duration.between(start4, end4) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	result.printResult(p);
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Get events by groupId and artifactId
    	p.append ("Get events by groupId and artifactId \r\n");
    	Instant start5 = Instant.now();
    	result = dbV1.getArtifactsByGroupAndArtifactId(groupId, artifactId, filterList, "<", skip, gavLimit);
    	Instant end5 = Instant.now();
    	System.out.println("ArangoDB V1: Duration of getArtifactsByGroupAndArtifactId with " + amount + " events in database: " + Duration.between(start5, end5));
    	p.append ("ArangoDB V1: Duration of getArtifactsByGroupAndArtifactId with " + amount + " events in database: " + Duration.between(start5, end5) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	result.printResult(p);
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Get events by groupId, artifactId and version
    	p.append ("Get events by groupId, artifactId and version \r\n");
    	Instant start6 = Instant.now();
    	JSONObject resGAV = dbV1.getArtifactByGAV(groupId, artifactId, version);
    	Instant end6 = Instant.now();
    	System.out.println("ArangoDB V1: Duration of getArtifactByGAV with " + amount + " events in database: " + Duration.between(start6, end6));
    	p.append ("ArangoDB V1: Duration of getArtifactsByGAV with " + amount + " events in database: " + Duration.between(start6, end6) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	System.out.println(resGAV);
    	p.append (resGAV + "\r\n");
    	p.append("\r\n");
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Get upstream events
    	p.append ("Get upstream events \r\n");
      	List<Object> result2 = new ArrayList<>();
     	String eventId = ((JSONObject) test1.get("meta")).get("id").toString();
      	//String eventId = "2decd614-860d-4be0-bff9-70c8c80283c6";
     	List<String> linkTypes = new ArrayList<>();
     	linkTypes.add("CAUSE");
     	linkTypes.add("ELEMENT");
     	ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
     	int limit = 7;
     	int levels = 4;
     	Instant start7 = Instant.now();
    	result2 = dbV1.getUpstreamEvents( eventId, linkTypes, visitedMap,  limit,  levels);
    	Instant end7 = Instant.now();
    	System.out.println("ArangoDB V1: Duration of getUpstreamEvents with " + amount + " events in database: " + Duration.between(start7, end7));
    	p.append ("ArangoDB V1: Duration of getUpstreamEvents with " + amount + " events in database: " + Duration.between(start7, end7) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	if(! (result2 == null)){
        	for(int el = 0; el<result2.size(); el++){
        		System.out.println(result2.get(el));
        		p.append (el +  ": " + result2.get(el) + "\r\n");
        		p.append ("\r\n");
        	}
    	}
    	p.append("\r\n");
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Get downstream events
    	p.append ("Get downstream events \r\n");
    	List<Object> result3 = new ArrayList<>();
    	eventId = "d0bb40e6-6361-4b92-8abc-c21087f43190";
       	Instant start8 = Instant.now();
    	result3 = dbV1.getDownstreamEvents( eventId, linkTypes, visitedMap,  limit,  levels);
    	Instant end8 = Instant.now();
    	System.out.println("ArangoDB V1: Duration of getDownstreamEvents with " + amount + " events in database: " + Duration.between(start8, end8));
    	p.append ("ArangoDB V1: Duration of getDownstreamEvents with " + amount + " events in database: " + Duration.between(start8, end8) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	if(! (result3 == null)){
        	for(int el = 0; el<result3.size(); el++){
        		System.out.println(result3.get(el));
        		p.append (el +  ": " + result3.get(el) + "\r\n");
        		p.append ("\r\n");
        	}
    	}
    	p.append("\r\n");
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Remove all nodes part
    	dbV1.removeAllEvents();
    	p.append ("Removed all events \r\n");
    	p.append ("\r\n");
    	p.append ("------------------------------------------------------------------ \r\n");
    	p.append ("\r\n");
    	
	}
	
	public static void testV2(ArangoDBDatabaseHelperV2 dbV2, List<JSONObject> jsonArr, FilterParameterList filterList, int amount, PrintStream p) throws Exception{
		dbV2.arangoDBGraphSetUp();
		
		// Storing test part
        p.append ("--------------------- V2 --------------------- \r\n");
        p.append ("\r\n");
    	// Storing test part
        p.append ("Storing test part \r\n");
    	Instant start = Instant.now();
    	dbV2.storeManyEvents(jsonArr, amount);
    	Instant end = Instant.now();
        System.out.println("ArangoDB V2: Duration of storing " + amount + " event(s): " + Duration.between(start, end));
        p.append ("ArangoDB V2: Duration of storing " + amount + " event(s): " + Duration.between(start, end) + "\r\n");
        p.append ("\r\n");
		
		// Get event by id test part
        p.append ("Get event by id test part \r\n");
    	//JSONObject test1 = (JSONObject) jsonArr.get(2);
        JSONObject test1 = (JSONObject) new JSONParser().parse(jsonArr.get(40).toString());
    	Instant start2 = Instant.now();
    	JSONObject test2 = dbV2.getEvent(((JSONObject) test1.get("meta")).get("id").toString());
    	Instant end2 = Instant.now();
        System.out.println("ArangoDB V2: Duration of geting " + "1" + " event(s): " + Duration.between(start2, end2));
        p.append ("ArangoDB V2: Duration of geting " + "1" + " event(s): " + Duration.between(start2, end2) + "\r\n");
        p.append ("\r\n");
        
    	System.out.println();
    	System.out.println(test1);
    	p.append (test1 + "\r\n");
    	p.append("\r\n");
    	System.out.println(test2);
    	p.append (test2 + "\r\n"); 
    	p.append("\r\n");
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Get events test part
    	p.append ("Get events test part \r\n");
    	DataStoreResult result = new DataStoreResult();
    	int skip = 2;
    	Instant start3 = Instant.now();
    	result = dbV2.getEvents(filterList, "<", skip, 5, false);
    	Instant end3 = Instant.now();
    	System.out.println("ArangoDB V2: Duration of getEvents with " + amount + " events in database: " + Duration.between(start3, end3));
    	p.append ("ArangoDB V2: Duration of getEvents with " + amount + " events in database: " + Duration.between(start3, end3) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	result.printResult(p);
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// GAV test part
    	p.append ("GAV test part \r\n");
    	String groupId = "com.mycompany.myproduct";
    	String artifactId = "component-3";
    	String version = "1.0.0";
    	int gavLimit = 30;
    	
    	// Get events by groupId
    	p.append ("Get events by groupId \r\n");
    	Instant start4 = Instant.now();
    	result = dbV2.getArtifactsByGroup(groupId, filterList, "<", skip, gavLimit);
    	Instant end4 = Instant.now();
    	System.out.println("ArangoDB V2: Duration of getArtifactsByGroup with " + amount + " events in database: " + Duration.between(start4, end4));
    	p.append ("ArangoDB V2: Duration of getArtifactsByGroup with " + amount + " events in database: " + Duration.between(start4, end4) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	result.printResult(p);
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Get events by groupId and artifactId
    	p.append ("Get events by groupId and artifactId \r\n");
    	Instant start5 = Instant.now();
    	result = dbV2.getArtifactsByGroupAndArtifactId(groupId, artifactId, filterList, "<", skip, gavLimit);
    	Instant end5 = Instant.now();
    	System.out.println("ArangoDB V2: Duration of getArtifactsByGroupAndArtifactId with " + amount + " events in database: " + Duration.between(start5, end5));
    	p.append ("ArangoDB V2: Duration of getArtifactsByGroupAndArtifactId with " + amount + " events in database: " + Duration.between(start5, end5) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	result.printResult(p);
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Get events by groupId, artifactId and version
    	p.append ("Get events by groupId, artifactId and version \r\n");
    	Instant start6 = Instant.now();
    	JSONObject resGAV = dbV2.getArtifactByGAV(groupId, artifactId, version);
    	Instant end6 = Instant.now();
    	System.out.println("ArangoDB V2: Duration of getArtifactByGAV with " + amount + " events in database: " + Duration.between(start6, end6));
    	p.append ("ArangoDB V2: Duration of getArtifactsByGAV with " + amount + " events in database: " + Duration.between(start6, end6) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	System.out.println(resGAV);
    	p.append (resGAV + "\r\n");
    	p.append("\r\n");
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Get upstream events
    	p.append ("Get upstream events \r\n");
      	List<Object> result2 = new ArrayList<>();
     	String eventId = ((JSONObject) test1.get("meta")).get("id").toString();
      	//String eventId = "2decd614-860d-4be0-bff9-70c8c80283c6";
     	List<String> linkTypes = new ArrayList<>();
     	linkTypes.add("CAUSE");
     	linkTypes.add("ELEMENT");
     	ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
     	int limit = 7;
     	int levels = 4;
     	Instant start7 = Instant.now();
    	result2 = dbV2.getUpstreamEvents( eventId, linkTypes, visitedMap,  limit,  levels);
    	Instant end7 = Instant.now();
    	System.out.println("ArangoDB V2: Duration of getUpstreamEvents with " + amount + " events in database: " + Duration.between(start7, end7));
    	p.append ("ArangoDB V2: Duration of getUpstreamEvents with " + amount + " events in database: " + Duration.between(start7, end7) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	if(! (result2 == null)){
        	for(int el = 0; el<result2.size(); el++){
        		System.out.println(result2.get(el));
        		p.append (el +  ": " + result2.get(el) + "\r\n");
        		p.append ("\r\n");
        	}
    	}
    	p.append("\r\n");
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Get downstream events
    	p.append ("Get downstream events \r\n");
    	List<Object> result3 = new ArrayList<>();
    	eventId = "d0bb40e6-6361-4b92-8abc-c21087f43190";
       	Instant start8 = Instant.now();
    	result3 = dbV2.getDownstreamEvents( eventId, linkTypes, visitedMap,  limit,  levels);
    	Instant end8 = Instant.now();
    	System.out.println("ArangoDB V2: Duration of getDownstreamEvents with " + amount + " events in database: " + Duration.between(start8, end8));
    	p.append ("ArangoDB V2: Duration of getDownstreamEvents with " + amount + " events in database: " + Duration.between(start8, end8) + "\r\n");
    	p.append ("\r\n");
    	System.out.println();
    	if(! (result3 == null)){
        	for(int el = 0; el<result3.size(); el++){
        		System.out.println(result3.get(el));
        		p.append (el +  ": " + result3.get(el) + "\r\n");
        		p.append ("\r\n");
        	}
    	}
    	p.append("\r\n");
    	System.out.println();
    	System.out.println("---------------------");
    	System.out.println();
    	
    	// Remove all nodes part
    	dbV2.removeAllEvents();
    	p.append ("Removed all events \r\n");
    	p.append ("\r\n");
    	p.append ("------------------------------------------------------------------ \r\n");
    	p.append ("\r\n");
	}
	
    public static void main( String[] args ) throws Exception
    {
    	System.out.println("Start");
    	
        ArangoDBDatabaseHelperV1 dbV1 = new ArangoDBDatabaseHelperV1();
        ArangoDBDatabaseHelperV2 dbV2 = new ArangoDBDatabaseHelperV2();
        
        String filePath1 = "C:/Users/ebinjak/Documents/Exjobb/DataSet/events.json";
        String filePath2 = "C:/Users/ebinjak/Documents/Exjobb/DataSet/events_test.json";
        String filePathMac = "/Users/Jakub1/Documents/Universitet/Exjobb/Imp/json_example/events.json";
        
        //JSONArray jsonArr = new JSONArray();
        int testNr = 1;
        int amount = 100;
        App temp = new App();
        //jsonArr = temp.readJSONFromFile(filePath1);
        List<JSONObject> jsonArr = new ArrayList<JSONObject>();
        InputStream infile = new FileInputStream(filePath1);
        jsonArr = readJsonStream(infile);
       
        
        String file = "C:/Users/ebinjak/Documents/Exjobb/TestsResults/ArangoDB_temp_results_with_" + amount + "_events_testnr_" + testNr + ".txt";
        String fileMac = "/Users/Jakub1/Documents/Universitet/Exjobb/Test_Results/ArangoDB_temp_results_with_" + amount + "_events_testnr_" + testNr + ".txt";

        FileOutputStream out;
        PrintStream p;
        out = new FileOutputStream(file);
        p = new PrintStream( out );
        
       // dbV1.arangoDBSetUp();
       // dbV1.storeManyEvents(jsonArr, 100);
        
        int skip = 1;
        int limit = 5;
        boolean lazy = false;
        FilterParameterList filterList = new FilterParameterList();
        filterList.addFilterParameter("meta_type", "EiffelArtifactCreatedEvent", "==");
        filterList.addFilterParameter("meta_version", "1.0.0", "==");
        filterList.addFilterParameter("meta_version", "2.0.0", "<");
        

        
        int n = 1;
        Scanner reader = new Scanner(System.in);
        while(n == 1 || n == 2 || n == 3){
        	
        	System.out.println("Test version 1 : 1");
        	System.out.println("Test version 2 : 2");
        	System.out.println("Test both versions : 3");
        	System.out.println("Remove database : 4");
	        System.out.println("Enter a number of a test: ");
	        n = reader.nextInt(); 
	        switch (n) {
	        	case 1:
	        		testV1(dbV1, jsonArr, filterList, amount, p);
	        		break;
	        	case 2:
	        		testV2(dbV2, jsonArr, filterList, amount, p);
	        		break;
	        	case 3:
	        		testV1(dbV1, jsonArr, filterList, amount, p);
	        		testV2(dbV2, jsonArr, filterList, amount, p);
	        		break;
	        	case 4:
	        		dbV1.removeDatabase();
	        	default:
	        		break;
	        }
	        
        }
        reader.close();
        System.out.println("Test ended");
        p.append("\r\n");
        p.append("Test ended \r\n");
        p.append("\r\n");
    	p.close();
    }
}
