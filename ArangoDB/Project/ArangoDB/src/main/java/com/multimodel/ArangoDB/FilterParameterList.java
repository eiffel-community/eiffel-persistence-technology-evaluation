package com.multimodel.ArangoDB;

import java.util.ArrayList;


public class FilterParameterList {
	private ArrayList<FilterParameter> filterList = new ArrayList<FilterParameter>();

	public ArrayList<FilterParameter> getFilterList() {
		return filterList;
	}

	public void setFilterList(ArrayList<FilterParameter> filterList) {
		this.filterList = filterList;
	}
	
	public void addParameterToFilterList(FilterParameter param){
		this.filterList.add(param);
	}
	
	public void addFilterParameter(String key, String value, String comparator) throws Exception{
		FilterParameter param = new FilterParameter(key, value, comparator);
		this.filterList.add(param);
	}
	
	public FilterParameter getParameterFromList(int i){
		return this.filterList.get(i);
	}
	
	public String getValueWithKey(String key){
		String value = "";
		for(int i = 0; i < filterList.size(); i++){
			if(filterList.get(i).getKey().equals(key)){
				value = filterList.get(i).getValue();
			}
		}
		return value;
	}
	
	public String getComparatorWithKey(String key){
		String comp = "";
		for(int i = 0; i < filterList.size(); i++){
			if(filterList.get(i).getKey().equals(key)){
				comp = filterList.get(i).getComparator();
			}
		}
		return comp;
	}
}
