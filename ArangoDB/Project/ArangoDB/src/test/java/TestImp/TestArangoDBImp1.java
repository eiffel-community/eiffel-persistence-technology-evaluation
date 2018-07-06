package TestImp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.multimodel.ArangoDB.ArangoDBDatabaseHelperV1;
import com.multimodel.ArangoDB.FilterParameterList;

//import java.lang.management.ManagementFactory.getThreadMXBean

public class TestArangoDBImp1 {

	public static TestArangoDBImp1 test = new TestArangoDBImp1();
	public static ArangoDBDatabaseHelperV1 con;
	
	//Variables and datatypes
	public static String DBMSName = "ArangoDB";
	public static String imp = "Imp1"; 
	public static String impFolder = "ArangoDB_IMP1";
	
	public static List<String> linkTypes = Arrays.asList("CAUSE", "ELEMENT", "CONTEXT", "COMPOSITION", "BASE", "FLOW_CONTEXT","ACTIVITY_EXECUTION",
			"PREVIOUS_ACTIVITY_EXECUTION", "PREVIOUS_VERSION", "ENVIRONMENT",
			"ARTIFACT", "SUBJECT", "CHANGE", "TEST_SUITE_EXECUTION",
			"TEST_CASE_EXECUTION","IUT","TERC","MODIFIED_ANNOUNCEMENT","SUB_CONFIDENCE_LEVEL",
			"REUSED_ARTIFACT","VERIFICATION_BASIS");
	
	public static FilterParameterList filterList = new FilterParameterList();
	public static FilterParameterList filterList2 = new FilterParameterList();
	public static FilterParameterList GAVFilterList = new FilterParameterList();
	public static int iterationsNumb = 10;
	public static int testNr = 0;
	public static List<Integer> testSizes = Arrays.asList(100, 200);//Arrays.asList(10000, 100000, 1000000, 2000000);
	public static List<String> metaIdList = new ArrayList<String>(); 
	public static List<String> metaTimeList = new ArrayList<String>(); 
	public static int timePos = 0;
	public static String mainMetaTime = "";
	public static String mainMetaTime1 = "";
	public static String mainMetaTime2 = "";
	
	public static int timeDivision = 1000;
	public static String timeFormatName = "Micro";
	
	public static String groupId = "com.mycompany.myproduct";
	public static String artifactId = "component-3";
	public static String gavVersion = "1.0.0";
	
	//Paths
	public static String logDocPathWin = "";
	public static String logDocPathMac = "/Users/Jakub1/Documents/Universitet/Exjobb/Imp/Project/eiffel-persistence-technology-evaluation/TestResults/" + impFolder + "/Log/testLog_TestNr_"+ testNr + ".txt";
	public static String logDocPath = logDocPathMac;
	public static String eventsFilePathWin = "C:/Users/ebinjak/Documents/Exjobb/DataSet/events.json";
	public static String eventsFilePathMac = "/Users/Jakub1/Documents/Universitet/Exjobb/Imp/json_example/events.json";
	public static String eventsFilePath = eventsFilePathMac;
	
	
	public static void main( String[] args ) throws Exception {
		System.out.println("Start");
		test.setUpImp1();
		test.setFilterParameters();
		test.setGAVFilterParameters();
		timePos = testSizes.get(0)/2;
	    	
    	Thread thread = new Thread(new Runnable(){
    		public void run(){
    			try {
    				for(int i = 0; i < testSizes.size(); i++){
		    			con.removeAllEvents();
		    	    	int amount = testSizes.get(i);
		    	    	System.out.println("Problem size : " + amount);
		    	    	
		    	    	test.testStore("1", amount);
						
		    	    	//test.testGetEvent("2", amount);
		    	    	
		    		    //mainMetaTime = metaTimeList.get(timePos);
		    		    long tempMainTime = (long) Double.parseDouble(metaTimeList.get(timePos));
		    		    mainMetaTime = String.valueOf(tempMainTime);
		    		    filterList2.addFilterParameter("meta_time", mainMetaTime, "!=");
						
		    		    //System.out.println(amount + " , " + mainMetaTime);
		    		    
		    		/*    test.testGetEvents0("3", amount);
		    	    	test.testGetEvents1("3", amount);
		    	    	test.testGetEvents2("3", amount);
		    	    	test.testGetEvents3("3", amount);
		    	    	test.testGetEvents4("3", amount);
		    	    	
		    	    	test.testGetArtifactsByGroup0("4", amount);
		    	    	test.testGetArtifactsByGroup1("4", amount);
		    	    	test.testGetArtifactsByGroup2("4", amount);
		    	    	test.testGetArtifactsByGroup3("4", amount);
		    	    	
		    	    	test.testGetArtifactsByGroupAndArtifactId0("5", amount);
		    	    	test.testGetArtifactsByGroupAndArtifactId1("5", amount);
		    	    	test.testGetArtifactsByGroupAndArtifactId2("5", amount);
		    	    	test.testGetArtifactsByGroupAndArtifactId3("5", amount);
		    	    	
		    	    	test.testGetArtifactByGAV("6", amount);*/
		    	    	
		    	    	test.testGetUpstreamEvents0("7", amount);
		    	    	test.testGetUpstreamEvents1("7", amount);
		    	    	test.testGetUpstreamEvents2("7", amount);
		    	    	test.testGetUpstreamEvents3("7", amount);
		    	    	
		    	    	test.resetVariables();
		    	    	con.removeAllEvents();
	    				
						}
    				System.out.println("Done");
    			} catch (Exception e) {
					// TODO Auto-generated catch block
    				e.printStackTrace();
    			}	
    		}
    	});
	    	
    	thread.start();
    	
	}
	    
