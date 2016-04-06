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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jscience.physics.amount.Amount;

import com.google.gson.Gson;

import eu.dc4cities.configuration.goal.Goal;
import eu.dc4cities.configuration.goal.GoalConfiguration;
import eu.dc4cities.configuration.goal.Objective;
import eu.dc4cities.configuration.goal.Target;
import eu.dc4cities.controlsystem.model.easc.PriceModifier;
import eu.dc4cities.controlsystem.model.json.JsonUtils;
import eu.dc4cities.demo.DemoConfiguration;

@Path("/customdemoservice")
public class CustomDemoService {

	private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
	private static final DemoConfiguration demoConfig = DemoConfiguration.getDemoConfiguration();
	
    @POST
    public Response startSimulator(String body) throws IOException, InterruptedException {
    	System.out.println(body);
    	Gson gson = new Gson();
        SimulationParameters parameters = gson.fromJson(body, SimulationParameters.class);
    	File workDir = new File(demoConfig.getSimulatorWorkDir());
        if (workDir.exists()) {
        	FileUtils.cleanDirectory(workDir);
        } else {
        	workDir.mkdirs();
        }
        List<String> args = prepareArgs(parameters, workDir);
        args.addAll(0, Arrays.asList(new String[] {"java", "-jar", demoConfig.getSimulatorPath()}));
        args.add(new File(workDir, "output").toString());
        System.out.println("Starting simulator with command: " + String.join(" ", args));
        File consoleOutput = new File(workDir, "console.txt");
        Process simulator = 
        		new ProcessBuilder(args.toArray(new String[args.size()]))
                		.directory(workDir)
                        .redirectErrorStream(true)
        				.redirectOutput(consoleOutput)
        				.start();
        simulator.waitFor();
        return Response.status(200).build();
    }

    private List<String> prepareArgs(SimulationParameters parameters, File workDir) throws IOException {
    	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String testBedId = parameters.getTestBedId();
    	String testBedDirName = "testbeds/" + testBedId;
        File testBedDir = new File(classLoader.getResource(testBedDirName).getFile());
        File simEnvDir = new File(demoConfig.getSimulatorWorkDir(), "env");
        FileUtils.copyDirectory(testBedDir, simEnvDir);
        File ctrlBackEndDir = new File(simEnvDir, "ctrl-backend");
        File techConfigFile = new File(ctrlBackEndDir, "technical-configuration.json");
        File goalConfigFile = new File(ctrlBackEndDir, "goal-configuration.json");
        prepareGoalConfig(goalConfigFile, parameters.getRenewablePercentage(), parameters.getPenalty());
        File erdsRootDir = new File(simEnvDir, "erds");
        DateTime startDate = new DateTime(parameters.getStartDate());
        List<String> erdsSources = prepareErdsSources(erdsRootDir, parameters.getDayProfile(), startDate);
        File eascDir = new File(simEnvDir, "easc");
        String eascDirPath = eascDir.getCanonicalPath();
        List<String> args = new ArrayList<String>();
        args.add("--tc");
        args.add(techConfigFile.getCanonicalPath());
        args.add("--gc");
        args.add(goalConfigFile.getCanonicalPath());
        args.add("--eascs");
        args.add(eascDirPath + ":1");
        if (hasSharedEnergyService(eascDir)) {
        	args.add("--eascs-shared-energy-service");
            args.add(eascDirPath);
        }
        for (String erdsSource : erdsSources) {
        	args.add("--source");
        	args.add(erdsSource);
        }
        args.add("--begin");
        args.add(dateFormatter.print(startDate));
        args.add("--end");
        args.add(dateFormatter.print(startDate.plusDays(1)));
        args.add("--escalation-manager");
        args.add("--hdb");
        args.add(demoConfig.getHdbUrl());
        args.add("--energis");
        args.add(demoConfig.getEnergisUrl());
        args.add("--energis-api-key");
        args.add(demoConfig.getEnergisApiKey());
        String simOpts = demoConfig.getTestBed(testBedId).getSimulatorOptions();
        if (simOpts != null) {
        	args.addAll(Arrays.asList(simOpts.split(" ")));
        }
        return args;
    }
    
