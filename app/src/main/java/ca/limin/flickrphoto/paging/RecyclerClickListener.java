package ca.limin.flickrphoto.paging;

import android.view.View;

public interface RecyclerClickListener {
    void onRecyclerItemClick(int position);
    void onRecyclerLongClick(View view, int position);
}
