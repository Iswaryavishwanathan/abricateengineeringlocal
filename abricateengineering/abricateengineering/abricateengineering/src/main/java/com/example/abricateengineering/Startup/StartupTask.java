package com.example.abricateengineering.Startup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupTask implements ApplicationRunner {

    @Autowired
    private SendDataToServer sendDataToServer; // Injected instead of manually creating

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Application has started. Executing ApplicationRunner.");
        
        try {
            sendDataToServer.getServerDataLastUpdate();
            sendDataToServer.sendResponseToServer();
        } catch (Exception e) {
            System.err.println("An error occurred during startup tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
