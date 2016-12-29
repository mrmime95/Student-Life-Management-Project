package com.halcyon.ubb.studentlifemanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CourseDetail extends AppCompatActivity {
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
