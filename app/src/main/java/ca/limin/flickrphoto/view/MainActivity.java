package ca.limin.flickrphoto.view;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import ca.limin.flickrphoto.R;
import ca.limin.flickrphoto.model.Photo;
import ca.limin.flickrphoto.paging.MyRecyclerViewAdapter;
import ca.limin.flickrphoto.paging.RecyclerClickListener;
import ca.limin.flickrphoto.view_model.PhotoViewModel;

public class MainActivity extends AppCompatActivity implements RecyclerClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private PhotoViewModel photoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        registerSwipedMove();
        registerObserverPagingList();
    }

    private void initialize() {
        photoViewModel = new ViewModelProvider(this).get(PhotoViewModel.class);
        getLifecycle().addObserver(photoViewModel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registerRecyclerView();
    }

    private void registerRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(this);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        myRecyclerViewAdapter.setRecyclerClickListener(this);
        recyclerView.setHasFixedSize(true);
        myRecyclerViewAdapter.setNetwork(photoViewModel.isNetworkAvailable());
    }

    private void registerSwipedMove() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                photoViewModel.delete(myRecyclerViewAdapter.getPhotoAt(viewHolder.getAdapterPosition()));
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void registerObserverPagingList() {
        photoViewModel.getPagedListLiveData().observe(this, new Observer<PagedList<Photo>>() {
            @Override
            public void onChanged(PagedList<Photo> photos) {
                myRecyclerViewAdapter.submitList(photos);
            }
        });
    }

    @Override
    public void onRecyclerItemClick(int position) {
        Log.i(TAG, "onRecyclerItemClick: " + position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("detail", myRecyclerViewAdapter.getPhotoAt(position));
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onRecyclerLongClick(View view, int position) {
        Log.i(TAG, "onRecyclerLongClick: " + position);
        Intent intent = new Intent(this, MagnifyActivity.class);
        intent.putExtra("magnify", myRecyclerViewAdapter.getPhotoAt(position));
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        SearchView mSearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        mSearchView.setSearchableInfo(searchableInfo);
        // setting to false: automatically opens up the keyboard and search fields
        mSearchView.setIconified(true);
        SetQueryListener(mSearchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void SetQueryListener(SearchView mSearchView) {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!photoViewModel.isNetworkAvailable()) {
                    Toast.makeText(MainActivity.this, "network is not available", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Log.i(TAG, "onQueryTextSubmit, the input string: " + query);
                photoViewModel.setQuery(true);
                photoViewModel.deleteAll();
                recyclerView.scrollToPosition(0);
                photoViewModel.requestCategorySearch(query);
                // needed to change focus to mainActivity
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // leave this for the OS to handle
                return false;
            }
        });
    }
}