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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/dayprofileservice")
public class DayProfileService {

    @GET
    @Path("/{param}")
    public Response getDayProfiles(@PathParam("param") String testBedId) {
        Gson gson = new Gson();
        String json = gson.toJson(readDayProfilesFromFileSystem(testBedId));
        return Response.status(200).entity(json).build();
    }

    public static List<String> readDayProfilesFromFileSystem(String testBedId) {
    	// Read the available profiles directly from the list of CSV files in the testbed.
    	// The following directory structure is required:
    	// testbeds                 <-- top classpath dir with testbed data
    	// - testbed1               <-- dir for testbed with id testbed1
    	//   - erds                 <-- dir with ERDS sources for testbed1
    	//     - dc1                <-- dir with ERDS sources for dc1 in testbed1
    	//       - erds1_for_dc1    <-- dir with the first ERDS for dc1
    	//         - day1.csv
    	//         - day2.csv
        //       - erds2_for_dc1    <-- dir with the second ERDS for dc1
    	//         - day1.csv
    	//         - day2.csv
    	//     - dc2
    	//       - erds1_for_dc1
    	//         [..]
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    	String erdsRootDirName = "testbeds/" + testBedId + "/erds";
        File erdsRootDir = new File(classLoader.getResource(erdsRootDirName).getFile());
        if (!erdsRootDir.isDirectory()) {
        	throw new RuntimeException(erdsRootDirName + " is not a directory");
        }
        File[] dataCenterDirs = erdsRootDir.listFiles();
        if (dataCenterDirs.length == 0 || !dataCenterDirs[0].isDirectory()) {
        	throw new RuntimeException(erdsRootDirName + " must contain exactly one directory for each data center, "
        			+ "without other files");
        }
        File[] erdsDirs = dataCenterDirs[0].listFiles();
        if (erdsDirs.length == 0 || !erdsDirs[0].isDirectory()) {
        	throw new RuntimeException(dataCenterDirs[0] + " must contain exactly one directory for each ERDS, "
        			+ "without other files");
        }
        File[] dayProfiles = erdsDirs[0].listFiles();
        if (dayProfiles.length == 0) {
        	throw new RuntimeException("No day profiles defined in " + erdsDirs[0]);
        }
        List<String> profileNames = new ArrayList<>(dayProfiles.length);
        for (File dayProfile : dayProfiles) {
        	// Remove file extension to get the profile name
        	profileNames.add(dayProfile.getName().split("\\.")[0]);
        }
        Collections.sort(profileNames);
        return profileNames;
    }

}