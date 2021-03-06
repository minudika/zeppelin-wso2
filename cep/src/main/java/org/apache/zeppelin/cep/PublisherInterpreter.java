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

package org.apache.zeppelin.cep;


/**
 * Created by minudika on 10/4/17.
 */

import org.apache.zeppelin.cep.beans.EventProcessor;
import org.apache.zeppelin.cep.beans.Visualizer;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;

import java.util.Properties;

import sun.awt.X11.Visual;

public class PublisherInterpreter extends Interpreter {

    public PublisherInterpreter(Properties property) {
        super(property);
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public InterpreterResult interpret(String st, InterpreterContext context) {
        EventProcessor eventProcessor = new EventProcessor();
        Visualizer visualizer;
        String tableString = null;
        try {
            eventProcessor.publish(st);
            visualizer = new Visualizer(eventProcessor);
            tableString = visualizer.generateCode();
            logger.info(tableString);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new InterpreterResult(InterpreterResult.Code.SUCCESS, InterpreterResult.Type.TABLE,tableString);
    }

    @Override
    public void cancel(InterpreterContext context) {

    }

    @Override
    public FormType getFormType() {
        return FormType.SIMPLE;
    }

    @Override
    public int getProgress(InterpreterContext context) {
        return 0;
    }
}
