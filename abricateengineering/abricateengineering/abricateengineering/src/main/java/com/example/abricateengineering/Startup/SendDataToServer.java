package com.example.abricateengineering.Startup;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.example.abricateengineering.DAO.DataRecordDAO;
import com.example.abricateengineering.Service.DataRecordService;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class SendDataToServer {

    private final RestTemplate restTemplate;
    private final DataRecordService dataRecordService;
    private String dateTimeString = null;

    public SendDataToServer(RestTemplate restTemplate, DataRecordService dataRecordService) {
        this.restTemplate = restTemplate;
        this.dataRecordService = dataRecordService;
    }

    @Scheduled(fixedDelay = 10000) // Runs every 10 seconds
    public void runTask() {
        try {
            getServerDataLastUpdate();
            sendResponseToServer();
        } catch (Exception e) {
            System.err.println("Error in scheduled task: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void getServerDataLastUpdate() {
        try {
            // String apiUrl = "http://13.202.101.254:8082/api/data/latest"; 
            String apiUrl = "http://localhost:8082/api/data/latest"; 
            dateTimeString = restTemplate.getForObject(apiUrl, String.class);
            System.out.println("Fetched last update time from server: " + dateTimeString);
        } catch (RestClientException e) {
            System.err.println("Error while fetching last update time from server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendResponseToServer() {

        try {
            if(!dataRecordService.verifyLastUpdateDate(dateTimeString)){
            String currentTime = LocalDateTime.now().toString();
            List<DataRecordDAO> dataRecordDAOs = dataRecordService.getDataAfterThisDate(dateTimeString, currentTime);
            // String apiUrl = "http://13.202.101.254:8082/api/data/receive-data"; 
              String apiUrl = "http://localhost:8082/api/data/receive-data"; 
            System.out.println("Data to be sent to server:");
            for (DataRecordDAO record : dataRecordDAOs) {
                System.out.println(record); 
            }

            restTemplate.postForObject(apiUrl, dataRecordDAOs, DataRecordDAO.class);
            System.out.println("Successfully sent data records to server.");
            }
            else{
                System.out.println("data is uptodate in server");
            }
        } catch (RestClientException e) {
            System.err.println("Error while sending data records to server: " + e.getMessage());
            e.printStackTrace();
        }
    }
  
}
