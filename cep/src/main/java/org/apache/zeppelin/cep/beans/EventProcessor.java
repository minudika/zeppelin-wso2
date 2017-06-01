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
import org.wso2.cep.beans.DataHolderBean;
import org.wso2.siddhi.core.ExecutionPlanRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.query.api.definition.Attribute;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by minudika on 13/4/17.
 */
public class EventProcessor {
    static final Logger log = Logger.getLogger(EventProcessor.class);
    String streamDefinition;
    String query;
    SiddhiManager siddhiManager;
    int numberOfData = 0;
    DataHolderBean dataHolderBean;
    ArrayList<Object> outputData;
    ExecutionPlanRuntime executionPlanRuntime;
    String[] attributeNames;
    ArrayList<Attribute.Type> attributeTypes;
    ArrayList<Object[]> attribtueValues;
    List<Attribute> attributeList;

    public EventProcessor(){
        dataHolderBean = DataHolderBean.getDataHolderBean();
        this.streamDefinition = dataHolderBean.getStreamDefinition();
        this.query = dataHolderBean.getQuery();
        SiddhiManager siddhiManager = new SiddhiManager();
        executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(streamDefinition + query);
    }

    public void process(String streamName) throws InterruptedException {
        attributeNames = executionPlanRuntime.getStreamDefinitionMap()
                .get(streamName).getAttributeNameArray();

        attributeList = executionPlanRuntime.getStreamDefinitionMap()
                .get(streamName).getAttributeList();
        for(Attribute attribute : attributeList){
            attributeTypes.add(attribute.getType());
        }

        executionPlanRuntime.addCallback(streamName, new StreamCallback() {
            @Override
            public void receive(Event[] events) {
                for(Event event : events){
                    attribtueValues.add(event.getData());
                }
            }
        });

        Thread.sleep(100);
        executionPlanRuntime.shutdown();
    }
}