	 /*   ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
	    System.out.println(mxBean.isThreadCpuTimeSupported());
	    Thread thr = new Thread();
	    System.out.println( mxBean.getThreadCpuTime(thr.getId()));*/
	    
	
	
	public void setUpImp1() throws Exception{
		String url = "bolt://localhost:7687";
        String user = "neo4j";
        String password = "Dekret66";
        
        
        try {
        	con = new ArangoDBDatabaseHelperV1();
        	logDone("0", "Connection", 0);
        }catch (Exception e){
        	//System.out.println("Failed to connect : " + e);
        	logError("0", "Failed to connect", 0, e);
        }
       
	}
	
	public void setFilterParameters() throws Exception{
		filterList.getFilterList().clear();
		filterList.addFilterParameter("meta_type", "EiffelCompositionDefinedEvent", "==");
        filterList.addFilterParameter("meta_version", "1.0.0", "==");
        filterList.addFilterParameter("data_name", "Composition 3", "==");
        filterList.addFilterParameter("data_version", "0", "==");
        filterList.addFilterParameter("meta_time", "1.490777046669E12", "==");
	}
	
	public void setGAVFilterParameters() throws Exception{
		GAVFilterList.getFilterList().clear();
		GAVFilterList.addFilterParameter("meta_type", "EiffelArtifactCreatedEvent", "==");
		GAVFilterList.addFilterParameter("meta_version", "1.0.0", "==");
		GAVFilterList.addFilterParameter("meta_time", "1.490777046672E12", "==");
	}
	
	public void resetVariables() throws Exception{
		metaIdList.clear();
		metaTimeList.clear();
		setFilterParameters();
		setGAVFilterParameters();
		filterList2.getFilterList().clear();
	}
	
	public void logDone(String caseNr, String functionName, int amount){
		try {
			PrintWriter pw;
			pw = new PrintWriter(new FileWriter(logDocPath, true));
			StringBuilder sb = new StringBuilder();
			sb.append("Case nr : " + caseNr + " -- Size : " + amount + " -- Function : " + functionName + " -- Done\n");
			pw.write(sb.toString());
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in logDone: " + e);
		}
		
	}
	
	public void logError(String caseNr, String functionName, int amount, Exception err){
		try {
			PrintWriter pw;
			pw = new PrintWriter(new FileWriter(logDocPath, true));
			StringBuilder sb = new StringBuilder();
			sb.append("Case nr : " + caseNr + " -- Size : " + amount + " -- Function : " + functionName + " -- Error : " + err + "\n");
			pw.write(sb.toString());
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in logError: " + e);
		}
	}
	
	public static List<JSONObject> readJsonStream(InputStream in) throws IOException{
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		List<JSONObject> messages = new ArrayList<JSONObject>();
		Gson gson = new Gson();
		reader.beginArray();
		while (reader.hasNext()) {
			JSONObject message = gson.fromJson(reader, JSONObject.class);
			metaIdList.add(((Map) message.get("meta")).get("id").toString());
			metaTimeList.add(((Map) message.get("meta")).get("time").toString());
			messages.add(message);
		}
		reader.endArray();
		reader.close();
		return messages;
		
	}
	
	public String createResFilePath(String folder, String testCaseNr, String functionName, int size){
		String resPathMac = "/Users/Jakub1/Documents/Universitet/Exjobb/Imp/Project/eiffel-persistence-technology-evaluation/TestResults/";
		String resPathWin = "";
		String res = "";
		if(size == 0){
			res = resPathMac + impFolder + "/" + folder + "/" + DBMSName + "_" + imp + "_TestNr_" + testNr + "_TestCase_" + testCaseNr + "_" + functionName + ".csv";
		}else{
			res = resPathMac + impFolder + "/" + folder + "/" + DBMSName + "_" + imp + "_TestNr_" + testNr + "_TestCase_" + testCaseNr + "_" + functionName + "_" + size + ".csv";
		}
		
		return res;
	}
	
