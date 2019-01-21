package com.multimodel.ArangoDB;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.json.simple.JSONObject;


public interface DatabaseHelper {

boolean store(JSONObject json);
	
	JSONObject getEvent(String metaId);
	
	DataStoreResult getEvents(FilterParameterList filterList, final String comparator, final int skip, final int limit, final boolean lazy);
	
	void removeAllEvents();
	
	
	List<Object> getUpstreamEvents(final String eventId, final List<String> linkTypes, final ConcurrentMap<String, String> visitedMap,
						final int limit, final int levels);
			
	boolean performUpstreamSearch(final JSONObject event, final List<String> linkTypes, final ConcurrentMap<String, String> visitedMap, 
						final int limit, final int levels, final List<Object> events);
	
	List<Object> getDownstreamEvents(final String eventId, final List<String> linkTypes, final ConcurrentMap<String, String> visitedMap,
			final int limit, final int levels);
	
	
	boolean performDownstreamSearch(final String eventId, final List<String> linkTypes, final ConcurrentMap<String, String> visitedMap, 
			final int limit, final int levels, final List<Object> events);
	
	// Original
	/*List<Object> getUpstreamEvents(final String eventId, final List<LinkType> linkTypes, final ConcurrentMap<String, String> visitedMap,
				final int limit, final int levels, final Condition<Map> condition);*/
	
	/*boolean performUpstreamSearch(final Document event, final List<LinkType> linkTypes, final ConcurrentMap<String, String> visitedMap, 
				final int limit, final int levels, final List<Object> events, final Condition<Map> condition);*/
	
																												//<id, "true/false">
	/*List<Object> getDownstreamEvents(final String eventId, final List<LinkType> linkTypes, final ConcurrentMap<String, String> visitedMap,
			final int limit, final int levels, final Condition<Map> condition);*/
	
	
	/*boolean performDownstreamSearch(final String eventId, final List<LinkType> linkTypes, final ConcurrentMap<String, String> visitedMap, 
			final int limit, final int levels, final List<Object> events, final Condition<Map> condition);*/
	
	DataStoreResult getArtifactsByGroup( final String groupId, FilterParameterList filterList, final String comparator, final int skip, final int limit);
	
	DataStoreResult getArtifactsByGroupAndArtifactId( final String groupId, final String artifactId, FilterParameterList filterList, final String comparator, 
      															final int skip, final int limit);
      															
    JSONObject getArtifactByGAV(final String groupId, final String artifactId, final String version);
	
	
}
