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

public class SimulationParameters {
	
    private String testBedId;
    private Integer renewablePercentage;
    private Double penalty;
    private String dayProfile;
    private String startDate;

	public String getTestBedId() {
		return testBedId;
	}

	public void setTestBedId(String testBedId) {
		this.testBedId = testBedId;
	}

	public Integer getRenewablePercentage() {
		return renewablePercentage;
	}

	public void setRenewablePercentage(Integer renewablePercentage) {
		this.renewablePercentage = renewablePercentage;
	}

	public Double getPenalty() {
		return penalty;
	}

	public void setPenalty(Double penalty) {
		this.penalty = penalty;
	}

	public String getDayProfile() {
		return dayProfile;
	}

	public void setDayProfile(String dayProfile) {
		this.dayProfile = dayProfile;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
    
}
