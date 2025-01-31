package com.example.abricateengineering.Service;

import org.springframework.stereotype.Service;
import com.example.abricateengineering.DAO.DataRecordDAO;
import com.example.abricateengineering.DAO.FormattedDataDAO;
import com.example.abricateengineering.DAO.DropDownDetails;
import com.example.abricateengineering.DAO.MaterialReport;
import com.example.abricateengineering.DAO.RecipeConsompsion;
import com.example.abricateengineering.DAO.RowRecordDAO;
import com.example.abricateengineering.Repository.DataRecordRepository;
import com.example.abricateengineering.entity.DataRecord;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class DataRecordService {
  

    private final DataRecordRepository dataRecordRepository;
    private final DataDAOService dataDAOService;
    private final MaterialReportService materialReportService;

    private List<MaterialReport> materialReports;
    private List<RecipeConsompsion> recipeConsompsions;

    public DataRecordService(DataRecordRepository dataRecordRepository, DataDAOService dataDAOService, MaterialReportService materialReportService) {
        this.dataRecordRepository = dataRecordRepository;
        this.dataDAOService = dataDAOService;
        this.materialReportService = materialReportService;
    }

    public List<MaterialReport> getMaterialReportBtwnReports(String startDateStr, String endDateStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateStr, dateFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateStr, dateFormatter);

        List<DataRecordDAO> dataRecordDAOs = new ArrayList<>();
        List<DataRecord> dataRecords = dataRecordRepository.findAllByDateTimeBetween(startDateTime, endDateTime);
        for (DataRecord record : dataRecords) {
            dataRecordDAOs.add(dataDAOService.convertToDAO(record));
        }

        materialReports = new ArrayList<>();
        materialReportService.processMaterialReports(dataRecordDAOs, materialReports);
        return materialReports;
    }

    public List<RecipeConsompsion> getRecipeConsompsions(String startDateStr, String endDateStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateStr, dateFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateStr, dateFormatter);

        List<DataRecordDAO> dataRecordDAOs = new ArrayList<>();
        List<DataRecord> dataRecords = dataRecordRepository.findAllByDateTimeBetween(startDateTime, endDateTime);
        for (DataRecord record : dataRecords) {
            dataRecordDAOs.add(dataDAOService.convertToDAO(record));
        }

        recipeConsompsions = new ArrayList<>();
        for (DataRecordDAO dataRecordDAO : dataRecordDAOs) {
            processRecipeConsumption(dataRecordDAO, recipeConsompsions);
        }
        return recipeConsompsions;
    }

    private void processRecipeConsumption(DataRecordDAO dataRecordDAO, List<RecipeConsompsion> recipeConsompsions) {
        String formulaName = dataRecordDAO.getFormula();
        if (!isRecipeConsompsion(formulaName, recipeConsompsions)) {
            recipeConsompsions.add(new RecipeConsompsion(formulaName, new ArrayList<>()));
        }
        int index = getIndexValue(formulaName, recipeConsompsions);
        materialReportService.processSingleMaterialReport(dataRecordDAO, recipeConsompsions.get(index).getMaterialReport());
    }

    private boolean isRecipeConsompsion(String formulaName, List<RecipeConsompsion> recipeConsompsions) {
        for (RecipeConsompsion recipeConsompsion : recipeConsompsions) {
            if (recipeConsompsion.getFormulaName().equals(formulaName)) {
                return true;
            }
        }
        return false;
    }

    private int getIndexValue(String formulaName, List<RecipeConsompsion> recipeConsompsions) {
        for (int i = 0; i < recipeConsompsions.size(); i++) {
            if (recipeConsompsions.get(i).getFormulaName().equals(formulaName)) {
                return i;
            }
        }
        return -1;
    }
    // public FormattedDataDAO getFormattedData(String startDateStr, String endDateStr) {
    //     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    //     LocalDateTime startDateTime = LocalDateTime.parse(startDateStr, dateFormatter);
    //     LocalDateTime endDateTime = LocalDateTime.parse(endDateStr, dateFormatter);
    
    //     List<DataRecord> originalRecords = dataRecordRepository.findAllByDateTimeBetween(startDateTime, endDateTime);
    //     List<RowRecordDAO> formattedRecords = new ArrayList<>();
    //     List<DropDownDetails> dropDownDetailsList = new ArrayList<>();
    
    //     Float[] previousTValues = new Float[20];
    //     Arrays.fill(previousTValues, null);
    
    //     String previousFormula = null;  // To keep track of the previous formula
    
    //     for (DataRecord currentRecord : originalRecords) {
    //         LocalDateTime dateTime = currentRecord.getDateTime();
    //         String formula = currentRecord.getFormula();
    
    //         // Collect material names for this formula
    //         List<String> materialNamesList = new ArrayList<>();
    //         for (int j = 0; j < 20; j++) {
    //             try {
    //                 String materialNameField = "n" + String.format("%02d", j + 1);
    //                 Field materialField = DataRecord.class.getDeclaredField(materialNameField);
    //                 materialField.setAccessible(true);
    //                 String materialName = (String) materialField.get(currentRecord);
    
    //                // Add the material name to the list, even if it's empty or null
    //     materialNamesList.add(materialName != null ? materialName : "");
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //             }
    //         }
    
    //         // Add the formula and material names to the list only if it's not already added
    //         boolean formulaExists = false;
    //         for (DropDownDetails details : dropDownDetailsList) {
    //             if (details.getFormulaName().equals(formula)) {
    //                 formulaExists = true;
    //                 break;
    //             }
    //         }
    
    //         if (!formulaExists) {
    //             // Add a new DropDownDetails only if the formula is not already present
    //             dropDownDetailsList.add(new DropDownDetails(formula, materialNamesList));
    //         }
    
    //         // Process T values and achieved weights...
    //         Float[] tValues = new Float[20];
    //         for (int j = 0; j < 20; j++) {
    //             tValues[j] = currentRecord.getTValue(j);
    //         }
    
    //         // Process achieved weights
    //         Float[] achievedWeights = new Float[20];
    //         for (int j = 0; j < 20; j++) {
    //             try {
    //                 String achievedWeightField = "a" + String.format("%02d", j + 1);
    //                 Field weightField = DataRecord.class.getDeclaredField(achievedWeightField);
    //                 weightField.setAccessible(true);
    //                 achievedWeights[j] = (Float) weightField.get(currentRecord);
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //             }
    //         }
    
    //         // Check if T values changed...
    //         boolean setValueChanged = false;
    //         for (int j = 0; j < 20; j++) {
    //             if (!Objects.equals(previousTValues[j], tValues[j])) {
    //                 setValueChanged = true;
    //                 break;
    //             }
    //         }
    
    //         // If the formula name changes or T values change, add the "setWeight" record
    //         if (!formula.equals(previousFormula) || setValueChanged) {
    //             List<Float> filteredTValuesList = new ArrayList<>();
    //             for (Float value : tValues) {
    //                 if (value != null) {
    //                     filteredTValuesList.add(value);
    //                 }
    //             }
    //             Float[] filteredTValues = filteredTValuesList.toArray(new Float[0]);
    //             formattedRecords.add(new RowRecordDAO(dateTime, "setWeight", formula, filteredTValues));
    
    //             // Update previousFormula to the current one
    //             previousFormula = formula;
    //         }
    
    //         // Add "achWeight" row...
    //         List<Float> filteredAValuesList = new ArrayList<>();
    //         for (Float value : achievedWeights) {
    //             if (value != null) {
    //                 filteredAValuesList.add(value);
    //             }
    //         }
    //         Float[] filteredAValues = filteredAValuesList.toArray(new Float[0]);
    //         formattedRecords.add(new RowRecordDAO(dateTime, "achWeight", formula, filteredAValues));
    
    //         previousTValues = tValues.clone();
    //     }
    
    //     return new FormattedDataDAO(dropDownDetailsList, formattedRecords);
    // }
  
    public FormattedDataDAO getFormattedData(String startDateStr, String endDateStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateStr, dateFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateStr, dateFormatter);
    
        List<DataRecord> originalRecords = dataRecordRepository.findAllByDateTimeBetween(startDateTime, endDateTime);
        List<RowRecordDAO> formattedRecords = new ArrayList<>();
        List<DropDownDetails> dropDownDetailsList = new ArrayList<>();
    
        // Map to track previous T values for each formula
        Map<String, Float[]> previousFormulaTValuesMap = new HashMap<>();
    
        for (DataRecord currentRecord : originalRecords) {
            LocalDateTime dateTime = currentRecord.getDateTime();
            String formula = currentRecord.getFormula();
    
            // Collect material names for this formula
            List<String> materialNamesList = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                try {
                    String materialNameField = "n" + String.format("%02d", j + 1);
                    Field materialField = DataRecord.class.getDeclaredField(materialNameField);
                    materialField.setAccessible(true);
                    String materialName = (String) materialField.get(currentRecord);
                    materialNamesList.add(materialName != null ? materialName : "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    
            // Add formula to drop-down list if not already added
            boolean formulaExists = false;
            for (DropDownDetails details : dropDownDetailsList) {
                if (details.getFormulaName().equals(formula)) {
                    formulaExists = true;
                    break;
                }
            }
            if (!formulaExists) {
                dropDownDetailsList.add(new DropDownDetails(formula, materialNamesList));
            }
    
            // Get T-values from current record
            Float[] tValues = new Float[20];
            for (int j = 0; j < 20; j++) {
                tValues[j] = currentRecord.getTValue(j);
            }
    
            // Get achieved weights
            Float[] achievedWeights = new Float[20];
            for (int j = 0; j < 20; j++) {
                try {
                    String achievedWeightField = "a" + String.format("%02d", j + 1);
                    Field weightField = DataRecord.class.getDeclaredField(achievedWeightField);
                    weightField.setAccessible(true);
                    achievedWeights[j] = (Float) weightField.get(currentRecord);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    
            // Check if T-values have changed for this formula
            boolean setValueChanged = false;
            Float[] previousTValues = previousFormulaTValuesMap.get(formula);
    
            if (previousTValues == null) {
                setValueChanged = true; // First occurrence of formula, treat as a change
            } else {
                for (int j = 0; j < 20; j++) {
                    if (!Objects.equals(previousTValues[j], tValues[j])) {
                        setValueChanged = true;
                        break;
                    }
                }
            }
    
            // Add "setWeight" row only if T-values changed for this formula
            if (setValueChanged) {
                List<Float> filteredTValuesList = new ArrayList<>();
                for (int j = 0; j < 20; j++) {
                    if (tValues[j] != null) {
                        filteredTValuesList.add(tValues[j]);
                    }
                }
                Float[] filteredTValues = filteredTValuesList.toArray(new Float[0]);
    
                formattedRecords.add(new RowRecordDAO(dateTime, "setWeight", formula, filteredTValues));
    
                // Update previous T-values for the formula
                previousFormulaTValuesMap.put(formula, tValues.clone());
            }
    
            // Always add "achWeight" row
            List<Float> filteredAValuesList = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                if (achievedWeights[j] != null) {
                    filteredAValuesList.add(achievedWeights[j]);
                }
            }
            Float[] filteredAValues = filteredAValuesList.toArray(new Float[0]);
    
            formattedRecords.add(new RowRecordDAO(dateTime, "achWeight", formula, filteredAValues));
        }
    
        return new FormattedDataDAO(dropDownDetailsList, formattedRecords);
    }
    

    

    
    
    
    
    // //added by abinav on  22/01/2025
    public List<DataRecordDAO> getDataAfterThisDate(String startDateStr,String endDateStr){
        LocalDateTime startDateTime =null;
        LocalDateTime endDateTime =null;
        
        try {
            // Include optional nanoseconds in the pattern
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.n]");
            startDateTime = LocalDateTime.parse(startDateStr.replace("\"", ""), dateFormatter);
            endDateTime = LocalDateTime.parse(endDateStr.replace("\"", ""), dateFormatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        
        List<DataRecordDAO> dataRecordDAOs = new ArrayList<>();
        List<DataRecord> dataRecords = dataRecordRepository.findAllByDateTimeBetween(startDateTime, endDateTime);
        for (DataRecord record : dataRecords) {
            dataRecordDAOs.add(dataDAOService.convertToDAO(record));
        }
        return dataRecordDAOs;

        
    }
}