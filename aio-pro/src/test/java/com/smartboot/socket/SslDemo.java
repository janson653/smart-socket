/*******************************************************************************
 * Copyright (c) 2017-2020, org.smartboot. All rights reserved.
 * project name: smart-socket
 * file name: SslDemo.java
 * Date: 2020-04-16
 * Author: sandao (zhengjunweimail@163.com)
 *
 ******************************************************************************/

package com.smartboot.socket;

import org.smartboot.socket.extension.plugins.TlsPlugin;
import org.smartboot.socket.extension.tls.ClientAuth;
import org.smartboot.socket.extension.tls.TlsConfig;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioQuickServer;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author 三刀
 * @version V1.0 , 2020/4/16
 */
public class SslDemo {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        IntegerServerProcessor serverProcessor = new IntegerServerProcessor();
        AioQuickServer sslQuickServer = new AioQuickServer(8080, new IntegerProtocol(), serverProcessor);

        TlsConfig serverConfig = new TlsConfig();
        serverConfig.setClientAuth(ClientAuth.NONE);
        serverConfig.setKeyPassword("123456");
        serverConfig.setKeyFile(SslDemo.class.getClassLoader().getResourceAsStream("server.keystore"));
        serverConfig.setKeystorePassword("123456");
        serverConfig.setTrustFile(SslDemo.class.getClassLoader().getResourceAsStream("server.keystore"));
        serverConfig.setTrustPassword("123456");
        serverProcessor.addPlugin(new TlsPlugin(serverConfig));
        sslQuickServer.start();

        IntegerClientProcessor clientProcessor = new IntegerClientProcessor();
        AioQuickClient sslQuickClient = new AioQuickClient("localhost", 8080, new IntegerProtocol(), clientProcessor);
        TlsConfig clientConfig = new TlsConfig();
        clientConfig.setClientMode(true);
        clientConfig.setKeyPassword("123456");
        clientConfig.setKeyFile(SslDemo.class.getClassLoader().getResourceAsStream("server.keystore"));
        clientConfig.setKeystorePassword("123456");
        clientConfig.setTrustFile(SslDemo.class.getClassLoader().getResourceAsStream("server.keystore"));
        clientConfig.setTrustPassword("123456");
        clientProcessor.addPlugin(new TlsPlugin(clientConfig));
        AioSession aioSession = sslQuickClient.start();
        aioSession.writeBuffer().writeInt(1);
        aioSession.writeBuffer().flush();

    }
}
