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

package eu.dc4cities.demo.rest;

import java.util.LinkedList;
import java.util.List;

public class TestBedActivity {

	private String name;
	private List<String> dataCenters = new LinkedList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDataCenters() {
		return dataCenters;
	}

	public void setDataCenters(List<String> dataCenters) {
		this.dataCenters = dataCenters;
	}
	
	public void addDataCenter(String dataCenter) {
		dataCenters.add(dataCenter);
	}
	
}
