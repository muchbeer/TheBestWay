package com.example.muchbeer.thebestway.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.muchbeer.thebestway.AppController;
import com.example.muchbeer.thebestway.ItemPojo;
import com.example.muchbeer.thebestway.R;
import com.example.muchbeer.thebestway.retrieve.RetrieveAdapter;

import java.util.List;

/**
 * Created by muchbeer on 23/07/2017.
 */

public class RetrieveAdapterImage extends RecyclerView.Adapter<RetrieveAdapterImage.MyViewHolder> {


    private List<ItemPojo> itemProduct;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName, itemLocation, itemPrice;

        public NetworkImageView thumbNail;

        public MyViewHolder(View view) {
            super(view);

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            thumbNail = (NetworkImageView) view.findViewById(R.id.thumbnail);
            itemName = (TextView) view.findViewById(R.id.itemName);
            itemLocation = (TextView) view.findViewById(R.id.itemLocation);
            itemPrice = (TextView) view.findViewById(R.id.itemPrice);
        }

    }

    public RetrieveAdapterImage(List<ItemPojo> itemProductArg) {
        this.itemProduct = itemProductArg;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_image, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemPojo itemPjo = itemProduct.get(position);
        // thumbnail image
     //   thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        holder.itemName.setText(itemPjo.getTitle());
        holder.itemLocation.setText(itemPjo.getLocation());
        holder.itemPrice.setText(itemPjo.getPrice());
holder.thumbNail.setImageUrl(itemPjo.getThumbnailUrl(), imageLoader);
    }


    @Override
    public int getItemCount() {

        return itemProduct.size();
    }
}
