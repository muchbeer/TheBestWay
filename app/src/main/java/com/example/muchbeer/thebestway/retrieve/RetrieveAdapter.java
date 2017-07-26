package com.example.muchbeer.thebestway.retrieve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.muchbeer.thebestway.AppController;
import com.example.muchbeer.thebestway.ItemPojo;
import com.example.muchbeer.thebestway.R;
import com.example.muchbeer.thebestway.util.NetworkController;

import java.util.List;

/**
 * Created by muchbeer on 21/07/2017.
 */

public class RetrieveAdapter extends RecyclerView.Adapter<RetrieveAdapter.MyViewHolder>  {


    private List<ItemPojo> itemProductList;
    private Context context;
    private LayoutInflater inflater;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();



    public RetrieveAdapter(Context context, List<ItemPojo> itemProductArg) {

        this.context = context;
        this.itemProductList = itemProductArg;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

          }

    @Override
    public RetrieveAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_image, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RetrieveAdapter.MyViewHolder holder, int position) {
        ItemPojo itemPjo = itemProductList.get(position);

        if (imageLoader == null)
           // imageLoader = AppController.getInstance().getImageLoader();

        imageLoader = NetworkController.getInstance(context).getImageLoader();
      /*  imageLoader.get(itemPjo.getThumbnailUrl(),
                ImageLoader.getImageListener(holder.thumbNail, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
*/
        holder.itemName.setText(itemPjo.getTitle());
        holder.itemLocation.setText(itemPjo.getLocation());
        holder.itemPrice.setText(itemPjo.getPrice());
        holder.thumbNail.setImageUrl(itemPjo.getThumbnailUrl(),imageLoader);
         }

    @Override
    public int getItemCount() {
        return itemProductList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName, itemLocation, itemPrice;
       // private NetworkImageView imageview;

          NetworkImageView thumbNail;

        public MyViewHolder(View view) {
            super(view);

            itemName = (TextView) view.findViewById(R.id.title);
            itemLocation = (TextView) view.findViewById(R.id.location);
            itemPrice = (TextView) view.findViewById(R.id.price);
            thumbNail  = (NetworkImageView) view.findViewById(R.id.thumbnail);

        }

    }



}
