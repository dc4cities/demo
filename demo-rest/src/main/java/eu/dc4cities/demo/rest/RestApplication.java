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

import eu.dc4cities.demo.rest.custom.*;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Torben on 08.07.2015.
 */
@ApplicationPath("rest")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();

        // Services for data for demos
        s.add(DayProfileService.class);
        s.add(TestBedService.class);
        s.add(ActivityService.class);

        // Services for demos
        s.add(CustomDemoService.class);

        return s;
    }

}