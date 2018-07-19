package info.quandoo.rohit.rest;

import java.util.List;

import info.quandoo.rohit.model.CustomerResponse;
import info.quandoo.rohit.model.TableResponse;
import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {
    @GET("quandoo-assessment/customer-list.json")
    Call<List<CustomerResponse>> getCustomerList();

    @GET("quandoo-assessment/table-map.json")
    Call<List<Boolean>> getTableDetails();
}
