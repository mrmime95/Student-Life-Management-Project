package com.halcyon.ubb.studentlifemanager.ui.course.selectedcourse;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.halcyon.ubb.studentlifemanager.R;
import com.halcyon.ubb.studentlifemanager.ui.course.detail.SmoothScrollBehavior;
import com.squareup.picasso.Picasso;

public class DetailedCourseActivity extends AppCompatActivity {
    private ImageView imgView, courseAttachmentIcon;
    private TextView courseTitle, courseDescription, courseAttachment;
    private Toolbar courseToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        imgView = (ImageView) findViewById(R.id.contact_image);
        courseTitle = (TextView) findViewById(R.id.courseTitle);
        courseDescription = (TextView) findViewById(R.id.courseDescription);
        courseAttachment = (TextView) findViewById(R.id.attachmentName);
        Picasso.with(this).load(getIntent().getStringExtra("img_id")).into(imgView);
        courseTitle.setText(getIntent().getStringExtra("courseTitle"));
        courseDescription.setText(getIntent().getStringExtra("courseDescription"));
        courseAttachment.setText(getIntent().getStringExtra("attachmentName"));
        courseAttachmentIcon = (ImageView)findViewById(R.id.attachmentIcon);

        //TODO CR: This is your decision but I'd argue that fixing such a minor glitch is not worth the effort and the amount of ugly code it introduces (you're trying
        //TODO CR: to use reflection 60 times each second - imagine how that affects performance on low-end devices...). If you researched the topic you know that the
        //TODO CR: creators of the Support Library are working on a solution, it shouldn't be your job to fix library bugs. [Peter]
        //android CollapsingToolbarLayout's fling is broken
        // custom behaviour for AppBarLayout solves this for now
        //TODO: Change back to android implementation once it is working.
        AppBarLayout appBarLayout= (AppBarLayout) findViewById(R.id.appBarLayout);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        params.setBehavior(new SmoothScrollBehavior(R.id.nested_scroll_view));

        /*Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.supportBarColor));*/

        if (courseAttachment.getText().toString().equals("none")){
            courseAttachment.setVisibility(View.GONE);
            courseAttachmentIcon.setVisibility(View.GONE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
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
