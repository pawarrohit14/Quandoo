package info.quandoo.rohit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import info.quandoo.rohit.R;
import info.quandoo.rohit.activity.TableBookingActivity;
import info.quandoo.rohit.model.CustomerResponse;
import info.quandoo.rohit.model.TableResponse;
import info.quandoo.rohit.storage.BaseStore;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<CustomerResponse> customer;
    private int rowLayout;
    private Context mContext;
    private BaseStore db;

    public interface ItemClickListener {
        void onClick(View view);
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        LinearLayout customerLayout;
        TextView tx_customerFirstName;
        TextView tx_id;
        TextView tx_tableno;
        private ItemClickListener itemClickListener;


        public CustomerViewHolder(View v) {
            super(v);
            customerLayout =  v.findViewById(R.id.customer_layout);
            tx_customerFirstName =  v.findViewById(R.id.name);
            tx_id =  v.findViewById(R.id.id);
            tx_tableno = v.findViewById(R.id.table_no);
            v.setOnClickListener(this);

        }

        public void setOnItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view);
        }

    }

    public CustomerAdapter(List<CustomerResponse> customer, int rowLayout, Context context, BaseStore db) {
        this.customer = customer;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.db = db;
    }

    @Override
    public CustomerAdapter.CustomerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new CustomerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomerViewHolder holder, final int position) {
        holder.tx_customerFirstName.setText(customer.get(position).getCustomerFirstName() + " " + customer.get(position).getCustomerLastName());
        holder.tx_id.setText(customer.get(position).getId() + "");


        TableResponse tableResponse = db.getTableBycusrID(customer.get(position).getId());
        if(tableResponse!= null)
        {
            holder.tx_tableno.setText("Table No. - " + tableResponse.getTable_Id()+"");
        } else {
            holder.tx_tableno.setText("");
        }


        holder.setOnItemClickListener(new CustomerAdapter.ItemClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, TableBookingActivity.class);
                intent.putExtra("CUST_ID",customer.get(position).getId() );
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return customer.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}