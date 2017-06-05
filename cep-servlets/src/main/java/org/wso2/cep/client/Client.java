package org.wso2.cep.client;

/**
 * Created by minudika on 1/6/17.
 */

import org.wso2.cep.beans.DataHolderBean;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    String server;
    int port;
    int containerPort;

    public Client(String server, int port, int containerPort) {
        this.server = server;
        this.port = port;
        this.containerPort = containerPort;
    }

    public void start() {
        String server = "localhost";
        int port = 5253;
        int containerPort = 8095;
        new Client(server, port, containerPort).send();
    }

    public void send() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientAdapterInitializer());

            Channel channel = bootstrap.connect(server, port).sync().channel();

            System.err.println("servlet client : sending data..");
            channel.write(format("streamName"));
            channel.flush();
            channel.write(format("fileURI"));
            channel.flush();
            channel.write(format("attributeNames"));
            channel.flush();
            channel.write(format("attributeTypes"));
            channel.flush();
            System.err.println("servlet client : sending data finished.");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private String format(String key){
        DataHolderBean dataHolderBean = DataHolderBean.getDataHolderBean();
        StringBuilder sb = new StringBuilder();
        if("streamName".equals(key)){
            sb.append("streamName>").append(dataHolderBean.getStreamName());
        } else if("attributeNames".equals(key)){
            sb.append("attributeNames>");
            for(String s : dataHolderBean.getStreamAttributeNames()){
                sb.append(s).append(">");
            }
        } else if("attributeTypes".equals(key)){
            sb.append("attributeTypes>");
            for(String s : dataHolderBean.getStreamAttributeTypes()){
                sb.append(s).append(">");
            }
        } else if("fileURI".equals(key)){
            sb.append("fileURI>").append(dataHolderBean.getInputDataFile());
        }
        return sb.append("|\n").toString();
    }
}
