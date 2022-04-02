package com.purpuligo.taxation.Model;

public class DataModel {
    String transaction_id, type, financialYear, date, status, name;

    public DataModel(String transaction_id, String type, String financialYear, String date, String status, String name) {
        this.transaction_id = transaction_id;
        this.type = type;
        this.financialYear = financialYear;
        this.date = date;
        this.status = status;
        this.name = name;
    }
    public DataModel(){

    }

    public String getTransaction_id() {
        return transaction_id;
    }
    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getFinancialYear() {
        return financialYear;
    }
    public void setFinancialYear(String financialYear) { this.financialYear = financialYear; }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status = status; }

    public String getName(){ return name;}
    public void setName(String name){ this.name = name; }
}
