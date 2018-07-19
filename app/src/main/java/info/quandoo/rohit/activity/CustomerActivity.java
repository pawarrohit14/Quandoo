package info.quandoo.rohit.activity;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import info.quandoo.rohit.R;
import info.quandoo.rohit.adapter.CustomerAdapter;
import info.quandoo.rohit.model.CustomerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rohit Pawar on 18-07-2018.
 */
public class CustomerActivity extends BaseActivity {

    private static final String TAG = CustomerActivity.class.getSimpleName();
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showTitle();
        setTitle(getResources().getString(R.string.customer_title));
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }


    @Override
    protected void onResume() {
        super.onResume();
        showDialog();
        fetchData();
    }

    private void fetchData() {
        //If customer data is already present in the database , use it and do not call webservice else get data from server
        if (getDbInstance().getAllCustomers().size() > 0) {
            fetchDataFromLocal();
        } else if (isOnline()) { //Check Internet Connection
            showSnack(true);
            fetchDatafromServer();

        } else {
            dismissDialog();
            showSnack(false); // Show internet unavailable msg
        }

    }

    //Fetch table data from local storage & show it on the list
    private void fetchDataFromLocal() {
        dismissDialog();
        recyclerView.setAdapter(new CustomerAdapter(getDbInstance().getAllCustomers(), R.layout.list_item_customer, getApplicationContext(), getDbInstance()));
    }

    // //Fetch table data from server using retrofit client
    private void fetchDatafromServer() {

        Call<List<CustomerResponse>> call = getAPIInterface().getCustomerList();
        call.enqueue(new Callback<List<CustomerResponse>>() {
            @Override
            public void onResponse(Call<List<CustomerResponse>> call, Response<List<CustomerResponse>> response) {
                try {
                    for (CustomerResponse customerResponse : response.body()) {
                        //add each customer record to the database
                        getDbInstance().addCustomer(customerResponse);
                    }
                    //dismiss progress dialogue
                    dismissDialog();
                } catch (SQLiteException e) {
                    e.printStackTrace();
                    dismissDialog();
                }

                recyclerView.setAdapter(new CustomerAdapter(getDbInstance().getAllCustomers(), R.layout.list_item_customer, getApplicationContext(), getDbInstance()));
            }

            @Override
            public void onFailure(Call<List<CustomerResponse>> call, Throwable t) {
                //dismiss progress dialogue
                dismissDialog();
            }
        });
    }


    //Search for Customer list
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_seatch, menu);
        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}
