DEMO - PORTAL
=================================
The demo portal consists of two war-files that can be hosted on any webserver, which has support for java servlets.
The first war-file contains the User interface, written in with Bootstrap, AngularJS and JQuery.
The second war-file contains the REST services that collect the data from the server and make the availabel for the UI and Services, that prepare and the CS simulator.

Installation
---------------
1. Install JDK 8
2. Install Tomcat 7
3. Run `mvn package` to build the two WAR files
4. Rename `demo-gui-0.1-SNAPSHOT.war` to `demo-gui.war` and `demo-rest-0.1-SNAPSHOT.war` to `demo-rest.war`
5. Copy `demo-gui.war` and `demo-rest.war` to `$CATALINA_HOME/webapps`
6. Create a directory named `d4c-simulator` in `$CATALINA_HOME`
7. Copy the latest build of `benchcs-1.0-jar-with-dependencies.jar` to `$CATALINA_HOME/d4c-simulator`
8. Create a directory named `demo-rest` in `$CATALINA_HOME/conf`
9. Copy the content of `$DEMO_ROOT/demo-rest/src/main/config` to the directory created above
10. Make any required changes to demo-configuration.json in the configuration directory created above. Specifically:
* `simulatorPath` must point to `$CATALINA_HOME/d4c-simulator/benchcs-1.0-jar-with-dependencies.jar`, where `$CATALINA_HOME` must be replace with the actual path
* `simulatorWorkDir` must point to the working directory for the simulator, e.g. `$CATALINA_HOME/d4c-simulator/work`, where `$CATALINA_HOME` must be replace with the actual path
* `hdbUrl` must point to KairosDB, e.g. "http://localhost:8081"
* `energisUrl` must point to the Energis endpoint used to upload alerts, e.g. "http://localhost/energiscloud-gateway/restful/api"
* `energisApiKey` is the key to authenticate to the Energis API
* It is not necessary to change the testbed configuration unless you want to change the default simulation parameters
11. Make sure KairosDB is listening at `http://localhost:8081` (or the same address stated in `hdbUrl`)
12. Start Tomcat
13. Install Energis as per the relevant instructions
14. Open a browser to `localhost:8080/demo-gui` to launch a simulation

Configuration
----------------------------
The list of available testbeds is defined in `demo-configuration.json`. Each testbed id must correspond to a subdirectory of the `testbeds` directory. For each testbed the following directory structure must be respected:
* testbed-id
	* ctrl-backend
		* goal-configuration.json
		* technical-configuration.json
	* easc
		* AppConfig.yaml
		* Config.yaml
		* ServerConfig.yaml (optional, required only when the shared energy service must be used)
	* erds
		* datacenter-id-1
			* erds1-in-dc1
				* list of day profile CSVs
			* erds2-in-dc1
				* list of day profile CSVs
		* datacenter-id-2
			* erds1-in-dc2
				* list of day profile CSVs
			* erds2-in-dc2
				* list of day profile CSVs

These are the files that will be used to create the simulator environment for running the simulation of a scenario. Parameters chosen in the GUI (e.g. renewable percentage objective, date) overwrite the corresponding values in the testbed configuration files, while all other values are preserved as-is.