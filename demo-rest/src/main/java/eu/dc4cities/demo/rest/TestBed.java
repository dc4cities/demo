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

import java.util.List;

public class TestBed {

	private String id;
    private String displayName;
    private String energisAsset;
    private String energisDashboard;
    private List<Integer> renewablePercentages;
    private List<Double> penalties;
    private String simulatorOptions;
    
    public TestBed() {}
    
    public TestBed(TestBed testBed) {
    	this.id = testBed.id;
    	this.displayName = testBed.displayName;
    	this.energisAsset = testBed.energisAsset;
    	this.energisDashboard = testBed.energisDashboard;
    	this.renewablePercentages = testBed.renewablePercentages;
    	this.penalties = testBed.penalties;
    	this.simulatorOptions = testBed.simulatorOptions;
    }
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEnergisAsset() {
		return energisAsset;
	}

	public void setEnergisAsset(String energisAsset) {
		this.energisAsset = energisAsset;
	}

	public String getEnergisDashboard() {
		return energisDashboard;
	}

	public void setEnergisDashboard(String energisDashboard) {
		this.energisDashboard = energisDashboard;
	}

	public List<Integer> getRenewablePercentages() {
		return renewablePercentages;
	}

	public void setRenewablePercentages(List<Integer> renewablePercentages) {
		this.renewablePercentages = renewablePercentages;
	}

	public List<Double> getPenalties() {
		return penalties;
	}

	public void setPenalties(List<Double> penalties) {
		this.penalties = penalties;
	}

	public String getSimulatorOptions() {
		return simulatorOptions;
	}

	public void setSimulatorOptions(String simulatorOptions) {
		this.simulatorOptions = simulatorOptions;
	}
    
	public TestBed withoutInternals() {
		TestBed copy = new TestBed(this);
		copy.setSimulatorOptions(null);
		return copy;
	}
	
}
