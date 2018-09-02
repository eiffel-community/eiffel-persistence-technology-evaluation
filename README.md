# eiffel-persistence-technology-evaluation
Repository for evaluation of database technologies for the purposes of storing and retrieving Eiffel events.

## Tests

1. MongoDB - DBMS that supports document model
2. Neo4j  - DBMS that supports graph model
   - Imp1
   - Imp2
3. ArangoDB - DBMS that supports key, document and graph models
   - Imp1 - Pure document implementation, same as MongoDB
   - Imp2 - Multi-model implementation, combination of document and graph models
   
### Tests' description
* All results are in microseconds

1.  store()

2.  getEvent()

3.  getEvents()	:
	- 3_0	:	param	:	meta.time !=; skip = 0; limit = 5000

	- 3_1	:	param	: 	meta.type = ; limit 1000; skip = 0
	- 3_2	:	param	:	meta.type =; meta.version =  ; limit 1000; skip = 0
	- 3_3	:	param	: 	meta.type =; meta.version =  ; data.name=; limit 1000; skip = 0
	- 3_4	:	param	: 	meta.type = "EiffelConfidenceLevelModifiedEvent"; meta.version =  ; data.version =; data.name =; limit 1000; skip = 0
	- 3_5	:	param	: 	meta.type = "EiffelConfidenceLevelModifiedEvent"; meta.version =  ; meta.time =; data.name =; data.version =; limit = 1000; skip = 0

	- 3_6	:	param	: 	meta.time != ; limit 10; skip = 0
	- 3_7	:	param	: 	meta.time != ; limit 100; skip = 0
	- 3_8	:	param	: 	meta.time != ; limit 1000; skip = 0
	- 3_9	:	param	: 	meta.time != ; limit 10000; skip = 0
	- 3_10	:	param	: 	meta.time != ; limit 100000; skip = 0

	- 3_11	:	param	: 	meta.time != ; limit 10000; skip = 1
	- 3_12	:	param	: 	meta.time != ; limit 10000;; skip = 5
	- 3_13	:	param	: 	meta.time != ; limit 10000; skip = 10
	- 3_14	:	param	: 	meta.time != ; limit 10000; skip = 20
	- 3_15	:	param	: 	meta.time != ; limit 10000; skip = 50

	- 3_16	:	param	: 	meta.time = ; limit 1000; skip = 0
	- 3_17	:	param	: 	meta.time > ; limit 1000; skip = 0
	- 3_18	:	param	: 	meta.time < ; limit 1000; skip = 0
	- 3_19	:	param	: 	meta.time >= ; limit 1000; skip = 0
	- 3_20	:	param	: 	meta.time <= ; limit 1000; skip = 0
	- 3_21	:	param	: 	meta.time != ; limit 1000; skip = 0
	- 3_22	:	param	: 	meta.time > ; meta.time <; limit 1000; skip = 0

4.  getArtifactsByGroup()	:
	- 4_0	:	param	:	gav.groupid = ; skip = 0; limit = 1

	- 4_1	:	param	: 	gav.groupid = ; limit 1000; skip = 0
	- 4_2	:	param	:	gav.groupid = ; meta.type = ; limit 1000; skip = 0
	- 4_3	:	param	: 	gav.groupid = ; meta.type =; meta.version =  ; limit 1000; skip = 0
	- 4_4	:	param	: 	gav.groupid = ; meta.type =; meta.version =  ; meta.time =; limit 1000; skip = 0

	- 4_5	:	param	: 	gav.groupid = ; limit 10; skip = 0
	- 4_6	:	param	: 	gav.groupid =  ; limit 100; skip = 0
	- 4_7	:	param	: 	gav.groupid =  ; limit 1000; skip = 0
	- 4_8	:	param	: 	gav.groupid = ; limit 10000; skip = 0
	- 4_9	:	param	: 	gav.groupid = ; limit 100000; skip = 0

	- 4_10	:	param	: 	gav.groupid = ; limit 100000; skip = 1
	- 4_11	:	param	: 	gav.groupid =  ; limit 100000;; skip = 5
	- 4_12	:	param	: 	gav.groupid =  ; limit 100000; skip = 10
	- 4_13	:	param	: 	gav.groupid = ; limit 100000; skip = 20
	- 4_14	:	param	: 	gav.groupid = ; limit 100000; skip = 50

