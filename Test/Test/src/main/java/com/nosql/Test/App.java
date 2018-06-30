package com.nosql.Test;

/*import com.ericsson.eiffel.EventRepositoryApp;
import com.ericsson.eiffel.er.datastore.exception.DataStoreException;
import com.ericsson.eiffel.er.datastore.exception.EventNotFoundException;
import com.ericsson.eiffel.mongo.primary.repositories.MongoDataStore;*/
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.graphmodel.neo4j.Neo4jDatabaseHelperV1;
import com.graphmodel.neo4j.Neo4jDatabaseHelperV2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
/**
 * 
 * Hello world!
 *
 */


public class App 
{
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
	
	public static void testMongo() throws Exception{

        
	    String filePath2 = "C:/Users/ebinjak/Documents/Exjobb/DataSet/events_test.json";
	    List<JSONObject> jsonArr = new ArrayList<JSONObject>();
        InputStream infile = new FileInputStream(filePath2);
        jsonArr = readJsonStream(infile);
	   // MongoDataStore temp = new MongoDataStore();
	    //temp.start();
	    for(int i = 0; i < 13; i++){
    		try {
				JSONObject json = (JSONObject) new JSONParser().parse(jsonArr.get(i).toString());
				//temp.store(json.toJSONString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
	   // System.out.println(temp.getEvent("3ae8debd-9cd4-4895-8fc3-2aaa45c5c415"));
	    
	}
	
	
	
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Hello World!" );
        String url = "bolt://localhost:7687";
        String user = "neo4j";
        String password = "Dekret66";
        String filePath1 = "C:/Users/ebinjak/Documents/Exjobb/DataSet/events.json";
       // Neo4jDatabaseHelperV1 neo4jV1 = new Neo4jDatabaseHelperV1(url, user, password);
       // Neo4jDatabaseHelperV2 neo4jV2 = new Neo4jDatabaseHelperV2(url, user, password);
        testMongo();
    }
}
