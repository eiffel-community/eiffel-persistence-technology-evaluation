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
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CyclicBarrier;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.multimodel.ArangoDB.ArangoDBDatabaseHelperV2;
import com.multimodel.ArangoDB.DataStoreResult;
import com.multimodel.ArangoDB.FilterParameterList;

//import java.lang.management.ManagementFactory.getThreadMXBean

public class TestArangoDBImp2 {

	public static TestArangoDBImp2 test = new TestArangoDBImp2();
	public static ArangoDBDatabaseHelperV2 con;
	
	//Variables and datatypes
	public static String DBMSName = "ArangoDB";
	public static String imp = "Imp2"; 
	public static String impFolder = "ArangoDB_IMP2";
	
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
	public static List<Integer> testSizes = Arrays.asList(10000);//Arrays.asList(10000, 100000, 1000000, 2000000);
	public static List<String> metaIdList = new ArrayList<String>(); 
	public static List<String> metaTimeList = new ArrayList<String>(); 
	public static int timePos = 0;
	public static String mainMetaTime = "";
	public static String mainMetaTime1 = "";
	public static String mainMetaTime2 = "";
	
	public static int timeDivision = 1000;
	public static String timeFormatName = "Micro";
	
	public static String groupId = "com.mycompany.myproduct";
	public static String artifactId = "component-1";
	public static String gavVersion = "1.1.0";
	
	public static int downstreamEventNr = 0;
	public static int upstreamEventNr = 99;
	