	public boolean checkIfFileEmpty(String resFilePath) throws IOException{
		boolean notEmpty = false;
		File f = new File(resFilePath);
        if (f.exists()){ 
        	if(f.length() != 0){
	        	notEmpty = true;
        	}
		}
		return notEmpty;
	}
	
	/*public boolean checkIfFileEmpty(String resFilePath) throws IOException{
		boolean notEmpty = false;
		BufferedReader br = new BufferedReader(new FileReader(resFilePath));
        if (br.readLine() != null){
        	notEmpty = true;
		}
        br.close();
		return notEmpty;
	}*/
	
	
	public void storeIterResInFile(String resFilePath, String colOne, String colTwo, String colThree, String colFour, String timeFormat) throws IOException{
		boolean notEmpty = checkIfFileEmpty(resFilePath);
		PrintWriter pw = new PrintWriter(new FileWriter(resFilePath, true));
		StringBuilder sb = new StringBuilder();
		
		if(! notEmpty){
			sb.append("Iterations");
			sb.append(';');
			sb.append("Exec_Time_" + timeFormat);
			sb.append(';');
			sb.append("Amount_Rec_events");
			sb.append(';');
			sb.append("Aver_R_Time_per_event");
			sb.append('\n');
		}
		
		sb.append(colOne);
		sb.append(';');
		sb.append(colTwo);
		sb.append(';');
		sb.append(colThree);
		sb.append(';');
		sb.append(colFour);
		sb.append('\n');
		pw.write(sb.toString());
		pw.close();
	}
	
	public void storeAverResInFile(String resFilePath, String colOne, String colTwo, String colThree, String colFour, String timeFormat) throws IOException{
		boolean notEmpty = checkIfFileEmpty(resFilePath);
		PrintWriter pw = new PrintWriter(new FileWriter(resFilePath, true));
		StringBuilder sb = new StringBuilder();
		
		if(! notEmpty){
			sb.append("Problem_Size");
			sb.append(';');
			sb.append("Exec_Time_" + timeFormat);
			sb.append(';');
			sb.append("Amount_Rec_events");
			sb.append(';');
			sb.append("Aver_R_Time_per_event");
			sb.append('\n');
		}
		
		sb.append(colOne);
		sb.append(';');
		sb.append(colTwo);
		sb.append(';');
		sb.append(colThree);
		sb.append(';');
		sb.append(colFour);
		sb.append('\n');
		pw.write(sb.toString());
		pw.close();
	}
	
	public String doubleToCSVValue(double value){
		String res = Double.toString(value);
		res = res.replace(".",",");
		return res;
	}
	
	
	public void testStore(String caseNr, int amount) throws Exception{
		String functionName = "storeMany";
		//ArrayList<Integer> resTime = new ArrayList<Integer>();
		
		List<JSONObject> jsonArr = new ArrayList<JSONObject>();
	    InputStream infile = new FileInputStream(eventsFilePath);
	    jsonArr = readJsonStream(infile);
	    infile.close();
	    

	    
	    //System.out.println(jsonArr.get(88));
		try{
			
			String iterationsResWhole = createResFilePath("All_iterations/Whole", caseNr, functionName + "_Whole_", amount);
			String iterationsResQuery = createResFilePath("All_iterations/Connection", caseNr, functionName + "_Connection_" + "_per_Event", amount);
			String averageResWhole = createResFilePath("Average/Whole", caseNr, functionName+ "_Whole_", 0);
			String averageResQuery = createResFilePath("Average/Connection", caseNr, functionName + "_Connection_" + "_per_Event", 0);
			//long sumWhole = 0;
			double sumWhole = 0;
			double sumQuery = 0;
			
	    	for(int c = 0; c < iterationsNumb; c++) {
	    		long startTime = System.nanoTime();
	    		
	    		con.storeManyEvents(jsonArr, amount);
	    		
	    		long endTime = System.nanoTime();
	    		
	    		long elapsedTime = (endTime-startTime) / timeDivision;
	    		sumWhole += elapsedTime;
	    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Integer.toString(0),Integer.toString(0), timeFormatName);
	    		
	    		int size = con.getTimeRes().size();
	    		for(int i = 0; i < size; i++){
	    			long conElapsedTime = con.getTimeRes().get(i) / timeDivision;
	    			storeIterResInFile(iterationsResQuery, Integer.toString(i+1) , Long.toString(conElapsedTime), Integer.toString(0),Integer.toString(0), timeFormatName);
	    			sumQuery += (conElapsedTime);
	    		}
	    		
	    		storeIterResInFile(iterationsResQuery, "---" , "---", "---", "", "");
	    		storeIterResInFile(averageResQuery, Integer.toString(c+1) , doubleToCSVValue(sumQuery/size), Integer.toString(0),Integer.toString(0), timeFormatName);
	    		
	    		con.timeResClear();
	    		con.setStartTime(0);
	    		con.setEndTime(0);
	    		
	    		if(c < (iterationsNumb -1)){
	    			con.removeAllEvents();
	    		}
	    	}
	    	
	    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Integer.toString(0), Integer.toString(0), timeFormatName);
	    	
