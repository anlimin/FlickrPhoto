package ca.limin.flickrphoto.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import ca.limin.flickrphoto.R;
import ca.limin.flickrphoto.glide_cache.GetCacheFile;
import ca.limin.flickrphoto.model.Photo;

public class MagnifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnify);

        ImageView imageView = findViewById(R.id.imageViewMagnify);
        Photo photo = (Photo) getIntent().getSerializableExtra("magnify");
        //get glide cache photo
        Glide.with(this).load(GetCacheFile.getCacheFile(this, photo.getMedia())).into(imageView);
    }

    public void onClick(View view) {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}