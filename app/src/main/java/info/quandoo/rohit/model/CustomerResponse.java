package info.quandoo.rohit.model;

import com.google.gson.annotations.SerializedName;


public class CustomerResponse {


    @SerializedName("customerFirstName")
    private String customerFirstName;

    @SerializedName("customerLastName")
    private String customerLastName;

    @SerializedName("id")
    private int id;

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
