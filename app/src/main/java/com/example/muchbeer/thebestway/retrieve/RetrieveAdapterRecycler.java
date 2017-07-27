package com.example.muchbeer.thebestway.retrieve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.muchbeer.thebestway.AppController;
import com.example.muchbeer.thebestway.ItemPojo;
import com.example.muchbeer.thebestway.R;
import com.example.muchbeer.thebestway.util.NetworkController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muchbeer on 26/07/2017.
 */

public class RetrieveAdapterRecycler extends RecyclerView.Adapter<RetrieveAdapterRecycler.ViewHolderRecycler> {


     Context context;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LayoutInflater inflater;

    CustomItemClickListener clickListener;
    //List of superHeroes
    ArrayList<ItemPojo> itemProductList;

    public RetrieveAdapterRecycler(Context context, ArrayList<ItemPojo> itemProduct, CustomItemClickListener listener){
        super();
        //Getting all the superheroes
        this.itemProductList = itemProduct;
        this.context = context;
        this.clickListener = listener;

    }

    @Override
    public RetrieveAdapterRecycler.ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_image, parent, false);

        final RetrieveAdapterRecycler.ViewHolderRecycler myViewHolder = new RetrieveAdapterRecycler.ViewHolderRecycler(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v, myViewHolder.getPosition());
            }
        });

         return myViewHolder;
    }


    @Override
    public void onBindViewHolder(RetrieveAdapterRecycler.ViewHolderRecycler holder, int position) {
        Context mContext = holder.thumbNail.getContext();

        ItemPojo itemPjo = itemProductList.get(position);
       // imageLoader.get(superHero.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));


        holder.itemName.setText(itemPjo.getTitle());
        holder.itemLocation.setText(itemPjo.getLocation());
        holder.itemPrice.setText(itemPjo.getPrice());
String urlString = itemProductList.get(position).getThumbnailUrl();


        urlString.replace("http", "https");
        String urlString2 =  "http://api.learn2crack.com/android/images/donut.png";
        Log.i("IMAGEPICASSO ", urlString.replace("http", "https"));
      //  vh.picture.setDefaultImageResId(R.drawable.recycler);
        RetrieveImage downloadVolleyImage = new RetrieveImage();
      //  downloadVolleyImage.getImage(urlString.replace("http", "https"), holder.thumbNail);
       PicassoAio.downloadImage(context, urlString.replace("http", "https"), holder.thumbNail);


      //  Picasso.with(activity).load(url).transform(new CircleTransform()).into(imageView);
       // holder.thumbNail.setImageUrl(itemPjo.getThumbnailUrl(),imageLoader);

       // if (!TextUtils.isEmpty(itemProductList.get(position).getThumbnailUrl())) {
            // I Love picasso library :) http://square.github.io/picasso/
           /* Picasso.with(mContext).load(itemProductList.get(position).getThumbnailUrl()).
                   placeholder(R.drawable.recycler).
                    transform(new RoundedTransformation()).
                    fit().
                    centerCrop().
                    into(holder.thumbNail);*/
        }/* else {
            holder.thumbNail.setImageResource(R.drawable.recycler);
        }
*/

    @Override
    public int getItemCount() {
        return itemProductList.size();
    }

    public class ViewHolderRecycler extends RecyclerView.ViewHolder {

        public TextView itemName, itemLocation, itemPrice;
        // private NetworkImageView imageview;

        ImageView thumbNail;

        public ViewHolderRecycler(View view) {
            super(view);

           itemName = (TextView) view.findViewById(R.id.title);
            itemLocation = (TextView) view.findViewById(R.id.location);
            itemPrice = (TextView) view.findViewById(R.id.price);
            thumbNail  = (ImageView) view.findViewById(R.id.thumbnail);

        }

    }
}
