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

package eu.dc4cities.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.gson.Gson;

import eu.dc4cities.demo.rest.TestBed;

public class DemoConfiguration {

	private static DemoConfiguration demoConfiguration;
	
	private String simulatorPath;
	private String simulatorWorkDir;
	private String hdbUrl;
	private String energisUrl;
	private String energisApiKey;
	private List<TestBed> testBeds;

	public String getSimulatorPath() {
		return simulatorPath;
	}

	public void setSimulatorPath(String simulatorPath) {
		this.simulatorPath = simulatorPath;
	}

	public String getSimulatorWorkDir() {
		return simulatorWorkDir;
	}

	public void setSimulatorWorkDir(String simulatorWorkDir) {
		this.simulatorWorkDir = simulatorWorkDir;
	}

	public String getHdbUrl() {
		return hdbUrl;
	}

	public void setHdbUrl(String hdbUrl) {
		this.hdbUrl = hdbUrl;
	}

	public String getEnergisUrl() {
		return energisUrl;
	}

	public void setEnergisUrl(String energisUrl) {
		this.energisUrl = energisUrl;
	}

	public String getEnergisApiKey() {
		return energisApiKey;
	}

	public void setEnergisApiKey(String energisApiKey) {
		this.energisApiKey = energisApiKey;
	}

	public TestBed getTestBed(String id) {
		for (TestBed testBed : testBeds) {
			if (testBed.getId().equals(id)) {
				return testBed;
			}
		}
		return null;
	}
	
	public List<TestBed> getTestBeds() {
		return testBeds;
	}

	public void setTestBeds(List<TestBed> testBeds) {
		this.testBeds = testBeds;
	}
	
	public static DemoConfiguration getDemoConfiguration() {
		if (demoConfiguration == null) {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			try (InputStream configStream = classLoader.getResourceAsStream("demo-configuration.json");
					InputStreamReader configReader = new InputStreamReader(configStream)) {
				Gson gson = new Gson();
		        demoConfiguration = gson.fromJson(configReader, DemoConfiguration.class);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return demoConfiguration;
	}
	
}
