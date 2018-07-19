package info.quandoo.rohit.model;

import com.google.gson.annotations.SerializedName;


public class TableResponse {


    private int table_Id;  //Table Id is considered as Table Number
    private Boolean isAvailible;
    private int custId;

    public int getTable_Id() {
        return table_Id;
    }

    public void setTable_Id(int table_Id) {
        this.table_Id = table_Id;
    }

    public Boolean getAvailible() {
        return isAvailible;
    }

    public void setAvailible(Boolean availible) {
        isAvailible = availible;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public TableResponse(int table_Id, Boolean isAvailible, int custId) {
        this.table_Id = table_Id;
        this.isAvailible = isAvailible;
        this.custId = custId;
    }
    public TableResponse() {
    }
}
