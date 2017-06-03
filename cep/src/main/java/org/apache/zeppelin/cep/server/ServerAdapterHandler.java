package org.apache.zeppelin.cep.server;

/**
 * Created by minudika on 1/6/17.
 */
import org.apache.zeppelin.cep.beans.InterpreterDataHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerAdapterHandler extends
        SimpleChannelInboundHandler<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerAdapterHandler.class);


    private static final ChannelGroup channels = new DefaultChannelGroup(
            "containers", GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("interpreter server : [START] New Container has been initialzed");
        channels.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("interpreter server : [END] A Container has been removed");
        channels.remove(ctx.channel());
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1)
            throws Exception {
        InterpreterDataHolder interpreterDataHolder = InterpreterDataHolder.getInterpreterDataHolder();
        String s = arg1.toString();
        LOGGER.info("interpreter server : channelRead -> "+arg1.toString());
        if(s.contains("streamName")){
            interpreterDataHolder.setStreamName(s.trim());
            LOGGER.info("interpreter server : "+ s);
        } else if(s.contains("attributeNames")){
            System.err.println("interpreter server : "+ s);
            String[] names = s.trim().split(">");
            for(int i=1;i<names.length;i++){
                interpreterDataHolder.addStramAttributeName(names[i]);
            }
        } else if(s.contains("attributeTypes")){
            LOGGER.info("interpreter server : "+ s);
            String[] types = s.trim().split(">");
            for(int i=1;i<types.length;i++){
                interpreterDataHolder.addStramAttributeType(types[i]);
            }
        } else if(s.contains("fileURI")){
            LOGGER.info("interpreter server : "+ s);
            interpreterDataHolder.setInputDataFile(s.split(">")[1].trim());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        LOGGER.info("interpreter server : channelRead0 -> "+s);
        InterpreterDataHolder interpreterDataHolder = InterpreterDataHolder.getInterpreterDataHolder();
        /*Channel currentChannel = channelHandlerContext.channel();
        System.err.println("[INFO] - " + currentChannel.remoteAddress() + " - "
                + s);
        currentChannel.write("[Server] - Success");*/

        if(s.contains("streamName")){
            interpreterDataHolder.setStreamName(s.trim());
            LOGGER.info("interpreter server : "+ s);
        } else if(s.contains("attributeNames")){
            System.err.println("interpreter server : "+ s);
            String[] names = s.trim().split(">");
            for(int i=1;i<names.length;i++){
                interpreterDataHolder.addStramAttributeName(names[i]);
            }
        } else if(s.contains("attributeTypes")){
            LOGGER.info("interpreter server : "+ s);
            String[] types = s.trim().split(">");
            for(int i=1;i<types.length;i++){
                interpreterDataHolder.addStramAttributeType(types[i]);
            }
        } else if(s.contains("fileURI")){
            LOGGER.info("interpreter server : "+ s);
            interpreterDataHolder.setInputDataFile(s.split(">")[1].trim());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext arg0)
            throws Exception {
        // TODO Auto-generated method stub
        System.err.println("channelReadComplete");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0)
            throws Exception {
        // TODO Auto-generated method stub
        System.out.println("channelWritabilityChanged");
    }

}
