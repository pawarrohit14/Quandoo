package info.quandoo.rohit.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;

import info.quandoo.rohit.R;
import info.quandoo.rohit.adapter.TableAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rohit Pawar on 18-07-2018.
 */
public class TableBookingActivity extends BaseActivity {

    private static final String TAG = TableBookingActivity.class.getSimpleName();
    int cust_id;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        showTitle();
        setTitle(getResources().getString(R.string.table_title));
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        getDataFromPreviousScreen();
        showDialog();
        fetchData();

    }

    private void fetchData() {
        //If table  data is already present in the database , use it and do not call webservice else get data from server
        if (getDbInstance().getAllTables().size() > 0) {
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
        //set data to recyclerview
        recyclerView.setAdapter(new TableAdapter(getDbInstance().getAllTables(), R.layout.list_item_table, TableBookingActivity.this, cust_id, getDbInstance()));
    }

    // get data from previous activity
    private void getDataFromPreviousScreen() {
        Intent i = getIntent();
        if (i != null) {
            cust_id = getIntent().getIntExtra("CUST_ID", 0);
        }
    }

    //Fetch data from server using Retrofit client
    private void fetchDatafromServer() {

        Call<List<Boolean>> call = getAPIInterface().getTableDetails();

        call.enqueue(new Callback<List<Boolean>>() {
            @Override
            public void onResponse(Call<List<Boolean>> call, Response<List<Boolean>> response) {

                try {

                    for (boolean isAvailable : response.body()) {
                        getDbInstance().addTable(String.valueOf(isAvailable));
                    }
                    dismissDialog();

                } catch (SQLiteException e) {

                    e.printStackTrace();
                    dismissDialog();
                }

                //set data to recyclerview
                recyclerView.setAdapter(new TableAdapter(getDbInstance().getAllTables(), R.layout.list_item_table, TableBookingActivity.this, cust_id, getDbInstance()));
            }

            @Override
            public void onFailure(Call<List<Boolean>> call, Throwable t) {
                dismissDialog();
            }
        });
    }

    //Set Home button for back navigation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
