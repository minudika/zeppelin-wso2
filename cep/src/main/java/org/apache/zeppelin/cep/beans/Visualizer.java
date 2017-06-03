package org.apache.zeppelin.cep.beans;

import org.wso2.siddhi.query.api.definition.Attribute;

import java.util.ArrayList;

/**
 * Created by minudika on 1/6/17.
 */
public class Visualizer {
    EventProcessor eventProcessor;
    ArrayList<Object[]> attributeValueList;
    ArrayList<String> attributeNames;
    InterpreterDataHolder interpreterDataHolder;
    public Visualizer(EventProcessor eventProcessor){
        this.eventProcessor = eventProcessor;
        interpreterDataHolder = InterpreterDataHolder.getInterpreterDataHolder();

    }

    public String generateCode(){
        StringBuilder sb = new StringBuilder();
        int i=0;
        sb.append("\"\"\"");
        for(i=0;i<eventProcessor.outputStreamAttributeNames.length-1;i++){
            sb.append(eventProcessor.outputStreamAttributeNames[i]).append("\t");
        }
        sb.append(eventProcessor.outputStreamAttributeNames[i]).append("\n");
        for(Object[] objects : attributeValueList){
            for(Object object : objects){
                sb.append(object.toString()).append("\t");
            }
            sb.replace(sb.length()-1,sb.length(),"\n");
        }
        sb.append("\"\"\"");
        return sb.toString();
    }
}
