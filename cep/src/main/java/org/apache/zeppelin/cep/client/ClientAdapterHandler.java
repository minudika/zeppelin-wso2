package org.apache.zeppelin.cep.client;

/**
 * Created by minudika on 1/6/17.
 */

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

;

public class ClientAdapterHandler extends
        SimpleChannelInboundHandler<String> {

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.err.println("client -> "+s);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext arg0)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0)
            throws Exception {
        // TODO Auto-generated method stub

    }

}
