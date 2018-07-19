package info.quandoo.rohit.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import info.quandoo.rohit.rest.ApiClient;
import info.quandoo.rohit.rest.ApiInterface;
import info.quandoo.rohit.storage.BaseStore;

/**
 * Created by Rohit Pawar on 18-07-2018.
 */
public class BaseActivity extends AppCompatActivity {
    public ProgressDialog dialog;
    private BaseStore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        db = new BaseStore(this);
    }


    //show progress dialogue
    public void showDialog() {
        if (dialog != null) {
            dialog.setMessage("Loading, please wait...");
            dialog.show();
        }
    }

    //dismiss progress dialogue
    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    //get Basestore instance
    public BaseStore getDbInstance() {
        return db;
    }

    //get APIInterface instance
    public ApiInterface getAPIInterface() {
        return ApiClient.getClient().create(ApiInterface.class);
    }

    //Set Actionbar title
    protected void setTitle(String title) {
        this.getSupportActionBar().setTitle(title);
    }

    //Show Actionbar title
    protected void showTitle() {
        this.getSupportActionBar().show();
    }

    //Show Actionbar title
    protected void hideTitle() {
        this.getSupportActionBar().hide();
    }

    //Check internet connection
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    // Showing the status in Snackbar
    protected void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
}