5.  getArtifactsByGroupAndArtifactId()	:
	- 5_0	:	param	:	gav.groupid = ; gav.artifactId() = ; skip = 0; limit = 5000

	- 5_1	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 1000; skip = 0
	- 5_2	:	param	:	gav.groupid = ; gav.artifactId() = ; meta.type = ; limit 1000; skip = 0
	- 5_3	:	param	: 	gav.groupid = ; gav.artifactId() = ; meta.type =; meta.version =  ; limit 1000; skip = 0
	- 5_4	:	param	: 	gav.groupid = ; gav.artifactId() = ; meta.type =; meta.version =  ; meta.time =; limit 1000; skip = 0

	- 5_5	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 10; skip = 0
	- 5_6	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 100; skip = 0
	- 5_7	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 1000; skip = 0
	- 5_8	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 10000; skip = 0
	- 5_9	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 100000; skip = 0

	- 5_10	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 100000; skip = 1
	- 5_11	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 100000;; skip = 5
	- 5_12	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 100000; skip = 10
	- 5_13	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 100000; skip = 20
	- 5_14	:	param	: 	gav.groupid = ; gav.artifactId() = ; limit 100000; skip = 50

6.  getArtifactByGAV()	:
	- 6_1	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ;
	- 6_2	:	param	:	gav.groupid = ; gav.artifactId() = ; gav.version = ;
	- 6_3	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ;
	- 6_4	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ;
	- 6_5	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ;
	- 6_6	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ;
	- 6_7	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ;
	- 6_8	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ;
	
7.  getUpstreamEvents()		:
	- 7_0	:	param	:	meta.id =; linkTypes : "ALL" ; levels = 50; limit = 5000

	- 7_1	:	param	: 	meta.id =; linkTypes : CAUSE ; limit = 1000; levels = 10;
	- 7_2	:	param	:	meta.id =; linkTypes : CAUSE, ELEMENT ; limit = 1000; levels = 10;
	- 7_3	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT ; limit = 1000; levels = 10;
	- 7_4	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION; limit = 1000; levels = 10;
	- 7_5	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION, BASE; limit = 1000; levels = 10;

	- 7_6	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 1;
	- 7_7	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 5;
	- 7_8	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 10;
	- 7_9	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 25;

	- 7_10	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 10; levels = 10;
	- 7_11	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 20; levels = 10;
	- 7_12	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 40; levels = 10;
	- 7_13	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 80; levels = 10;
	- 7_14	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 160; levels = 10;

8.  getDownstreamEvents()	:
	- 8_0	:	param	:	meta.id =; linkTypes : "ALL" ; levels = 50; limit = 5000
	- 8_1	:	param	: 	meta.id =; linkTypes : CAUSE ; limit = 1000; levels = 10;
	- 8_2	:	param	:	meta.id =; linkTypes : CAUSE, ELEMENT ; limit = 1000; levels = 10;
	- 8_3	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT ; limit = 1000; levels = 10;
	- 8_4	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION; limit = 1000; levels = 10;
	- 8_5	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION, BASE; limit = 1000; levels = 10;

	- 8_6	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 1;
	- 8_7	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 5;
	- 8_8	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 10;
	- 8_9	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 25;
	

	- 8_10	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 10; levels = 10;
	- 8_11	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 20; levels = 10;
	- 8_12	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 40; levels = 10;
	- 8_13	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 80; levels = 10;
	- 8_14  :	param	: 	meta.id =; linkTypes : "ALL" ; limit = 160; levels = 10;


