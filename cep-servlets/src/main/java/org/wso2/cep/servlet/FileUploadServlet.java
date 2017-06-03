package org.wso2.cep.servlet;/*
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

/**
 * Created by minudika on 13/4/17.
 */

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.wso2.cep.beans.DataHolderBean;
import org.wso2.cep.server.Server;
import org.wso2.cep.client.Client;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FileUploadServlet extends HttpServlet {

    private boolean isMultipart;
    private String filePath = "/home/minudika/uploads/";
    private int maxFileSize = 50 * 1024;
    private int maxMemSize = 4 * 1024;
    private File file ;
    private DataHolderBean dataHolderBean;
    private Client client;

    public void init( ){
        // Get the file location where it would be stored.
        System.err.println("************************************************servlet checked");
        /*filePath =
                getServletContext().getInitParameter("file-upload");*/
        final Server server = new Server();
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.start();
            }
        }).start();

    }
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {

        dataHolderBean = DataHolderBean.getDataHolderBean();
        dataHolderBean.clear();
        // Check that we have a file upload request
        isMultipart = ServletFileUpload.isMultipartContent(request);
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter( );
        if( !isMultipart ){
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet upload</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>No file uploaded</p>");
            out.println("</body>");
            out.println("</html>");
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);
        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("../../../../tmp"));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax( maxFileSize );

        try{
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet upload</title>");
            out.println("</head>");
            out.println("<body>");

            while ( i.hasNext () )
            {
                FileItem fi = (FileItem)i.next();
                if ( !fi.isFormField () )
                {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();
                    // Write the file
                    if( fileName.lastIndexOf("\\") >= 0 ){
                        file = new File( filePath +
                                fileName.substring( fileName.lastIndexOf("\\"))) ;
                    }else{
                        file = new File( filePath +
                                fileName.substring(fileName.lastIndexOf("\\")+1)) ;
                    }
                    fi.write( file ) ;
                    out.println("Uploaded Filename: " + fileName + "<br>");
                } else{
                    setStreamAttributes(fi);
                }
            }
            String server = "localhost";
            int port = 5253;
            int containerPort = 8095;
            new Client(server, port, containerPort).send();

            out.println("</body>");
            out.println("</html>");
            //response.sendRedirect(request.getRequestURI());

        }catch(Exception ex) {
            System.err.println(ex);
        }
    }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

        System.err.println("iframe checked");
    }

    private void setStreamAttributes(FileItem item){
        String name = item.getFieldName();
        String value = item.getString();

        System.err.println("field name : "+name+" , value : "+value);

        if(DataHolderBean.STREAM_NAME_IDENTIFIER.equals(name)){
            dataHolderBean.setStreamName(value);
        } else if(name.contains(DataHolderBean.STREAM_ATTRIBUTE_NAME_PREFIX)){
            dataHolderBean.addStramAttributeName(value);
        } else if(name.contains(DataHolderBean.STREAM_ATTRIBUTE_TYPE_PREFIX)){
            dataHolderBean.addStramAttributeType(value);
        }
    }


}
