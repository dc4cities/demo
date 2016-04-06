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

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import eu.dc4cities.demo.DemoConfiguration;
import eu.dc4cities.demo.rest.TestBed;

@Path("/testbedservice")
public class TestBedService {

    @GET
    public Response getTestBeds() {
    	List<TestBed> testBeds = DemoConfiguration.getDemoConfiguration().getTestBeds();
    	List<TestBed> cleanTestBeds = new ArrayList<TestBed>(testBeds.size());
    	for (TestBed testBed : testBeds) {
    		cleanTestBeds.add(testBed.withoutInternals());
    	}
        Gson gson = new Gson();
        String json = gson.toJson(cleanTestBeds);
        return Response.status(200).entity(json).build();
    }

}