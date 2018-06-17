package com.graphmodel.neo4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilterParameterListV2 {
	Map<String, FilterParameterList> parametersList = new HashMap<String, FilterParameterList>();
	
	
	public Map<String, FilterParameterList> getParametersList(){
		return this.parametersList;
	}
	
	public FilterParameterList getListOfSpecificParameters(String key){
		return parametersList.get(key);
	}
	
	public String getSpecificParameter(String key1, String key2){
		return parametersList.get(key1).getValueWithKey(key2);
	}
	
	public String getSpecificComparator(String key1, String key2){
		return parametersList.get(key1).getComparatorWithKey(key2);
	}
	
	public void setParametersList(Map<String, FilterParameterList> list){
		this.parametersList = list;
	}
	
	public void addParameterListToList(FilterParameterList filterList){
		ArrayList<FilterParameter> tempList = filterList.getFilterList();
		for (int i = 0; i<tempList.size(); i++) {
			String o = tempList.get(i).getKey();
			String value = tempList.get(i).getValue();
			String comparator = tempList.get(i).getComparator();
			String[] keysList = o.substring(0).split("_");
			String key = keysList[keysList.length-1];
			String tempKey = "";
			for(int c = 0; c<keysList.length-1; c++){
					tempKey += keysList[c] + "_";
			}
			tempKey = tempKey.substring(0, tempKey.length() - 1);
			if(parametersList.containsKey(tempKey)){
				try {
					parametersList.get(tempKey).addFilterParameter(key, value, comparator);
				} catch (Exception e) {
					e.printStackTrace();
				};
			}else{
				FilterParameterList list = new FilterParameterList();
				try {
					list.addFilterParameter(key, value, comparator);
				} catch (Exception e) {
					e.printStackTrace();
				}
				parametersList.put(tempKey, list);
			}
			
		}
	}
	
	public void addParameterToList(String key, String value){
		
	}
	
}