	//Paths
	public static String logDocPathWin = "C:/Users/ebinjak/Documents/Exjobb/eiffel-persistence-technology-evaluation/TestResults/" + impFolder + "/Log/testLog_TestNr_"+ testNr + ".txt";
	public static String logDocPathMac = "/Users/Jakub1/Documents/Universitet/Exjobb/Imp/Project/eiffel-persistence-technology-evaluation/TestResults/" + impFolder + "/Log/testLog_TestNr_"+ testNr + ".txt";
	public static String logDocPath = logDocPathWin;
	public static String eventsFilePathWin = "C:/Users/ebinjak/Documents/Exjobb/DataSet/events.json";
	public static String eventsFilePathMac = "/Users/Jakub1/Documents/Universitet/Exjobb/Imp/json_example/events.json";
	public static String filePath3 = "C:/Users/ebinjak/Documents/Exjobb/Default_events/default/events.json";
	public static String eventsFilePath = filePath3;
	
	
	public static void main( String[] args ) throws Exception {
		System.out.println("Start");
		test.setUpImp();
		test.setFilterParameters();
		test.setGAVFilterParameters();
		//timePos = testSizes.get(0)/2;
		timePos = 5000;
		upstreamEventNr = 500;
	    	
    	Thread thread = new Thread(new Runnable(){
    		public void run(){
    			try {
    				for(int i = 0; i < testSizes.size(); i++){
		    			con.removeAllEvents();
		    	    	int amount = testSizes.get(i);
		    	    	System.out.println("Problem size : " + amount);
		    	    	
		    	    	downstreamEventNr = (amount - 200);
		    	    	
		    	    	test.testStore("1", amount);
						
		    	    	//test.testGetEvent("2", amount);
		    	    	
		    		    //mainMetaTime = metaTimeList.get(timePos);
		    		    long tempMainTime = (long) Double.parseDouble(metaTimeList.get(timePos));
		    		    mainMetaTime = String.valueOf(tempMainTime);
		    		    filterList2.addFilterParameter("meta_time", mainMetaTime, "!=");
						
		    		    //System.out.println(amount + " , " + mainMetaTime);
		    		   
		    		  /* test.testGetEvents0("3", amount);
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
		    	    	
		    	    	test.testGetArtifactByGAV("6", amount);
		    	    	
		    	    	test.testGetUpstreamEvents0("7", amount);
		    	    	test.testGetUpstreamEvents1("7", amount);
		    	    	test.testGetUpstreamEvents2("7", amount);
		    	    	test.testGetUpstreamEvents3("7", amount);*/
		    		    
		    		    test.testGetDownstreamEvents0("8", amount);
		    	    	test.testGetDownstreamEvents1("8", amount);
		    	    	test.testGetDownstreamEvents2("8", amount);
		    	    	test.testGetDownstreamEvents3("8", amount);
		    	    	
		    	    	/*test.testCombinations1("9_1", amount);
		    	    	test.testCombinations2("9_1", amount);
		    	    	test.testCombinations3("9_1", amount);*/
		    	    	
		    	    	test.testCombinations4("9_2", amount);
		    	    	test.testCombinations5("9_2", amount);
		    	    	test.testCombinations6("9_2", amount);
		    		    
		    		/*    test.testCombinations7("9_3", amount);
		    		    
		    		    test.testCombinations8("9_4", amount);
		    		    
		    		    test.testCombinations9("9_5", amount);
		    		    
		    		    test.testCombinations10("9_6", amount);
		    		    
		    		    test.testDiffAmountThreads1("10_1", amount);
		    		    test.testDiffAmountThreads2("10_2", amount);
		    		    test.testDiffAmountThreads3("10_3", amount);
		    		    test.testDiffAmountThreads4("10_4", amount);
		    		    test.testDiffAmountThreads5("10_5", amount);
		    		    test.testDiffAmountThreads6("10_6", amount);
		    		    test.testDiffAmountThreads7("10_7", amount);
		    		    
		    		    test.testDiffAmountThreadsUD1("10_8", amount);
		    		    test.testDiffAmountThreadsUD2("10_9", amount);
		    		    test.testDiffAmountThreadsUD3("10_10", amount);
		    		    test.testDiffAmountThreadsUD4("10_11", amount);
		    		    test.testDiffAmountThreadsUD5("10_12", amount);
		    		    test.testDiffAmountThreadsUD6("10_13", amount);
		    		    test.testDiffAmountThreadsUD7("10_14", amount);
		    		    test.testDiffAmountThreadsUD8("10_15", amount);
		    		    test.testDiffAmountThreadsUD9("10_16", amount);
		    		    test.testDiffAmountThreadsUD10("10_17", amount);*/
		    		    
		    	    	
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
	    
	
	
	public void setUpImp() throws Exception{
        
        try {
        	con = new ArangoDBDatabaseHelperV2();
        	con.arangoDBGraphSetUp();
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
        filterList.addFilterParameter("data_version", "1", "==");
        filterList.addFilterParameter("meta_time", "1.532083206212E12", "==");
	}
	
	public void setGAVFilterParameters() throws Exception{
		GAVFilterList.getFilterList().clear();
		GAVFilterList.addFilterParameter("meta_type", "EiffelArtifactCreatedEvent", "==");
		GAVFilterList.addFilterParameter("meta_version", "1.0.0", "==");
		GAVFilterList.addFilterParameter("meta_time", "1.532083406212E12", "==");
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
	
	/*public static List<JSONObject> readJsonStream(InputStream in) throws IOException{
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
		
	}*/
	

	public void readJsonStream(int max, int iteration) throws IOException, ParseException{
		InputStream infile = new FileInputStream(eventsFilePath);
		JsonReader reader = new JsonReader(new InputStreamReader(infile, "UTF-8"));
		Gson gson = new Gson();
		reader.beginArray();
		int i = 0;
		while (reader.hasNext() && i < max) {
			JSONObject message = gson.fromJson(reader, JSONObject.class);
			JSONObject json = (JSONObject) new JSONParser().parse(message.toString());
			con.store(json);
			if(iteration == 0) {
				metaIdList.add(((Map) message.get("meta")).get("id").toString());
				metaTimeList.add(((Map) message.get("meta")).get("time").toString());
			}
			i++;
		}
		//reader.endArray();
		reader.close();
		infile.close();
	}
	
	public String createResFilePath(String folder, String testCaseNr, String functionName, int size){
		String resPathMac = "/Users/Jakub1/Documents/Universitet/Exjobb/Imp/Project/eiffel-persistence-technology-evaluation/TestResults/";
		String resPathWin = "C:/Users/ebinjak/Documents/Exjobb/eiffel-persistence-technology-evaluation/TestResults/";
		String res = "";
		if(size == 0){
			res = resPathWin + impFolder + "/" + folder + "/" + DBMSName + "_" + imp + "_TestNr_" + testNr + "_TestCase_" + testCaseNr + "_" + functionName + ".csv";
		}else{
			res = resPathWin + impFolder + "/" + folder + "/" + DBMSName + "_" + imp + "_TestNr_" + testNr + "_TestCase_" + testCaseNr + "_" + functionName + "_" + size + ".csv";
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
		
		/*List<JSONObject> jsonArr = new ArrayList<JSONObject>();
	    InputStream infile = new FileInputStream(eventsFilePath);
	    jsonArr = readJsonStream(infile);
	    infile.close();*/
	    

	    
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
	    		
	    		//con.storeManyEvents(jsonArr, amount);
	    		readJsonStream(amount, c);
	    		
	    		long endTime = System.nanoTime();
	    		
	    		long elapsedTime = (endTime-startTime) / timeDivision;
	    		sumWhole += elapsedTime;
	    		storeIterResInFile(iterationsResWhole, Integer.toString(c+1) , Long.toString(elapsedTime), Integer.toString(0),Integer.toString(0), timeFormatName);
	    		
	    		int size = con.getTimeRes().size();
	    		for(int i = 0; i < size; i++){
	    			long conElapsedTime = con.getTimeRes().get(i) / timeDivision;
	    			//storeIterResInFile(iterationsResQuery, Integer.toString(i+1) , Long.toString(conElapsedTime), Integer.toString(0),Integer.toString(0), timeFormatName);
	    			sumQuery += (conElapsedTime);
	    		}
	    		
	    		//storeIterResInFile(iterationsResQuery, "---" , "---", "---", "", "");
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
		//jsonArr.clear();
		
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
		String metaId = metaIdList.get(upstreamEventNr);
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
		String metaId = metaIdList.get(upstreamEventNr);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			//System.out.println(tempCaseNr);
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
		String metaId = metaIdList.get(upstreamEventNr);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		
		for(int i = 6; i < 11; i++){
			String tempCaseNr = caseNr + "_" + i;
			//System.out.println(tempCaseNr);
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
		String metaId = metaIdList.get(upstreamEventNr);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		List<Object> res = new ArrayList<>();
		
		for(int i = 11; i < 16; i++){
			String tempCaseNr = caseNr + "_" + i;
			//System.out.println(tempCaseNr);
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
		String metaId = metaIdList.get(downstreamEventNr);
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
		String metaId = metaIdList.get(downstreamEventNr);
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
		String metaId = metaIdList.get(downstreamEventNr);
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
		String metaId = metaIdList.get(downstreamEventNr);
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
		String functionName = "getEvent_getUpstreamEvents";
		List<String> tempLinkTypes = new ArrayList<String>();
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(upstreamEventNr);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			tempLinkTypes.add(linkTypes.get(i-1));

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				
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
	
	public void testCombinations2(String caseNr, int amount) throws Exception{
		String functionName = "getEvent_getUpstreamEvents";
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(upstreamEventNr);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		
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
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = getEventGetUpstreamEvents(metaId, linkTypes, limit, levels, visitedMap);
		    				
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
	
	public void testCombinations3(String caseNr, int amount) throws Exception{
		String functionName = "getEvent_getUpstreamEvents";
		int levels = 10;
		int limit = 10;
		long count = 0;
		String metaId = metaIdList.get(upstreamEventNr);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		
		for(int i = 11; i < 16; i++){
			String tempCaseNr = caseNr + "_" + i;

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = getEventGetUpstreamEvents(metaId, linkTypes, limit, levels, visitedMap);
		    				
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
			limit = limit * 10;
		}
	}
	
	public int getEventGetDownstreamEvents(String metaId, List<String> linkTypesList, int limit, int levels, ConcurrentMap<String, String> visitedMap){
		List<Object> res = new ArrayList<>();
		con.getEvent(metaId);
		res = con.getDownstreamEvents(metaId, linkTypesList, visitedMap, limit, levels);
		return res.size();	
	}
	
	
	public void testCombinations4(String caseNr, int amount) throws Exception{
		String functionName = "getEvent_getDownstreamEvents";
		List<String> tempLinkTypes = new ArrayList<String>();
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(downstreamEventNr);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			tempLinkTypes.add(linkTypes.get(i-1));

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = getEventGetDownstreamEvents(metaId, tempLinkTypes, limit, levels, visitedMap);
		    				
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
	
	public void testCombinations5(String caseNr, int amount) throws Exception{
		String functionName = "getEvent_getDownstreamEvents";
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(downstreamEventNr);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		
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
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = getEventGetDownstreamEvents(metaId, linkTypes, limit, levels, visitedMap);
		    				
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
	
	public void testCombinations6(String caseNr, int amount) throws Exception{
		String functionName = "getEvent_getDownstreamEvents";
		int levels = 10;
		int limit = 10;
		long count = 0;
		String metaId = metaIdList.get(downstreamEventNr);
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		
		for(int i = 11; i < 16; i++){
			String tempCaseNr = caseNr + "_" + i;

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		count = getEventGetDownstreamEvents(metaId, linkTypes, limit, levels, visitedMap);
		    				
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
			limit = limit * 10;
		}
	}
	
	public void testCombinations7(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactsByGroup_getUpstreamEvents";
		List<String> tempLinkTypes = new ArrayList<String>();
		int levels = 10;
		int limit = 1000;
		int skip = 0;
		long count = 0;
		FilterParameterList tempFilterList = new FilterParameterList();
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		DataStoreResult ABGresult = new DataStoreResult();
		
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			tempLinkTypes.add(linkTypes.get(i-1));

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		int amountRecEvents = 0;
		    		
		    		long startTime = System.nanoTime();
		    		
		    		ABGresult = con.getArtifactsByGroup(groupId, tempFilterList, "<", skip, limit);

		    		for(int d = 0; d < ABGresult.getCount(); d++){
		    			JSONObject event = ABGresult.getEventFromEventsArray(d);
		    			String eventId = ((Map) event.get("meta")).get("id").toString();
		    			amountRecEvents += con.getUpstreamEvents(eventId, tempLinkTypes, visitedMap, limit, levels).size();
		    			visitedMap.clear();	
		    		}
		    			
		    		long endTime = System.nanoTime();
		    		
		    		count = amountRecEvents;
		    		
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
	
	public void testCombinations8(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactsByGroup_getDownstreamEvents";
		List<String> tempLinkTypes = new ArrayList<String>();
		int levels = 10;
		int limit = 1000;
		int skip = 0;
		long count = 0;
		FilterParameterList tempFilterList = new FilterParameterList();
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		DataStoreResult ABGresult = new DataStoreResult();
		
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			tempLinkTypes.add(linkTypes.get(i-1));

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		int amountRecEvents = 0;
		    		
		    		long startTime = System.nanoTime();
		    		
		    		ABGresult = con.getArtifactsByGroup(groupId, tempFilterList, "<", skip, limit);

		    		for(int d = 0; d < ABGresult.getCount(); d++){
		    			JSONObject event = ABGresult.getEventFromEventsArray(d);
		    			String eventId = ((Map) event.get("meta")).get("id").toString();
		    			amountRecEvents += con.getDownstreamEvents(eventId, tempLinkTypes, visitedMap, limit, levels).size();
		    			visitedMap.clear();	
		    		}
		    				
		    		long endTime = System.nanoTime();
		    		
		    		count = amountRecEvents;
		    		
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
	
	public void testCombinations9(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactByGAV_getUpstreamEvents";
		List<String> tempLinkTypes = new ArrayList<String>();
		int levels = 10;
		int limit = 1000;
		long count = 0;
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			tempLinkTypes.add(linkTypes.get(i-1));

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		JSONObject event = con.getArtifactByGAV(groupId, artifactId, gavVersion);
	    			String eventId = ((Map) event.get("meta")).get("id").toString();
	    			count = con.getUpstreamEvents(eventId, tempLinkTypes, visitedMap, limit, levels).size();
	    				
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
	
	public void testCombinations10(String caseNr, int amount) throws Exception{
		String functionName = "getArtifactByGAV_getDownstreamEvents";
		List<String> tempLinkTypes = new ArrayList<String>();
		int levels = 10;
		int limit = 1000;
		long count = 0;
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
		
		for(int i = 1; i < 6; i++){
			String tempCaseNr = caseNr + "_" + i;
			
			tempLinkTypes.add(linkTypes.get(i-1));

			try{
			
				String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
				String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
				
				double sumWhole = 0;
				
		    	for(int c = 0; c < iterationsNumb; c++) {
		    		
		    		long startTime = System.nanoTime();
		    		
		    		JSONObject event = con.getArtifactByGAV(groupId, artifactId, gavVersion);
	    			String eventId = ((Map) event.get("meta")).get("id").toString();
	    			count = con.getDownstreamEvents(eventId, tempLinkTypes, visitedMap, limit, levels).size();
		    				
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
	
	public void tempGetEventsThreads(String tempCaseNr, String functionName, int amount, FilterParameterList tempFilterList) throws Exception{
		
		String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
		String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
		String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
		String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
		
		int skip = 0;
		int limit = 1000;
		
		long count = 0;
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
    	
	}
	
	public void testDiffAmountThreads1(String caseNr, int amount) {
		
		String functionName = "diffAmountThreads";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(2);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "==");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		        
		    t1.start(); 
		    gate.await();
		    t1.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	public void testDiffAmountThreads2(String caseNr, int amount) {
		
		String functionName = "diffAmountThreads";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(3);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "==");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
			    Thread t2 = new Thread(){
				    public void run(){
				        try {
							gate.await();
							String tempCaseNr = caseNr + "_" + 2; 
						    FilterParameterList tempFilterList = new FilterParameterList();
							tempFilterList.getFilterList().clear();
							tempFilterList.addFilterParameter("meta_time", time1, ">");
							tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
							logDone(tempCaseNr, functionName, amount);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logError(caseNr, functionName, amount, e);
						}	
				    }};
			    
		        
		    t1.start(); 
		    t2.start(); 
		    gate.await();
		    t1.join();
		    t2.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}

	public void testDiffAmountThreads3(String caseNr, int amount) {
		
		String functionName = "diffAmountThreads";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(4);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "==");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
			    Thread t2 = new Thread(){
				    public void run(){
				        try {
							gate.await();
							String tempCaseNr = caseNr + "_" + 2; 
						    FilterParameterList tempFilterList = new FilterParameterList();
							tempFilterList.getFilterList().clear();
							tempFilterList.addFilterParameter("meta_time", time1, ">");
							tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
							logDone(tempCaseNr, functionName, amount);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							logError(caseNr, functionName, amount, e);
						}	
				    }};
			    
				    Thread t3 = new Thread(){
					    public void run(){
					        try {
								gate.await();
								String tempCaseNr = caseNr + "_" + 3; 
							    FilterParameterList tempFilterList = new FilterParameterList();
								tempFilterList.getFilterList().clear();
								tempFilterList.addFilterParameter("meta_time", time1, "<");
								tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
								logDone(tempCaseNr, functionName, amount);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								logError(caseNr, functionName, amount, e);
							}	
					    }};
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	
	public void testDiffAmountThreads4(String caseNr, int amount) {
		
		String functionName = "diffAmountThreads";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(5);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "==");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, ">");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		    
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "<");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
				    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, ">=");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	
	public void testDiffAmountThreads5(String caseNr, int amount) {
		
		String functionName = "diffAmountThreads";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(6);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "==");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, ">");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		    
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "<");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
				    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, ">=");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t5 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 5; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "<=");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    t5.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    t5.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	
	
	public void testDiffAmountThreads6(String caseNr, int amount) {
		
		String functionName = "diffAmountThreads";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(7);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "==");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, ">");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		    
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "<");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
				    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, ">=");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t5 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 5; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "<=");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t6 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 6; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "!=");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    t5.start();
		    t6.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    t5.join();
		    t6.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	
	public void testDiffAmountThreads7(String caseNr, int amount) {
		
		String functionName = "diffAmountThreads";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(8);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "==");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, ">");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		    
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "<");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
				    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, ">=");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t5 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 5; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "<=");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t6 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 6; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, "!=");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		        
		    Thread t7 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 7; 
					    FilterParameterList tempFilterList = new FilterParameterList();
						tempFilterList.getFilterList().clear();
						tempFilterList.addFilterParameter("meta_time", time1, ">");
						tempFilterList.addFilterParameter("meta_time", time2, "<");
						tempGetEventsThreads(tempCaseNr, functionName, amount, tempFilterList);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
				    
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    t5.start();
		    t6.start();
		    t7.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    t5.join();
		    t6.join();
		    t7.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	
	public void tempGetUpstreamThreads(String tempCaseNr, String functionName, int amount, List<String> tempLinkTypes) throws Exception{
			
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(upstreamEventNr);
		
		List<Object> res = new ArrayList<>();
		
		String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
		String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
		String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
		String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
		
		double sumWhole = 0;
		double sumQuery = 0;
		
		
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
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
    	
	
	}

	public void tempGetDownstreamThreads(String tempCaseNr, String functionName, int amount, List<String> tempLinkTypes) throws Exception{
		
		int levels = 10;
		int limit = 1000;
		long count = 0;
		String metaId = metaIdList.get(downstreamEventNr);
		
		List<Object> res = new ArrayList<>();
		
		String iterationsResWhole = createResFilePath("All_iterations/Whole", tempCaseNr, functionName + "_Whole_", amount);
		String iterationsResQuery = createResFilePath("All_iterations/Connection", tempCaseNr, functionName + "_Connection_", amount);
		String averageResWhole = createResFilePath("Average/Whole", tempCaseNr, functionName + "_Whole_", 0);
		String averageResQuery = createResFilePath("Average/Connection", tempCaseNr, functionName + "_Connection_", 0);
		
		double sumWhole = 0;
		double sumQuery = 0;
		
		
		ConcurrentMap<String, String> visitedMap = new ConcurrentHashMap<String,String>();
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
    	
	
	}
	
	public void testDiffAmountThreadsUD1(String caseNr, int amount) {
		
		String functionName = "diffAmountThreadsUD";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(2);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		        
		    t1.start(); 
		    gate.await();
		    t1.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	public void testDiffAmountThreadsUD2(String caseNr, int amount) {
		
		String functionName = "diffAmountThreadsUD";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(3);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		        
		    t1.start(); 
		    t2.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}

	public void testDiffAmountThreadsUD3(String caseNr, int amount) {
		
		String functionName = "diffAmountThreadsUD";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(4);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	public void testDiffAmountThreadsUD4(String caseNr, int amount) {
		
		String functionName = "diffAmountThreadsUD";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(5);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	public void testDiffAmountThreadsUD5(String caseNr, int amount) {
		
		String functionName = "diffAmountThreadsUD";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(6);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t5 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 5; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempLinkTypes.add(linkTypes.get(4));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    t5.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    t5.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	public void testDiffAmountThreadsUD6(String caseNr, int amount) {
		
		String functionName = "diffAmountThreadsUD";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(7);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t5 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 5; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempLinkTypes.add(linkTypes.get(4));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t6 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 6; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    t5.start();
		    t6.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    t5.join();
		    t6.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	public void testDiffAmountThreadsUD7(String caseNr, int amount) {
		
		String functionName = "diffAmountThreadsUD";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(8);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t5 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 5; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempLinkTypes.add(linkTypes.get(4));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t6 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 6; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t7 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 7; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    t5.start();
		    t6.start();
		    t7.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    t5.join();
		    t6.join();
		    t7.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}

	public void testDiffAmountThreadsUD8(String caseNr, int amount) {
		
		String functionName = "diffAmountThreadsUD";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(9);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t5 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 5; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempLinkTypes.add(linkTypes.get(4));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t6 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 6; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t7 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 7; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t8 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 8; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    t5.start();
		    t6.start();
		    t7.start();
		    t8.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    t5.join();
		    t6.join();
		    t7.join();
		    t8.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	public void testDiffAmountThreadsUD9(String caseNr, int amount) {
		
		String functionName = "diffAmountThreadsUD";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(10);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t5 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 5; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempLinkTypes.add(linkTypes.get(4));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t6 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 6; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t7 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 7; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t8 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 8; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t9 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 9; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
		        
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    t5.start();
		    t6.start();
		    t7.start();
		    t8.start();
		    t9.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    t5.join();
		    t6.join();
		    t7.join();
		    t8.join();
		    t9.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}
	
	public void testDiffAmountThreadsUD10(String caseNr, int amount) {
		
		String functionName = "diffAmountThreadsUD";
		
		
		int size1 = timePos;
		int size2 = size1/2 + size1;
		
		long tempMainTime1 = (long) Double.parseDouble(metaTimeList.get(size1));
		String time1 = String.valueOf(tempMainTime1);
		long tempMainTime2 = (long) Double.parseDouble(metaTimeList.get(size2));
		String time2 = String.valueOf(tempMainTime2);
		
		
		
		final CyclicBarrier gate = new CyclicBarrier(11);
		try{
			Thread t1 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 1; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t2 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 2; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			
		    Thread t3 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 3; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t4 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 4; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t5 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 5; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempLinkTypes.add(linkTypes.get(4));
						tempGetUpstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t6 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 6; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t7 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 7; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t8 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 8; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    Thread t9 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 9; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
	        
		    Thread t10 = new Thread(){
			    public void run(){
			        try {
						gate.await();
						String tempCaseNr = caseNr + "_" + 10; 
						List<String> tempLinkTypes = new ArrayList<String>();
						tempLinkTypes.clear();
						tempLinkTypes.add(linkTypes.get(0));
						tempLinkTypes.add(linkTypes.get(1));
						tempLinkTypes.add(linkTypes.get(2));
						tempLinkTypes.add(linkTypes.get(3));
						tempLinkTypes.add(linkTypes.get(4));
						tempGetDownstreamThreads(tempCaseNr, functionName, amount, tempLinkTypes);
						logDone(tempCaseNr, functionName, amount);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logError(caseNr, functionName, amount, e);
					}	
			    }};
			    
		    t1.start(); 
		    t2.start();
		    t3.start();
		    t4.start();
		    t5.start();
		    t6.start();
		    t7.start();
		    t8.start();
		    t9.start();
		    t10.start();
		    gate.await();
		    t1.join();
		    t2.join();
		    t3.join();
		    t4.join();
		    t5.join();
		    t6.join();
		    t7.join();
		    t8.join();
		    t9.join();
		    t10.join();
		    System.out.println("all threads started");
		    
		}catch(Exception e){
			logError(caseNr, functionName, amount, e);
		}
	}

}
