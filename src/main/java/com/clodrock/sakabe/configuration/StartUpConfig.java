package com.clodrock.sakabe.configuration;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartUpConfig implements CommandLineRunner {
    private final SocketIOServer socketIOServer;

    public StartUpConfig(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @Override
    public void run(String... args) {
        socketIOServer.start();
    }
}
