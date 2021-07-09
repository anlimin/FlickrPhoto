package ca.limin.flickrphoto.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import ca.limin.flickrphoto.R;
import ca.limin.flickrphoto.glide_cache.GetCacheFile;
import ca.limin.flickrphoto.model.Photo;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DetailActivity.class.getSimpleName();
    ImageView imageView;
    TextView textViewTitle, textViewDate, textViewPublish, textViewAuthor, textViewTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initialize();
    }

    @SuppressLint("SetTextI18n")
    private void initialize() {
        imageView = findViewById(R.id.imageViewDetail);
        imageView.setOnClickListener(this);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDate = findViewById(R.id.textViewDate);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewPublish = findViewById(R.id.textViewPublish);
        textViewTag = findViewById(R.id.textViewTag);

        Photo photo = (Photo) getIntent().getSerializableExtra("detail");
        //get glide cache photo
        Log.i(TAG, "cache file path: " + GetCacheFile.getCacheFile(this, photo.getMedia()));
        Glide.with(this).load(GetCacheFile.getCacheFile(this, photo.getMedia())).into(imageView);
        textViewTitle.setText("Title: " + photo.getTitle());
        textViewDate.setText("Date: " + photo.getDate());
        textViewAuthor.setText("Author: " + photo.getAuthor());
        textViewPublish.setText("Published: " + photo.getPublish());
        textViewTag.setText("Tag: " + photo.getTag());
    }

    @Override
    public void onClick(View view) {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}