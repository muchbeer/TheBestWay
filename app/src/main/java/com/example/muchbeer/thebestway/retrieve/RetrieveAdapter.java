package com.example.muchbeer.thebestway.retrieve;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muchbeer.thebestway.ItemPojo;
import com.example.muchbeer.thebestway.R;

import java.util.List;

/**
 * Created by muchbeer on 21/07/2017.
 */

public class RetrieveAdapter extends RecyclerView.Adapter<RetrieveAdapter.MyViewHolder>  {


    private List<ItemPojo> itemProduct;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName, itemLocation, itemPrice;

        public MyViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.itemName);
            itemLocation = (TextView) view.findViewById(R.id.itemLocation);
            itemPrice = (TextView) view.findViewById(R.id.itemPrice);
        }

    }

    public RetrieveAdapter(List<ItemPojo> itemProductArg) {
        this.itemProduct = itemProductArg;
    }

    @Override
    public RetrieveAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RetrieveAdapter.MyViewHolder holder, int position) {
        ItemPojo itemPjo = itemProduct.get(position);

        holder.itemName.setText(itemPjo.getItemName());
        holder.itemLocation.setText(itemPjo.getItemLocation());
        holder.itemPrice.setText(itemPjo.getItemPrice());

    }


    @Override
    public int getItemCount() {
        return itemProduct.size();
    }
}
