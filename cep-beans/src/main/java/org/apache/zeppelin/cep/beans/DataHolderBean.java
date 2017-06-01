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

import java.util.ArrayList;

/**
 * Created by minudika on 13/4/17.
 */
public class DataHolderBean {
    private ArrayList<String> streamAttributeNames;
    private ArrayList<String> streamAttributeTypes;

    private String streamName;
    public static String  STREAM_NAME_IDENTIFIER = "streamName";
    public static String  STREAM_ATTRIBUTE_NAME_PREFIX = "attributeName";
    public static String STREAM_ATTRIBUTE_TYPE_PREFIX = "select";

    public DataHolderBean(){
        streamAttributeNames = new ArrayList<String>();
        streamAttributeTypes = new ArrayList<String>();
    }

    public void setStreamName(String streamName){
        this.streamName = streamName;
    }

    public String getStreamName(){
        return streamName;
    }

    public ArrayList<String> getStreamAttributeNames(){
        return streamAttributeNames;
    }

    public void addStramAttributeName(String attributeName){
        streamAttributeNames.add(attributeName);
    }

    public ArrayList<String> getStreamAttributeTypes(){
        return streamAttributeTypes;
    }

    public void addStramAttributeType(String attributeType){
        streamAttributeTypes.add(attributeType);
    }
}
