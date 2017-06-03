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
import org.apache.zeppelin.cep.server.Server;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class ReceiverInterpreter extends Interpreter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverInterpreter.class);


    public ReceiverInterpreter(Properties property) {
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Server().start();
            }
        }).start();

        System.err.println("**************************Receiver interpreter : interpret() hit");
        LOGGER.info("**************************Receiver interpreter : interpret() hit");

        // EventProcessor eventProcessor = new EventProcessor();
        String html = "";
        String testHtml2 = "<html>\n" +
                "<head>\n" +
                "<title>File Uploading Form</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h3>File Upload:</h3>\n" +
                "Select a file to upload: <br />\n" +
                "<form action=\"uploadservlet\" method=\"post\"\n" +
                "                        enctype=\"multipart/form-data\">\n" +
                "<input type=\"file\" name=\"file\" size=\"50\" />\n" +
                "<br />\n" +
                "<input type=\"submit\" value=\"Upload File\" />\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>" ;

        String testHtml = "<html>\n" +
                "<head>\n" +
                "<title>File Uploading Form</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\t<script type=\"text/javascript\">\n" +
                "  function loadFileAsText(){\n" +
                "  //var content;\n" +
                "  var fileToLoad = document.getElementById(\"uploadedFile\").files[0];\n" +
                "  console.log(\"file selected\"+fileToLoad);\n" +
                "\n" +
                "  var fileReader = new FileReader();\n" +
                "  fileReader.onload = function(fileLoadedEvent){\n" +
                "      var textFromFileLoaded = fileLoadedEvent.target.result;\n" +
                "      console.log(textFromFileLoaded);\n" +
                "      document.getElementById(\"content\").value = textFromFileLoaded;\n" +
                "  };\n" +
                "\n" +
                "  fileReader.readAsText(fileToLoad, \"UTF-8\");\n" +
                "}\n" +
                "\n" +
                "function readData(){\n" +
                "    var input  = document.getElementById('uploadedFile'); // get the input\n" +
                "    var file   = input.files[0];                  // assuming single file, no multiple\n" +
                "    var reader = new FileReader();\n" +
                "\n" +
                "    reader.onload = function(e) {\n" +
                "        var text = reader.result;                 // the entire file\n" +
                "\n" +
                "        var firstLine = text.split('\\n').shift(); // first line \n" +
                "        document.getElementById(\"content\").value = firstLine;\n" +
                "        console.log(firstLine);                   // use the console for debugging\n" +
                "    }\n" +
                "\n" +
                "    reader.readAsText(file, 'UTF-8');             // or whatever encoding you're using\n" +
                "                                                  // UTF-8 is default, so this argument \n" +
                "}      \n" +
                "</script>\n" +
                "<h3>File Upload:</h3>\n" +
                "Select a file to upload: <br />\n" +
                "<form action=\"uploadservlet\" method=\"post\"\n" +
                "                        enctype=\"multipart/form-data\">\n" +
                "<input type=\"file\" id=\"uploadedFile\" name=\"file\" size=\"50\" onchange=\"readData()\"/>\n" +
                "<!-- <input type='button' value='select' onclick='loadFileAsText()'/>\n" +
                " --><br />\n" +
                "<input type=\"submit\" value=\"Upload File\" />\n" +
                "</form>\n" +
                "<input type=\"text\" id=\"content\" value=\"content\">\n" +
                "</body>\n" +
                "</html>";


        StringBuilder sb = new StringBuilder();
        StringBuffer stringBuffer = new StringBuffer();

        String filepath = "/home/minudika/Projects/Apache/zeppelin_0.5.6/zeppelin/cep/src/main/java/org/apache/zeppelin/cep/webpages/uploadfile.html";

        //File file = new File("../resources/webpages/uploadfile.html");
        LOGGER.info("***************************** checked");

        try {
            LOGGER.info("***************************** checked2");

            BufferedReader b = new BufferedReader(new FileReader(filepath));

            LOGGER.info("*****************************Reading file using Buffered Reader");
            String readLine = "";
            while ((readLine = b.readLine()) != null) {
                html += readLine;
                stringBuffer.append(readLine);
                stringBuffer.append('\n');
            }

            //LOGGER.info("************************"+stringBuffer.toString());
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return new InterpreterResult(InterpreterResult.Code.SUCCESS, InterpreterResult.Type.HTML,stringBuffer.toString());
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
