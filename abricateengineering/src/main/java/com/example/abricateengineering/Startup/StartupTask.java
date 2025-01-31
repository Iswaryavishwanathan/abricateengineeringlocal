package com.example.abricateengineering.Startup;

import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.abricateengineering.DAO.DataRecordDAO;
import com.example.abricateengineering.Service.DataRecordService;
import java.time.LocalDateTime;
import java.util.List;

// Added by Abinav on 21/01/2025
@Component
public class StartupTask implements ApplicationRunner {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DataRecordService dataRecordService;

    private String dateTimeString = null;

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Application has started. Executing ApplicationRunner.");

        try {
            getServerDataLastUpdate();
            sendResponseToServer();
        } catch (Exception e) {
            System.err.println("An error occurred during startup tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getServerDataLastUpdate() {
        try {
            String apiUrl = "http://localhost:8082/api/data/latest"; // Replace with your API URL
            dateTimeString = restTemplate.getForObject(apiUrl, String.class);
            System.out.println("Fetched last update time from server: " + dateTimeString);
        } catch (RestClientException e) {
            System.err.println("Error while fetching last update time from server: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error in getServerDataLastUpdate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendResponseToServer() {
        try {
            String currentTime = LocalDateTime.now().toString();
            List<DataRecordDAO> dataRecordDAOs = dataRecordService.getDataAfterThisDate(dateTimeString, currentTime);
            String apiUrl = "http://localhost:8082/api/data/receive-data"; // Replace with your API URL

            restTemplate.postForObject(apiUrl, dataRecordDAOs, DataRecordDAO.class);
            // ObjectMapper objectMapper = new ObjectMapper();
            // try {
            //     String jsonPayload = objectMapper.writeValueAsString(dataRecordDAOs);
            //     System.out.println("Payload to send: " + jsonPayload);
            // } catch (JsonProcessingException e) {
            //     e.printStackTrace();
            // }

            System.out.println("Successfully sent data records to server.");
        } catch (RestClientException e) {
            System.err.println("Error while sending data records to server: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error in sendResponseToServer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