    private void prepareGoalConfig(File goalConfigFile, int renewablePercentage, double penalty) throws IOException {
    	if (penalty <= 0) {
    		throw new IllegalArgumentException("Penalty must be provided as an absolute non-zero "
    				+ "value (received " + penalty + ")");
    	}
    	GoalConfiguration goalConfig = null;
    	try (InputStream configInputStream = new FileInputStream(goalConfigFile)) {
    		goalConfig = JsonUtils.load(configInputStream, GoalConfiguration.class);
    	}
        for (Goal goal : goalConfig.getGoals()) {
        	for (Objective objective : goal.getObjectives()) {
        		Target target = objective.getTarget();
        		if (target.getMetric().equals("renewablePercentage")) {
        			double originalTargetValue = target.getValue();
        			List<PriceModifier> modifiers = objective.getPriceModifiers();
        			if (modifiers.size() != 2) {
        				throw new IllegalArgumentException("Only objectives with exactly two modifiers are supported");
        			}
        			PriceModifier modifier1 = modifiers.get(0);
        			PriceModifier modifier2 = modifiers.get(1);
        			if (modifier1.getThreshold().getEstimatedValue() != originalTargetValue
        					|| modifier1.getModifier().getEstimatedValue() != 0
        					|| modifier2.getThreshold().getEstimatedValue() != 0) {
        				throw new IllegalArgumentException("Only modifiers with first threshold = target with no "
        						+ "reward and second threshold = 0 are supported");
        			}
        			target.setValue(renewablePercentage);
        			modifier1.setThreshold(Amount.valueOf(renewablePercentage, modifier1.getThreshold().getUnit()));
        			modifier2.setModifier(Amount.valueOf(penalty * -1, modifier2.getModifier().getUnit()));
        		}
        	}
        }
        try (OutputStream configOutputStream = new FileOutputStream(goalConfigFile)) {
        	JsonUtils.save(goalConfig, configOutputStream);
        }
    }

    private List<String> prepareErdsSources(File erdsRootDir, String dayProfile, DateTime startDate) 
    		throws IOException {
    	List<String> erdsSources = new LinkedList<>();
    	for (File dataCenterDir : erdsRootDir.listFiles()) {
    		String dataCenterName = dataCenterDir.getName();
    		for (File erdsDir : dataCenterDir.listFiles()) {
    			String erdsName = erdsDir.getName();
    			boolean profileFound = false;
    			String dayProfileFullName = dayProfile + ".csv";
    			for (File dayProfileFile : erdsDir.listFiles()) {
    				if (dayProfileFile.getName().equals(dayProfileFullName)) {
    					prepareDayProfile(dayProfileFile, startDate);
    					erdsSources.add(dataCenterName + ":" + erdsName + ":" + dayProfileFile.getCanonicalPath());
    					profileFound = true;
    				} else {
    					dayProfileFile.delete();
    				}
    			}
    			if (!profileFound) {
    				throw new IllegalArgumentException(dayProfileFullName + " not found in " + erdsDir);
    			}
    		}
    	}
    	return erdsSources;
    }
    
    private void prepareDayProfile(File dayProfile, DateTime startDay) throws IOException {
    	File tempProfile = new File(dayProfile.getCanonicalPath() + ".temp");
    	// Need to append a full day after the profile day for the forecast to work in the simulator
    	List<String> appendLines = new LinkedList<>();
    	DateTime nextDay = startDay.plusDays(1);
    	try (FileReader fileReader = new FileReader(dayProfile);
    			BufferedReader reader = new BufferedReader(fileReader);
    			FileWriter fileWriter = new FileWriter(tempProfile);
    			BufferedWriter writer = new BufferedWriter(fileWriter)) {
    		String line;
    		while ((line = reader.readLine()) != null) {
    			// Assume CSVs are well-formed with the 10-char date at the beginning
    			writer.write(replaceDate(line, startDay));
    			writer.newLine();
    			appendLines.add(replaceDate(line, nextDay));
    		}
    		appendLines.add(replaceDate(appendLines.get(0), nextDay.plusDays(1)));
    		for (String appendLine : appendLines) {
    			writer.write(appendLine);
    			writer.newLine();
    		}
    	}
    	dayProfile.delete();
    	tempProfile.renameTo(dayProfile);
    }
    
    private String replaceDate(String line, DateTime date) {
    	// Assume line is well-formed with the 10-char date at the beginning
		return dateFormatter.print(date) + line.substring(10);
    }

    private boolean hasSharedEnergyService(File eascDir) {
    	File serverConfigFile = new File(eascDir, "ServerConfig.yaml");
    	return serverConfigFile.exists();
    }
    
}