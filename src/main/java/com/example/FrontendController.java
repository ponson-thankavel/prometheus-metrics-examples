/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Map;

@Controller
@Description("A controller for handling requests for hello messages")
public class FrontendController {

    @Autowired
    private com.example.FrontendService frontendService;

    //Counter Metrics Declaration
    private final Counter requestCount;

    //Gauge metrics declaration for gauge metrics example 1
    private final Gauge queueSize;

    //Gauge metrics declaration for gauge metrics example 2
    private final Gauge jobWaitQueueSize;

    public FrontendController(CollectorRegistry collectorRegistry) {
        //Counter Metrics requestCount Initialization
        requestCount = Counter.build().name("x_request_count").help("Number of HTTP requests").register(collectorRegistry);
        //Gauge Metrics queueSize Initialization
        queueSize = Gauge.build().name("x_queue_size").help("Number of requests pending in queue").register(collectorRegistry);
        //Gauge Metrics jobWaitQueueSize Initialization
        jobWaitQueueSize = Gauge.build().name("x_job_wait_queue_size").help("Number of jobs waiting in queue").labelNames("gt","lte").register(collectorRegistry);
    }

    @GetMapping("/request")
    @ResponseBody
    public Map<String, String> processRequest() {

        //Incrementing counter metrics on new counter
        requestCount.inc();

        return Collections.singletonMap("message",
                this.frontendService.getMessage("New Request Received"));
    }

    @GetMapping("/queue")
    @ResponseBody
    public Map<String, String> push() {
        //Incrementing queue size metrics on receiving a new item
        queueSize.inc();

        //Code to process the new item

        return Collections.singletonMap("message",
                this.frontendService.getMessage("New Item Pushed to Queue"));
    }

    @GetMapping("/jobs")
    @ResponseBody
    public Map<String, String> complexGauge() {

		//BEGIN Code to fetch # of jobs waiting in each buckets
		//...
		//END Code to fetch # of jobs waiting in each buckets

		//below example sets random counts just as an example. In actual implementation, we should set the actual counts fetched from previous step
		jobWaitQueueSize.labels("0","60").set(Math.round(Math.random()*100));
		jobWaitQueueSize.labels("60","120").set(Math.round(Math.random()*100));
		jobWaitQueueSize.labels("120","300").set(Math.round(Math.random()*100));
		jobWaitQueueSize.labels("300","900").set(Math.round(Math.random()*100));
		jobWaitQueueSize.labels("900","1800").set(Math.round(Math.random()*100));
		jobWaitQueueSize.labels("1800","3600").set(Math.round(Math.random()*100));
		jobWaitQueueSize.labels("3600","7200").set(Math.round(Math.random()*100));
		jobWaitQueueSize.labels("7200","14400").set(Math.round(Math.random()*100));
		jobWaitQueueSize.labels("14400","28800").set(Math.round(Math.random()*100));
		jobWaitQueueSize.labels("28800","+Inf").set(Math.round(Math.random()*100));

        return Collections.singletonMap("message",
                this.frontendService.getMessage("Gauge Metrics for Job Wait Queue is Set"));
    }
}
