package com.multimodel.ArangoDB;

public class FilterParameter {
	public static final String[] COMPARATORS = { ">=", ">", "<=", "<", "!=", "==" };
	
	/*
	== equality
	!= inequality
	< less than
	<= less or equal
	> greater than
	>= greater or equal
	*/
	
	private String key;
	private String value;
	private String comparator;
	
	public FilterParameter(String key, String value, String comparator) throws Exception{
		this.key = key;
		this.value = value;
		if(checkComparator(comparator)){
			this.comparator = comparator;
		}else{
			throw new Exception(
                    "Comparator '" + comparator + "' is illegal. Valid comparators are " + getValidComparatorString());
		}
	}
	
	public boolean checkComparator(String comparator){
		for(String str: COMPARATORS) {
		    if(str.equals(comparator)){
		    	//System.out.println(comparator);
		       return true;
		    }
		}
		return false;
	}
	
	private String getValidComparatorString() {
		String valid = "";
		for(String e: COMPARATORS){
			valid += e + ", ";
		}
		valid = valid.substring(0, valid.length()-2);
		return valid;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public String getComparator(){
		return this.comparator;
	}
	
}
