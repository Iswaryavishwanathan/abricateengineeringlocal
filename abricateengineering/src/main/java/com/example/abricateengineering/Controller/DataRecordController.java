package com.example.abricateengineering.Controller;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.abricateengineering.DAO.FormattedDataDAO;
import com.example.abricateengineering.DAO.MaterialReport;
import com.example.abricateengineering.DAO.RecipeConsompsion;
import com.example.abricateengineering.Service.DataRecordService;

@RestController
@RequestMapping("/api/data")
public class DataRecordController {

    private final DataRecordService dataRecordService;

    public DataRecordController(DataRecordService dataRecordService) {
        this.dataRecordService = dataRecordService;
    }

    @GetMapping("/material-report")
    public List<MaterialReport> getMaterialReportsBetweenDates(
            @RequestParam("startDate") String startDate, 
            @RequestParam("endDate") String endDate) {
        return dataRecordService.getMaterialReportBtwnReports(startDate, endDate);
    }

    @GetMapping("/recipe-consumptions")
    public List<RecipeConsompsion> getRecipeConsompsions(
        @RequestParam("startDate") String startDate, 
        @RequestParam("endDate") String endDate) {
        return dataRecordService.getRecipeConsompsions(startDate, endDate);
    }
    @GetMapping("/formattedData")
    public  FormattedDataDAO getFormattedData(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return dataRecordService.getFormattedData(startDate, endDate);
    }
    //  @GetMapping("/formattedData")
    // public ResponseEntity<FormattedDataDAO> getFormattedData(
    //         @RequestParam String startDateStr,
    //         @RequestParam String endDateStr,
    //         @RequestParam String selectedFormula) {

    //     try {
    //         FormattedDataDAO formattedData = dataRecordService.getFormattedData(startDateStr, endDateStr, selectedFormula);
    //         return ResponseEntity.ok(formattedData);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    //     }
    // }
    // @GetMapping("/getAvailableFormulas")
    // public List<String> getAvailableFormulas(
    //         @RequestParam String startDate,
    //         @RequestParam String endDate) {
    //     return dataRecordService.getAvailableFormulas(startDate, endDate);
    // }
    // @GetMapping("/getDataForFormula")
    // public FormattedDataDAO getFormattedData(
    //         @RequestParam String startDate,
    //         @RequestParam String endDate,
    //         @RequestParam String selectedFormula) {
    //     return dataRecordService.getFormattedData(startDate, endDate, selectedFormula);
    // }
}
    