	    	logDone(caseNr, functionName, amount);
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
		jsonArr.clear();
		
	}
	
	public void testGetEvent(String caseNr, int amount) throws Exception{
		String functionName = "getEvent";
		
	    
		try{
			
			String iterationsResWhole = createResFilePath("All_iterations/Whole", caseNr, functionName + "_Whole_", amount);
			String iterationsResQuery = createResFilePath("All_iterations/Connection", caseNr, functionName + "_Connection_", amount);
			String averageResWhole = createResFilePath("Average/Whole", caseNr, functionName + "_Whole_", 0);
			String averageResQuery = createResFilePath("Average/Connection", caseNr, functionName + "_Connection_", 0);
			
			double sumWhole = 0;
			double sumQuery = 0;
			
			Random rand = new Random();
			String metaId;
			
	    	for(int c = 0; c < iterationsNumb; c++) {
	    		metaId = metaIdList.get(rand.nextInt(amount));
	    		//System.out.println(metaId);
	    		long startTime = System.nanoTime();
	    		
	    		con.getEvent(metaId);
	    		
	    		long endTime = System.nanoTime();
	    		
	    		long conElapsedTime = con.getElapsedTime() / timeDivision;
	    		sumQuery += (conElapsedTime);
	    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Integer.toString(1), Long.toString(conElapsedTime), timeFormatName);
	    		
	    		long elapsedTime = (endTime-startTime) / timeDivision;
	    		sumWhole += elapsedTime;
	    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Integer.toString(1), Long.toString(elapsedTime), timeFormatName);
	    			    		    		
	    		con.setElapsedTime(0);
	    	}
	    	
	    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Integer.toString(1), doubleToCSVValue(sumQuery/iterationsNumb), timeFormatName);
	    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Integer.toString(1), doubleToCSVValue(sumWhole/iterationsNumb), timeFormatName);
	    	
	    	
	    	logDone(caseNr, functionName, amount);
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	public void testGetEvents0(String caseNr, int amount) throws Exception{
		String functionName = "getEvents";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 1000;
		long count = 0;
		//String time1 = metaTimeList.get(timePos);
		String tempCaseNr = caseNr + "_" + 0;
		
		//tempFilterList.getFilterList().add(filterList.getParameterFromList(i-1));
		//tempFilterList.addFilterParameter("meta_time", time1, "!=");
		tempFilterList.addParameterToFilterList(filterList2.getParameterFromList(0));
		//System.out.println(tempFilterList.getParameterFromList(i-1).getValue());

		try{
		
			String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
			String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
			String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
			String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
			
			double sumWhole = 0;
			double sumQuery = 0;
			
			
			
	    	for(int c = 0; c < iterationsNumb; c++) {
	    		
	    		long startTime = System.nanoTime();
	    		
	    		count = con.getEvents(tempFilterList, "<", skip, limit, false).getCount();
	    		
	    		long endTime = System.nanoTime();
	    		
	    		long conElapsedTime = con.getElapsedTime() / timeDivision;
	    		sumQuery += (conElapsedTime);
	    		double conIPerEvent = 0;
	    		if(count != 0){
	    			conIPerEvent = conElapsedTime/count;
	    		}
	    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
	    		
	    		long elapsedTime = (endTime-startTime) / timeDivision;
	    		sumWhole += elapsedTime;
	    		double wIPerEvent = 0;
	    		if(count != 0){
	    			wIPerEvent =	elapsedTime / count;
	    		}
	    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
	    			    		    		
	    		con.setElapsedTime(0);
	    	}
	    	
	    	double conAPerEvent = 0;
	    	double wAPerEvent = 0;
	    	if(count != 0){
	    		conAPerEvent = (sumQuery/iterationsNumb)/count;
	    		wAPerEvent = (sumWhole/iterationsNumb)/count;
	    	}
	    	
	    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue(conAPerEvent), timeFormatName);
	    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue(wAPerEvent), timeFormatName);
	    	
	    	logDone(tempCaseNr, functionName, amount);
		
		}catch(Exception e){
			logError(tempCaseNr, functionName, amount, e);
		}
	
	}
	
	public void testGetEvents1(String caseNr, int amount) throws Exception{
		String functionName = "getEvents";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 1000;
		long count = 0;
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			tempFilterList.getFilterList().add(filterList.getParameterFromList(i-1));
			//System.out.println(tempFilterList.getParameterFromList(i-1).getValue());

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = con.getEvents(tempFilterList, "<", skip, limit, false).getCount();
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    			    		    		
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetEvents2(String caseNr, int amount) throws Exception{
		String functionName = "getEvents";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 10;
		tempFilterList.addParameterToFilterList(filterList2.getParameterFromList(0));
		long count = 0;
		
		for(int i = 6; i < 11; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = con.getEvents(tempFilterList, "<", skip, limit, false).getCount();
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
			
			limit = limit * 10;
		}
	}
	
	public void testGetEvents3(String caseNr, int amount) throws Exception{
		String functionName = "getEvents";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 100000;
		tempFilterList.addParameterToFilterList(filterList2.getParameterFromList(0));
		long count = 0;
		
		for(int i = 11; i < 16; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			if(i == 11){
				skip = 1;
			}else if(i == 12){
				skip = 5;
			}else if(i == 13){
				skip = 10;
			}else if(i == 14){
				skip = 20;
			}else if(i == 15){
				skip = 50;
			}
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = con.getEvents(tempFilterList, "<", skip, limit, false).getCount();
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetEvents4(String caseNr, int amount) throws Exception{
		String functionName = "getEvents";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 1000;
		int size1 = timePos;
		int size2 = size1/2 + size1;
		//String time1 = metaTimeList.get(size1);
		//String time2 = metaTimeList.get(size2);
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		//System.out.println(amount + " , " + time1);
		//System.out.println(amount + " , " + time2);
		long count = 0;
		
		for(int i = 16; i < 23; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			if(i == 16){
				tempFilterList.getFilterList().clear();
				tempFilterList.addFilterParameter("meta_time", time1, "==");
			}else if(i == 17){
				tempFilterList.getFilterList().clear();
				tempFilterList.addFilterParameter("meta_time", time1, ">");
			}else if(i == 18){
				tempFilterList.getFilterList().clear();
				tempFilterList.addFilterParameter("meta_time", time1, "<");
			}else if(i == 19){
				tempFilterList.getFilterList().clear();
				tempFilterList.addFilterParameter("meta_time", time1, ">=");
			}else if(i == 20){
				tempFilterList.getFilterList().clear();
				tempFilterList.addFilterParameter("meta_time", time1, "<=");
			}else if(i == 21){
				tempFilterList.getFilterList().clear();
				tempFilterList.addFilterParameter("meta_time", time1, "!=");
			}else if(i == 22){
				tempFilterList.getFilterList().clear();
				tempFilterList.addFilterParameter("meta_time", time1, ">");
				tempFilterList.addFilterParameter("meta_time", time2, "<");
			}
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = con.getEvents(tempFilterList, "<", skip, limit, false).getCount();
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetArtifactsByGroup0(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactsByGroup";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 1;
		long count = 0;
		String tempCaseNr = caseNr + "_" + 0;
		

		try{
		
			String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
			String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
			String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
			String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
			
			double sumWhole = 0;
			double sumQuery = 0;
			
			
			
	    	for(int c = 0; c < iterationsNumb; c++) {
	    		
	    		long startTime = System.nanoTime();
	    		
	    		count = con.getArtifactsByGroup(groupId, tempFilterList, "<", skip, limit).getCount();
	    		
	    		long endTime = System.nanoTime();
	    		
	    		long conElapsedTime = con.getElapsedTime() / timeDivision;
	    		sumQuery += (conElapsedTime);
	    		double conIPerEvent = conElapsedTime/count;
	    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
	    		
	    		long elapsedTime = (endTime-startTime) / timeDivision;
	    		sumWhole += elapsedTime;
	    		double wIPerEvent = elapsedTime / count;
	    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
	    			    		    		
	    		con.setElapsedTime(0);
	    	}
	    	
	    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
	    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
	    	
	    	logDone(tempCaseNr, functionName, amount);
		
		}catch(Exception e){
			logError(tempCaseNr, functionName, amount, e);
		}
	}
	
	public void testGetArtifactsByGroup1(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactsByGroup";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 1000;
		long count = 0;
		for(int i = 1; i < 5; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			if(i > 1){
				tempFilterList.getFilterList().add(GAVFilterList.getParameterFromList(i-2));
			}
			//System.out.println(tempFilterList.getParameterFromList(i-1).getValue());

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = con.getArtifactsByGroup(groupId, tempFilterList, "<", skip, limit).getCount();
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    			    		    		
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetArtifactsByGroup2(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactsByGroup";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 10;
		//tempFilterList.addParameterToFilterList(filterList2.getParameterFromList(0));
		long count = 0;
		
		for(int i = 5; i < 10; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = con.getArtifactsByGroup(groupId, tempFilterList, "<", skip, limit).getCount();
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
			
			limit = limit * 10;
		}
	}
	
	public void testGetArtifactsByGroup3(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactsByGroup";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 100000;
		//tempFilterList.addParameterToFilterList(filterList2.getParameterFromList(0));
		long count = 0;
		
		for(int i = 10; i < 15; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			if(i == 10){
				skip = 1;
			}else if(i == 11){
				skip = 5;
			}else if(i == 12){
				skip = 10;
			}else if(i == 13){
				skip = 20;
			}else if(i == 14){
				skip = 50;
			}
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = con.getArtifactsByGroup(groupId, tempFilterList, "<", skip, limit).getCount();
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetArtifactsByGroupAndArtifactId0(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactsByGroupAndArtifactId";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 1;
		long count = 0;
		String tempCaseNr = caseNr + "_" + 0;
		

		try{
		
			String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
			String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
			String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
			String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
			
			double sumWhole = 0;
			double sumQuery = 0;
			
			
			
	    	for(int c = 0; c < iterationsNumb; c++) {
	    		
	    		long startTime = System.nanoTime();
	    		
	    		count = con.getArtifactsByGroupAndArtifactId(groupId, artifactId, tempFilterList, "<", skip, limit).getCount();
	    		
	    		long endTime = System.nanoTime();
	    		
	    		long conElapsedTime = con.getElapsedTime() / timeDivision;
	    		sumQuery += (conElapsedTime);
	    		double conIPerEvent = conElapsedTime/count;
	    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
	    		
	    		long elapsedTime = (endTime-startTime) / timeDivision;
	    		sumWhole += elapsedTime;
	    		double wIPerEvent = elapsedTime / count;
	    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
	    			    		    		
	    		con.setElapsedTime(0);
	    	}
	    	
	    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
	    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
	    	
	    	logDone(tempCaseNr, functionName, amount);
		
		}catch(Exception e){
			logError(tempCaseNr, functionName, amount, e);
		}
	}
	
	public void testGetArtifactsByGroupAndArtifactId1(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactsByGroupAndArtifactId";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 1000;
		long count = 0;
		for(int i = 1; i < 5; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			if(i > 1){
				tempFilterList.getFilterList().add(GAVFilterList.getParameterFromList(i-2));
			}
			//System.out.println(tempFilterList.getParameterFromList(i-1).getValue());

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = con.getArtifactsByGroupAndArtifactId(groupId, artifactId, tempFilterList, "<", skip, limit).getCount();
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    			    		    		
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetArtifactsByGroupAndArtifactId2(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactsByGroupAndArtifactId";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 10;
		//tempFilterList.addParameterToFilterList(filterList2.getParameterFromList(0));
		long count = 0;
		
		for(int i = 5; i < 10; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = con.getArtifactsByGroupAndArtifactId(groupId, artifactId, tempFilterList, "<", skip, limit).getCount();
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
			
			limit = limit * 10;
		}
	}
	
	public void testGetArtifactsByGroupAndArtifactId3(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactsByGroupAndArtifactId";
		FilterParameterList tempFilterList = new FilterParameterList();
		int skip = 0;
		int limit = 100000;
		//tempFilterList.addParameterToFilterList(filterList2.getParameterFromList(0));
		long count = 0;
		
		for(int i = 10; i < 15; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			if(i == 10){
				skip = 1;
			}else if(i == 11){
				skip = 5;
			}else if(i == 12){
				skip = 10;
			}else if(i == 13){
				skip = 20;
			}else if(i == 14){
				skip = 50;
			}
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = con.getArtifactsByGroupAndArtifactId(groupId, artifactId, tempFilterList, "<", skip, limit).getCount();
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetArtifactByGAV(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactByGAV";
		long count = 1;
		String tempGId = "";
		String tempAId = "";
		String tempVersion = "";
		
		for(int i = 1; i < 9; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				switch (i) {
					case 1:
						tempGId = "com.mycompany.myproduct";
						tempAId = "component-1";
						tempVersion = "1.0.0";
						break;
					case 2:
						tempGId = "com.mycompany.myproduct";
						tempAId = "component-2";
						tempVersion = "1.0.0";
						break;
					case 3:
						tempGId = "com.mycompany.myproduct";
						tempAId = "component-1";
						tempVersion = "1.1.0";
						break;
					case 4:
						tempGId = "com.mycompany.myproduct";
						tempAId = "component-2";
						tempVersion = "1.1.0";
						break;
					case 5:
						tempGId = "com.othercompany.library";
						tempAId = "third-party-library";
						tempVersion = "3.2.4";
						break;
					case 6:
						tempGId = "com.mycompany.myproduct";
						tempAId = "component-1";
						tempVersion = "2.0.0";
						break;
					case 7:
						tempGId = "com.mycompany.myproduct";
						tempAId = "component-3";
						tempVersion = "1.0.0";
						break;
					case 8:
						tempGId = "com.mycompany.myproduct";
						tempAId = "component-3";
						tempVersion = "1.1.0";
						break;
					default :
						break;
				}
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		con.getArtifactByGAV(tempGId, tempAId, tempVersion);
		    		
		    		long endTime = System.nanoTime();
		    		
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetUpstreamEvents0(String caseNr, int amount) throws Exception{
		String functionName = "getUpstreamEvents";
		long count = 0;
		int levels = 100;
		int limit = 5000;
		String metaId = metaIdList.get(99);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		String tempCaseNr = caseNr + "_" + 0;

		try{
		
			String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
			String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
			String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
			String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
			
			double sumWhole = 0;
			double sumQuery = 0;
			
	    	for(int c = 0; c < iterationsNumb; c++) {
	    		
	    		long startTime = System.nanoTime();
	    		
	    		res = con.getUpstreamEvents(metaId, linkTypes, visitedMap, limit, levels);
	    		
	    		long endTime = System.nanoTime();
	    		
	    		count = res.size();
	    		long conElapsedTime = con.getElapsedTime() / timeDivision;
	    		sumQuery += (conElapsedTime);
	    		double conIPerEvent = conElapsedTime/count;
	    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
	    		
	    		long elapsedTime = (endTime-startTime) / timeDivision;
	    		sumWhole += elapsedTime;
	    		double wIPerEvent = elapsedTime / count;
	    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
	    		
	    		visitedMap.clear();
	    		con.setElapsedTime(0);
	    	}
	    	
	    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
	    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
	    	
	    	logDone(tempCaseNr, functionName, amount);
		
		}catch(Exception e){
			logError(tempCaseNr, functionName, amount, e);
		}
	}
	
	public void testGetUpstreamEvents1(String caseNr, int amount) throws Exception{
		String functionName = "getUpstreamEvents";
		List<String> tempLinkTypes = new ArrayList<String>();
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(99);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			System.out.println(tempCaseNr);
			tempLinkTypes.add(linkTypes.get(i-1));

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		res = con.getUpstreamEvents(metaId, tempLinkTypes, visitedMap, limit, levels); 
		    				
		    		long endTime = System.nanoTime();
		    		
		    		count = res.size();
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		visitedMap.clear();    		    		
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetUpstreamEvents2(String caseNr, int amount) throws Exception{
		String functionName = "getUpstreamEvents";
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(99);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		
		for(int i = 6; i < 11; i++){
			String tempCaseNr = caseNr + "_" + i;
			System.out.println(tempCaseNr);
			switch (i) {
				case 6:
					levels = 10;
					break;
				case 7:
					levels = 50;
					break;
				case 8:
					levels = 100;
					break;
				case 9:
					levels = 1000;
					break;
				case 10:
					levels = 10000;
					break;
				default :
					break;
			}
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		res = con.getUpstreamEvents(metaId, linkTypes, visitedMap, limit, levels);
		    		
		    		long endTime = System.nanoTime();
		    		
		    		count = res.size();
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		visitedMap.clear();
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetUpstreamEvents3(String caseNr, int amount) throws Exception{
		String functionName = "getUpstreamEvents";
		int levels = 10;
		int limit = 10;
		long count = 0;
		String metaId = metaIdList.get(99);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		
		for(int i = 11; i < 16; i++){
			String tempCaseNr = caseNr + "_" + i;
			System.out.println(tempCaseNr);
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		res = con.getUpstreamEvents(metaId, linkTypes, visitedMap, limit, levels);
		    		
		    		long endTime = System.nanoTime();
		    		
		    		count = res.size();
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		visitedMap.clear();
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
			
			limit = limit * 10;
		}
	}
	
	public void testGetDownstreamEvents0(String caseNr, int amount) throws Exception{
		String functionName = "getDownstreamEvents";
		long count = 0;
		int levels = 100;
		int limit = 5000;
		String metaId = metaIdList.get(0);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		String tempCaseNr = caseNr + "_" + 0;

		try{
		
			String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
			String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
			String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
			String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
			
			double sumWhole = 0;
			double sumQuery = 0;
			
	    	for(int c = 0; c < iterationsNumb; c++) {
	    		
	    		long startTime = System.nanoTime();
	    		
	    		res =con.getDownstreamEvents(metaId, linkTypes, visitedMap, limit, levels);
	    		
	    		long endTime = System.nanoTime();
	    		
	    		count = res.size();
	    		long conElapsedTime = con.getElapsedTime() / timeDivision;
	    		sumQuery += (conElapsedTime);
	    		double conIPerEvent = conElapsedTime/count;
	    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
	    		
	    		long elapsedTime = (endTime-startTime) / timeDivision;
	    		sumWhole += elapsedTime;
	    		double wIPerEvent = elapsedTime / count;
	    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
	    		
	    		visitedMap.clear();
	    		con.setElapsedTime(0);
	    	}
	    	
	    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
	    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
	    	
	    	logDone(tempCaseNr, functionName, amount);
		
		}catch(Exception e){
			logError(tempCaseNr, functionName, amount, e);
		}
	}
	
	public void testGetDownstreamEvents1(String caseNr, int amount) throws Exception{
		String functionName = "getDownstreamEvents";
		List<String> tempLinkTypes = new ArrayList<String>();
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(0);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			tempLinkTypes.add(linkTypes.get(i-1));

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		res = con.getDownstreamEvents(metaId, tempLinkTypes, visitedMap, limit, levels); 
		    				
		    		long endTime = System.nanoTime();
		    		
		    		count = res.size();
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		visitedMap.clear();	    		    		
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetDownstreamEvents2(String caseNr, int amount) throws Exception{
		String functionName = "getDownstreamEvents";
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(0);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		
		for(int i = 6; i < 11; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			switch (i) {
				case 6:
					levels = 10;
					break;
				case 7:
					levels = 50;
					break;
				case 8:
					levels = 100;
					break;
				case 9:
					levels = 1000;
					break;
				case 10:
					levels = 10000;
					break;
				default :
					break;
			}
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		res = con.getDownstreamEvents(metaId, linkTypes, visitedMap, limit, levels);
		    		
		    		long endTime = System.nanoTime();
		    		
		    		count = res.size();
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		visitedMap.clear();
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
	public void testGetDownstreamEvents3(String caseNr, int amount) throws Exception{
		String functionName = "getDownstreamEvents";
		int levels = 10;
		int limit = 10;
		long count = 0;
		String metaId = metaIdList.get(0);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		
		for(int i = 11; i < 16; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
				
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		res = con.getDownstreamEvents(metaId, linkTypes, visitedMap, limit, levels);
		    		
		    		long endTime = System.nanoTime();
		    		
		    		count = res.size();
		    		long conElapsedTime = con.getElapsedTime() / timeDivision;
		    		sumQuery += (conElapsedTime);
		    		double conIPerEvent = conElapsedTime/count;
		    		storeIterResInFile(iterationsResQuery, Integer.toString(c+1) , Long.toString(conElapsedTime), Long.toString(count), doubleToCSVValue(conIPerEvent), timeFormatName);
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		visitedMap.clear();
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResQuery, Integer.toString(amount) , doubleToCSVValue(sumQuery/iterationsNumb), Long.toString(count), doubleToCSVValue((sumQuery/iterationsNumb)/count), timeFormatName);
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
			
			limit = limit * 10;
		}
	}
	
	public int getEventGetUpstreamEvents(String metaId, List<String> linkTypesList, int limit, int levels, ConcurrentMap<String, String> visitedMap){
		List<Object> res = new ArrayList<>();
		con.getEvent(metaId);
		res = con.getUpstreamEvents(metaId, linkTypesList, visitedMap, limit, levels);
		return res.size();	
	}
	
	
	public void testCombinations1(String caseNr, int amount) throws Exception{
		String functionName = "getUpstreamEvents";
		List<String> tempLinkTypes = new ArrayList<String>();
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(99);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			tempLinkTypes.add(linkTypes.get(i-1));

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				double sumQuery = 0;
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = getEventGetUpstreamEvents(metaId, tempLinkTypes, limit, levels, visitedMap);
		    				
		    		long endTime = System.nanoTime();
		    		
		    		long elapsedTime = (endTime-startTime) / timeDivision;
		    		sumWhole += elapsedTime;
		    		double wIPerEvent = elapsedTime / count;
		    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Long.toString(count), doubleToCSVValue(wIPerEvent), timeFormatName);
		    			
		    		visitedMap.clear();	    		    		
		    		con.setElapsedTime(0);
		    	}
		    	
		    	storeAverResInFile(averageResWhole, Integer.toString(amount) , doubleToCSVValue(sumWhole/iterationsNumb), Long.toString(count), doubleToCSVValue((sumWhole/iterationsNumb)/count), timeFormatName);
		    	
		    	logDone(tempCaseNr, functionName, amount);
			
			}catch(Exception e){
				logError(tempCaseNr, functionName, amount, e);
			}
		}
	}
	
}
