package com.clodrock.sakabe.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class SocketIOConfig {
    @Value("${socket.port:8090}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer() throws UnknownHostException {
        InetAddress locIP = InetAddress.getByName("127.0.0.1");
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(locIP.getHostName());
        config.setPort(port);
        return new SocketIOServer(config);
    }
}