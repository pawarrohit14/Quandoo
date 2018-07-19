package info.quandoo.rohit.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.quandoo.rohit.R;
import info.quandoo.rohit.model.TableResponse;
import info.quandoo.rohit.storage.BaseStore;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private List<TableResponse> tableResponses;
    private int rowLayout, cust_id;
    private Context mContext;
    private BaseStore db;

    public interface ItemClickListener {
        void onClick(View view);
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView tx_table_no;
        private ItemClickListener itemClickListener;


        public TableViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.card_view);
            tx_table_no = v.findViewById(R.id.table_no);
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

    public TableAdapter(List<TableResponse> tableResponses, int rowLayout, Context context, int cust_id, BaseStore db) {
        this.tableResponses = tableResponses;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.cust_id = cust_id;
        this.db = db;
    }

    @Override
    public TableAdapter.TableViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TableViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TableViewHolder holder, final int position) {
        int tableNo = position;

        if (tableResponses.get(position).getAvailible()) {

            holder.tx_table_no.setText("Table No \n" + tableNo);
            holder.cardView.setBackgroundColor(Color.parseColor("#00cc00")); //Green

        } else {

            holder.tx_table_no.setText("Table No \n" + tableNo);
            holder.cardView.setBackgroundColor(Color.parseColor("#e50000")); //Red

        }

        holder.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view) {


                if (tableResponses.get(position).getAvailible()) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Book Table")
                            .setMessage("Do you want to book this table")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TableResponse tableResponse = tableResponses.get(position -1);
                                    tableResponse.setCustId(cust_id);
                                    db.updateTable(tableResponse);
                                    Toast.makeText(mContext, "Table booked successfully", Toast.LENGTH_LONG).show();
                                    tableResponses.get(position).setAvailible(false);
                                    notifyDataSetChanged();
                                }
                            }).setNegativeButton("No", null).show();

                } else {
                    Toast.makeText(mContext, "Table already booked, Please try another", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tableResponses.size();
    }


}