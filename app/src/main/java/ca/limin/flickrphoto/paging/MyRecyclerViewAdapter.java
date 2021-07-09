package ca.limin.flickrphoto.paging;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ca.limin.flickrphoto.R;
import ca.limin.flickrphoto.glide_cache.GetCacheFile;
import ca.limin.flickrphoto.model.Photo;

import org.jetbrains.annotations.NotNull;

public class MyRecyclerViewAdapter extends PagedListAdapter<Photo, MyRecyclerViewAdapter.ViewHolder>  {
    private static final String TAG = MyRecyclerViewAdapter.class.getSimpleName();
    private final Context context;
    private RecyclerClickListener recyclerClickListener;
    private Boolean isNetwork;

    public MyRecyclerViewAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<Photo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Photo>() {
        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Photo oldItem, @NonNull @NotNull Photo newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull @NotNull Photo oldItem, @NonNull @NotNull Photo newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder position: " + position);

        Photo photo = getItem(position);
        if (photo != null) {
            if (isNetwork)
                Glide.with(context).load(photo.getMedia()).into(holder.imageView);
            else
                Glide.with(context).load(GetCacheFile.getCacheFile(context, photo.getMedia())).into(holder.imageView);
        }

        //callback for short click
        holder.cardView.setOnClickListener(view -> {
            if (recyclerClickListener != null)
                recyclerClickListener.onRecyclerItemClick(position);
        });

        //callback for long click
        holder.cardView.setOnLongClickListener(view -> {
            if (recyclerClickListener != null)
                recyclerClickListener.onRecyclerLongClick(holder.cardView, position);
            return true;
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;

        public ViewHolder(@NonNull CardView itemView) {
            super(itemView);
            Log.i(TAG, "ViewHolder construction");
            cardView = itemView;
            imageView = cardView.findViewById(R.id.imageView);
        }
    }

    public void setRecyclerClickListener(RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = recyclerClickListener;
    }

    public void setNetwork(Boolean network) {
        isNetwork = network;
    }

    public Photo getPhotoAt(int position) {
        return getItem(position);
    }
}