9.  Combinations	:
	getEvent() + getUpstreamEvents()	:

	- 9_1_1	:	param	: 	meta.id =; linkTypes : CAUSE ; limit = 1000; levels = 10;
	- 9_1_2	:	param	:	meta.id =; linkTypes : CAUSE, ELEMENT ; limit = 1000; levels = 10;
	- 9_1_3	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT ; limit = 1000; levels = 10;
	- 9_1_4	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION; limit = 1000; levels = 10;
	- 9_1_5	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION, BASE; limit = 1000; levels = 10;

	- 9_1_6	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 1;
	- 9_1_7	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 5;
	- 9_1_8	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 10;
	- 9_1_9	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 25;
	- 9_1_10:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 50;

	- 9_1_11	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 10; levels = 10;
	- 9_1_12	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 20; levels = 10;
	- 9_1_13	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 40; levels = 10;
	- 9_1_14	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 80; levels = 10;
	- 9_1_15	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 160; levels = 10;

	getEvent()  + getDownstreamEvents()	:

	- 9_2_1	:	param	: 	meta.id =; linkTypes : CAUSE ; limit = 1000; levels = 10;
	- 9_2_2	:	param	:	meta.id =; linkTypes : CAUSE, ELEMENT ; limit = 1000; levels = 10;
	- 9_2_3	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT ; limit = 1000; levels = 10;
	- 9_2_4	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION; limit = 1000; levels = 10;
	- 9_2_5	:	param	: 	meta.id =; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION, BASE; limit = 1000; levels = 10;

	- 9_2_6	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 1;
	- 9_2_7	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 5;
	- 9_2_8	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 10;
	- 9_2_9	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 25;
	- 9_2_10	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 1000; levels = 50;

	- 9_2_11	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 10; levels = 10;
	- 9_2_12	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 20; levels = 10;
	- 9_2_13	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 40; levels = 10;
	- 9_2_14	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 80; levels = 10;
	- 9_2_15	:	param	: 	meta.id =; linkTypes : "ALL" ; limit = 160; levels = 10;

	getArtifactsByGroup() + getUpstreamEvents()	:

	- 9_3_1	:	param	: 	gav.groupid = ; linkTypes : CAUSE ; limit = 1000; levels = 10;
	- 9_3_2	:	param	:	gav.groupid = ; linkTypes : CAUSE, ELEMENT ; limit = 1000; levels = 10;
	- 9_3_3	:	param	: 	gav.groupid = ; linkTypes : CAUSE, ELEMENT, CONTEXT ; limit = 1000; levels = 10;
	- 9_3_4	:	param	: 	gav.groupid = ;  linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION; limit = 1000; levels = 10;
	- 9_3_5	:	param	: 	gav.groupid = ;  linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION, BASE; limit = 1000; levels = 10;

	getArtifactsByGroup() + getDownstreamEvents():

	- 9_4_1	:	param	: 	gav.groupid = ; linkTypes : CAUSE ; limit = 1000; levels = 10;
	- 9_4_2	:	param	:	gav.groupid = ; linkTypes : CAUSE, ELEMENT ; limit = 1000; levels = 10;
	- 9_4_3	:	param	: 	gav.groupid = ; linkTypes : CAUSE, ELEMENT, CONTEXT ; limit = 1000; levels = 10;
	- 9_4_4	:	param	: 	gav.groupid = ;  linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION; limit = 1000; levels = 10;
	- 9_4_5	:	param	: 	gav.groupid = ;  linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION, BASE; limit = 1000; levels = 10;

	getArtifactByGAV() + getUpstreamEvents()	:

	- 9_5_1	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ; linkTypes : CAUSE ; limit = 1000; levels = 10;
	- 9_5_2	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ; linkTypes : CAUSE, ELEMENT ; limit = 1000; levels = 10;
	- 9_5_3	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ; linkTypes : CAUSE, ELEMENT, CONTEXT ; limit = 1000; levels = 10;
	- 9_5_4	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION; limit = 1000;                       levels = 10;
	- 9_5_5	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION, BASE; limit = 1000; levels = 10;

  getArtifactByGAV() + getDownstreamEvents()	:
	- 9_6_1	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ; linkTypes : CAUSE ; limit = 1000; levels = 10;
	- 9_6_2	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ; linkTypes : CAUSE, ELEMENT ; limit = 1000; levels = 10;
	- 9_6_3	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ; linkTypes : CAUSE, ELEMENT, CONTEXT ; limit = 1000; levels = 10;
	- 9_6_4	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION; limit = 1000;                       levels = 10;
	- 9_6_5	:	param	: 	gav.groupid = ; gav.artifactId() = ; gav.version = ; linkTypes : CAUSE, ELEMENT, CONTEXT, COMPOSITION, BASE; limit = 1000; levels = 10;

10. Different amount of threads	:
	1 - 7 Threads 	:
	- 10_1	:	3_16
	- 10_2	:	3_16, 3_17
	- 10_3	:	3_16, 3_17, 3_18
	- 10_4	:	3_16, 3_17, 3_18, 3_19
	- 10_5	:	3_16, 3_17, 3_18, 3_19, 3_20
	- 10_6	:	3_16, 3_17, 3_18, 3_19, 3_20,3_21
	- 10_7	:	3_16, 3_17, 3_18, 3_19, 3_20,3_21, 3_22
		
	1-10 Threads	:
	- 10_8	:	7_1
	- 10_9	:	7_1, 7_2
	- 10_10	:	7_1, 7_2, 7_3
	- 10_11	:	7_1, 7_2, 7_3, 7_4
	- 10_12	:	7_1, 7_2, 7_3, 7_4, 7_5
	- 10_13	:	7_1, 7_2, 7_3, 7_4, 7_5, 8_1
	- 10_14	:	7_1, 7_2, 7_3, 7_4, 7_5, 8_1, 8_2
	- 10_15	:	7_1, 7_2, 7_3, 7_4, 7_5, 8_1, 8_2, 8_3
	- 10_16	:	7_1, 7_2, 7_3, 7_4, 7_5, 8_1, 8_2, 8_3, 8_4
	- 10_17	:	7_1, 7_2, 7_3, 7_4, 7_5, 8_1, 8_2, 8_3, 8_4, 8_5
