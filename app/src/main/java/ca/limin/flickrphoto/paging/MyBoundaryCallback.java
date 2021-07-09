package ca.limin.flickrphoto.paging;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;


import org.jetbrains.annotations.NotNull;

import ca.limin.flickrphoto.model.Photo;
import ca.limin.flickrphoto.view_model.PhotoViewModel;

public class MyBoundaryCallback extends PagedList.BoundaryCallback<Photo> {
    private final PhotoViewModel photoViewModel;
    private boolean isQuery = false;
    private String query = null;

    public MyBoundaryCallback(PhotoViewModel photoViewModel) {
        super();
        this.photoViewModel = photoViewModel;
    }

    @Override
    public void onZeroItemsLoaded() {
        super.onZeroItemsLoaded();
        if (isQuery)
            photoViewModel.requestCategorySearch(query);
        else
            photoViewModel.requestNetwork();
    }

    @Override
    public void onItemAtFrontLoaded(@NonNull @NotNull Photo itemAtFront) {
        super.onItemAtFrontLoaded(itemAtFront);
    }

    @Override
    public void onItemAtEndLoaded(@NonNull @NotNull Photo itemAtEnd) {
        super.onItemAtEndLoaded(itemAtEnd);
        if (isQuery)
            photoViewModel.requestCategorySearch(query);
        else
            photoViewModel.requestNetwork();
    }

    public void setQuery(boolean query) {
        isQuery = query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
