package com.halcyon.ubb.studentlifemanager;

import android.app.ActionBar;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class CourseDetail extends AppCompatActivity {
    private ImageView imgView;
    private TextView courseTitle, courseDescription;
    private Toolbar courseToolbar;
    private int counter;
    private int imgHeight, imgWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        imgView = (ImageView) findViewById(R.id.contact_image);
        courseTitle = (TextView) findViewById(R.id.courseTitle);
        courseDescription = (TextView) findViewById(R.id.courseDescription);
        //imgView.setImageResource(getIntent().getIntExtra("img_id", 00));
        Picasso.with(this).load(getIntent().getStringExtra("img_id")).into(imgView);
        courseTitle.setText(getIntent().getStringExtra("courseTitle"));
        courseDescription.setText(getIntent().getStringExtra("courseDescription"));

        //toolbar options
        courseToolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(courseToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //supportBar options
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.supportBarColor));

        //get imageview's default size
        ViewTreeObserver vto = imgView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imgView.getViewTreeObserver().removeOnPreDrawListener(this);
                imgHeight = imgView.getMeasuredHeight();
                imgWidth = imgView.getMeasuredWidth();
                return true;
            }
        });

        // home button
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    // OnClickListener of courseImageView(one click increase, another one reduction)
    public void imageClickListener(View view){
        counter++;
        if (counter % 2 != 0){
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) imgView.getLayoutParams();
            params.width = 900;
            params.height = 900;
            imgView.setLayoutParams(params);
        }else {
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) imgView.getLayoutParams();
            params.width = imgWidth;
            params.height = imgHeight;
            imgView.setLayoutParams(params);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return false;
    }
}
