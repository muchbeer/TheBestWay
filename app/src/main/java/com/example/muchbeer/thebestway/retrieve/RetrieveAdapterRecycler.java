package com.example.muchbeer.thebestway.retrieve;

import android.content.Context;
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
import com.example.muchbeer.thebestway.util.NetworkController;

import java.util.List;

/**
 * Created by muchbeer on 26/07/2017.
 */

public class RetrieveAdapterRecycler extends RecyclerView.Adapter<RetrieveAdapterRecycler.ViewHolderRecycler> {


    private Context context;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LayoutInflater inflater;

    //List of superHeroes
    List<ItemPojo> itemProductList;

    public RetrieveAdapterRecycler(List<ItemPojo> itemProduct, Context context){
        super();
        //Getting all the superheroes
        this.itemProductList = itemProduct;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public RetrieveAdapterRecycler.ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_image, parent, false);

        RetrieveAdapterRecycler.ViewHolderRecycler myViewHolder = new RetrieveAdapterRecycler.ViewHolderRecycler(itemView);
         return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RetrieveAdapterRecycler.ViewHolderRecycler holder, int position) {

        ItemPojo itemPjo = itemProductList.get(position);
       // imageLoader.get(superHero.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));


        holder.itemName.setText(itemPjo.getTitle());
        holder.itemLocation.setText(itemPjo.getLocation());
        holder.itemPrice.setText(itemPjo.getPrice());
        holder.thumbNail.setImageUrl(itemPjo.getThumbnailUrl(),imageLoader);
    }

    @Override
    public int getItemCount() {
        return itemProductList.size();
    }

    public class ViewHolderRecycler extends RecyclerView.ViewHolder {

        public TextView itemName, itemLocation, itemPrice;
        // private NetworkImageView imageview;

        NetworkImageView thumbNail;

        public ViewHolderRecycler(View view) {
            super(view);

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();



            itemName = (TextView) view.findViewById(R.id.title);
            itemLocation = (TextView) view.findViewById(R.id.location);
            itemPrice = (TextView) view.findViewById(R.id.price);
            thumbNail  = (NetworkImageView) view.findViewById(R.id.thumbnail);

        }

    }
}
