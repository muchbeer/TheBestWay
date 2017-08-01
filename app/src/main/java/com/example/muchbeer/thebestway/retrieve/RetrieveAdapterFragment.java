package com.example.muchbeer.thebestway.retrieve;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.example.muchbeer.thebestway.AppController;
import com.example.muchbeer.thebestway.ItemPojo;
import com.example.muchbeer.thebestway.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.muchbeer.thebestway.R.id.itemName;

/**
 * Created by muchbeer on 01/08/2017.
 */

public class RetrieveAdapterFragment extends RecyclerView.Adapter<RetrieveAdapterFragment.ViewHolderRecycler> {

    private static final String LOG = RetrieveAdapterRecycler.class.getSimpleName();
    Context context;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LayoutInflater inflater;
    CustomItemClickListener clickListener;
    //List of superHeroes
    ArrayList<ItemPojo> itemProductList;

    //List of item to be deleted after swipe undo
    ArrayList<ItemPojo> itemsPendingRemoval =   new ArrayList<>();;
    HashMap<ItemPojo, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    //Swiping detail declaration
    int lastInsertedIndex; // so we can add some more items for testing purposes
    private static final int PENDING_REMOVAL_TIMEOUT = 4000; // 4sec
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu
    private Handler handler = new Handler(); // hanlder for running delayed runnables


    public RetrieveAdapterFragment(Context context, ArrayList<ItemPojo> itemProduct,
                                   CustomItemClickListener listener){
        super();
        //Getting all the superheroes
        this.itemProductList = itemProduct;
        this.context = context;
        this.clickListener = listener;
       itemsPendingRemoval = new ArrayList<>();

    }

    @Override
    public RetrieveAdapterFragment.ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_image, parent, false);

        final RetrieveAdapterFragment.ViewHolderRecycler myViewHolder = new RetrieveAdapterFragment.ViewHolderRecycler(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v, myViewHolder.getPosition());
            }
        });

        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(RetrieveAdapterFragment.ViewHolderRecycler holder, int position) {

        ItemPojo itemPjo = itemProductList.get(position);

        final ItemPojo itemNameTobeDeleted = itemProductList.get(position);
        // imageLoader.get(superHero.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        if (itemsPendingRemoval.contains(itemProductList)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.RED);
            holder.thumbNail.setVisibility(View.GONE);
            holder.itemName.setVisibility(View.GONE);
            holder.itemLocation.setVisibility(View.GONE);
            holder.itemPrice.setVisibility(View.GONE);
            holder.undoButton.setVisibility(View.VISIBLE);


            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    afterDeleteClicked(itemNameTobeDeleted);
                }
            });
        } else {
            // we need to show the "normal" state
            holder.itemView.setBackgroundColor(Color.WHITE);
            holder.thumbNail.setVisibility(View.VISIBLE);
            holder.itemName.setVisibility(View.VISIBLE);
            holder.itemLocation.setVisibility(View.VISIBLE);
            holder.itemPrice.setVisibility(View.VISIBLE);
            holder.undoButton.setVisibility(View.GONE);
            holder.undoButton.setOnClickListener(null);

            //The rest will be the normal routine

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
        }



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
    }


        /* else {
            holder.thumbNail.setImageResource(R.drawable.recycler);
        }
*/


    private void afterDeleteClicked  (ItemPojo title_name) {

        // user wants to undo the removal, let's cancel the pending task
        Runnable pendingRemovalRunnable = pendingRunnables.get(title_name);
        pendingRunnables.remove(title_name);
        if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
        itemsPendingRemoval.remove(title_name);
        // this will rebind the row in "normal" state
        notifyItemChanged(itemProductList.indexOf(title_name));
    }
    @Override
    public int getItemCount() {
        return itemProductList.size();
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        //make note here instead of ItemName
        final ItemPojo itemNamePojo = itemProductList.get(position);

        if (!itemsPendingRemoval.contains(itemNamePojo)) {
            itemsPendingRemoval.add(itemNamePojo);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(itemProductList.indexOf(itemNamePojo));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(itemNamePojo, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {


        ItemPojo itemNamePojeRemove = itemProductList.get(position);
        if (itemsPendingRemoval.contains(itemNamePojeRemove)) {
            //   itemsPendingRemoval.remove(itemName);
            Toast.makeText(context, "Items PendingRemoval contain " + itemNamePojeRemove, Toast.LENGTH_LONG).show();
            Log.i(LOG, "Items PendingRemoval contain " + itemName);


        }
        if (itemProductList.contains(itemName)) {
            // itemProductList.remove(position);
            //Do the remove here from the database
            // notifyItemRemoved(position);
            Toast.makeText(context, "Now your item will be removed for good", Toast.LENGTH_LONG).show();
            Log.i(LOG, "Your item will be removed for goods");
        }
    }

    public boolean isPendingRemoval(int position) {
        String itemName = itemProductList.get(position).getTitle();
        return itemsPendingRemoval.contains(itemName);
    }
    public class ViewHolderRecycler extends RecyclerView.ViewHolder {

        public TextView itemName, itemLocation, itemPrice;
        // private NetworkImageView imageview;
        Button undoButton;
        ImageView thumbNail;

        public ViewHolderRecycler(View view) {
            super(view);

            itemName = (TextView) view.findViewById(R.id.title);
            itemLocation = (TextView) view.findViewById(R.id.location);
            itemPrice = (TextView) view.findViewById(R.id.price);
            thumbNail  = (ImageView) view.findViewById(R.id.thumbnail);

            undoButton = (Button) itemView.findViewById(R.id.undo_button);
        }

    }
}
