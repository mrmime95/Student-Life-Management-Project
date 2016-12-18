package com.halcyon.ubb.studentlifemanager;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CourseDetail extends AppCompatActivity {
    private ImageView imgView;
    private TextView courseTitle, courseDescription, courseAttachment;
    private Toolbar courseToolbar;
    private FrameLayout mFrameCourse;
    private View mCourses;
    private View mCoursesZoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        mFrameCourse = (FrameLayout) findViewById(R.id.course_opened);
        mCourses=getLayoutInflater().inflate(R.layout.course_article_all_description, mFrameCourse, false);
        mFrameCourse.addView(mCourses);

        imgView = (ImageView) findViewById(R.id.contact_image);
        courseTitle = (TextView) findViewById(R.id.courseTitle);
        courseDescription = (TextView) findViewById(R.id.courseDescription);
        courseAttachment = (TextView) findViewById(R.id.attachmentName);
        Picasso.with(this).load(getIntent().getStringExtra("img_id")).into(imgView);
        courseTitle.setText(getIntent().getStringExtra("courseTitle"));
        courseDescription.setText(getIntent().getStringExtra("courseDescription"));
        courseAttachment.setText(getIntent().getStringExtra("attachmentName"));
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
        // home button
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        findViewById(R.id.contact_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCoursesZoom=getLayoutInflater().inflate(R.layout.course_image_zoom, mFrameCourse, false);
                mFrameCourse.addView(mCoursesZoom);
                Picasso.with(getApplication()).load(getIntent().getStringExtra("img_id")).into((ImageView) findViewById(R.id.image_zoom));
            }
        });
    }
    public void zoomToThePicture(View view)
    {
        mFrameCourse.removeView(mCoursesZoom);
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
