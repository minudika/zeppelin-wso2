/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.zeppelin.cep.beans;

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.query.api.ExecutionPlan;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.compiler.SiddhiCompiler;

import java.util.HashMap;

/**
 * Created by minudika on 13/4/17.
 */
public class EventProcessor {
    static final Logger log = Logger.getLogger(EventProcessor.class);
    public int count = 0;
    public String flag = "none";
    public HashMap<String,String> registeredStreams;
    private SiddhiManager siddhiManager;

    public EventProcessor(){
        siddhiManager = new SiddhiManager();
        registeredStreams = new HashMap<String, String>();
    }
    public void run(String executionPlan) throws InterruptedException {
        String inStreamDefinition = "" +
                "define stream inputStream (symbol string, price string, volume string);";
        String query = ("@info(name = 'query1') " +
                "from inputStream select symbol "+
                "insert into outputStream;");
        ExecutionPlanRuntime executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(executionPlan);

        executionPlanRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);


                for(int cnt=0;cnt<inEvents.length;cnt++){
                    count++;
                    log.info("Event : " + count + ",currentDate : " + inEvents[cnt].getData(1));
                }
            }
        });


        String params[] = null;
        InputHandler inputHandler = executionPlanRuntime.getInputHandler("inputStream");
        executionPlanRuntime.start();
        flag = params[0];
        inputHandler.send(new Object[]{params[0], 700f, 100l});
        Thread.sleep(100);
        executionPlanRuntime.shutdown();
    }

    public void registerStream(String streamID, String streamDef){
        registeredStreams.put(streamID,streamDef);
    }
}
