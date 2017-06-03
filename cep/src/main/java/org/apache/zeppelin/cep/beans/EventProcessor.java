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
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.stream.output.StreamCallback;
import org.wso2.siddhi.query.api.definition.Attribute;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    InterpreterDataHolder interpreterDataHolder;
    ArrayList<Object> outputData;
    ExecutionPlanRuntime executionPlanRuntime;
    String[] outputStreamAttributeNames;
    ArrayList<Attribute.Type> outputStreamAttributeTypes;
    ArrayList<Object[]> attribtueValues;
    List<Attribute> outputStreamAttributeList;

    public EventProcessor(){
        interpreterDataHolder = InterpreterDataHolder.getInterpreterDataHolder();
        this.streamDefinition = interpreterDataHolder.getStreamDefinition();
        this.query = interpreterDataHolder.getQuery();
        SiddhiManager siddhiManager = new SiddhiManager();
        executionPlanRuntime = siddhiManager.createExecutionPlanRuntime(streamDefinition + query);
    }

    public void publish(String streamName) throws InterruptedException {
        outputStreamAttributeNames = executionPlanRuntime.getStreamDefinitionMap()
                .get(streamName).getAttributeNameArray();

        outputStreamAttributeList = executionPlanRuntime.getStreamDefinitionMap()
                .get(streamName).getAttributeList();

        for(Attribute attribute : outputStreamAttributeList){
            outputStreamAttributeTypes.add(attribute.getType());
        }

        executionPlanRuntime.addCallback(streamName, new StreamCallback() {
            @Override
            public void receive(Event[] events) {
                for(Event event : events){
                    attribtueValues.add(event.getData());
                }
            }
        });

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(interpreterDataHolder.getInputDataFile()));
            InputHandler inputHandler = executionPlanRuntime.getInputHandler(interpreterDataHolder.getStreamName());

            String line = "";
            while((line = bufferedReader.readLine()) != null){
                String[] data = line.trim().split(",");
                Object[] objects = new Object[data.length];
                int i=0;
                for(String d : data){
                    objects[i] = convertAttribute(d, outputStreamAttributeTypes.get(i));
                    i++;
                }
                inputHandler.send(objects);
                Thread.sleep(10);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread.sleep(100);
        executionPlanRuntime.shutdown();
    }

    public void publish2(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(interpreterDataHolder.getInputDataFile()));
            InputHandler inputHandler = executionPlanRuntime.getInputHandler(interpreterDataHolder.getStreamName());

            String line = "";
            while((line = bufferedReader.readLine()) != null){
                String[] data = line.trim().split(",");
                Object[] objects = new Object[data.length];
                int i=0;
                for(String d : data){
                    objects[i] = convertAttribute(d, outputStreamAttributeTypes.get(i));
                    i++;
                }
                inputHandler.send(objects);
                Thread.sleep(10);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Object convertAttribute(String attributeValue, Attribute.Type type){
        switch (type){
            case INT:
                return Integer.parseInt(attributeValue);
            case LONG:
                return Long.parseLong(attributeValue);
            case FLOAT:
                return Float.parseFloat(attributeValue);
            case DOUBLE:
                return Double.parseDouble(attributeValue);
            case BOOL:
                return Boolean.parseBoolean(attributeValue);
            default:
                return attributeValue;
        }
    }

    public ArrayList<Object[]> getAttributeValueList(){
        return attribtueValues;
    }
}
