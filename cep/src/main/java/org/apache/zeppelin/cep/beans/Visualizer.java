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
    DataHolderBean dataHolderBean;
    public Visualizer(EventProcessor eventProcessor){
        this.eventProcessor = eventProcessor;
        dataHolderBean = DataHolderBean.

    }

    public String generateCode(){
        StringBuilder sb = new StringBuilder();
        int i=0;
        sb.append("\"\"\"");
        sb.append()
        for(Object[] objects : attributeValueList){

        }
    }
}
