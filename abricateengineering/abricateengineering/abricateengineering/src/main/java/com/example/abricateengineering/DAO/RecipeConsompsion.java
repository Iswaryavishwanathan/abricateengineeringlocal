package com.example.abricateengineering.DAO;

import java.util.List;

public class RecipeConsompsion {
    private String formulaName;
    private List<MaterialReport> materialReport;
    private int noOfBatches;
  
    public RecipeConsompsion(String formulaName, int noOfBatches,List<MaterialReport> materialReport) {
        this.formulaName = formulaName; 
        this.materialReport = materialReport;
        this.noOfBatches = noOfBatches;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName;
    }

    public List<MaterialReport>getMaterialReport() {
        return materialReport;
    }

    public void setMaterialReport(List<MaterialReport> materialReport) {
        this.materialReport = materialReport;
    }

    public int getNoOfBatches() {
        return noOfBatches;
    }

    public void setNoOfBatches(int noOfBatches) {
        this.noOfBatches = noOfBatches;
    }

}
