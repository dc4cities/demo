/*
 * Copyright 2012 The DC4Cities author.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.dc4cities.demo.rest.custom;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import eu.dc4cities.controlsystem.model.unit.Units;
import eu.dc4cities.demo.rest.TestBedActivity;
import eu.dc4cities.easc.Application;
import eu.dc4cities.easc.activity.Activity;
import eu.dc4cities.easc.activity.DataCenterWorkingModes;
import eu.dc4cities.easc.configuration.ConfigReader;

@Path("/activityservice")
public class ActivityService {

	@GET
    @Path("/{param}")
    public Response getActivities(@PathParam("param") String testBedId) throws IOException, URISyntaxException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	String eascDirName = "testbeds/" + testBedId + "/easc";
    	URL eascDir = classLoader.getResource(eascDirName);
    	Units.init();
        Application appConfig = ConfigReader.readAppConfig(new File(eascDir.toURI()).getCanonicalPath()).get();
        List<TestBedActivity> testBedActivities = new LinkedList<>();
        for (Activity activity : appConfig.getActivities()) {
        	TestBedActivity testBedActivity = new TestBedActivity();
        	testBedActivity.setName(activity.getName());
        	for (DataCenterWorkingModes dataCenter : activity.getDataCenters()) {
        		testBedActivity.addDataCenter(dataCenter.getDataCenterName());
        	}
        	testBedActivities.add(testBedActivity);
        }
        Gson gson = new Gson();
        String json = gson.toJson(testBedActivities);
        return Response.status(200).entity(json).build();
	}
	
}
