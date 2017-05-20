package com.halcyon.ubb.studentlifemanager.ui.course.selectedcourse;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.halcyon.ubb.studentlifemanager.R;
import com.squareup.picasso.Picasso;

public class DetailedCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ImageView imgView = (ImageView) findViewById(R.id.contact_image);
        TextView courseTitle = (TextView) findViewById(R.id.courseTitle);
        TextView courseDescription = (TextView) findViewById(R.id.courseDescription);
        TextView courseAttachment = (TextView) findViewById(R.id.attachmentName);
        courseTitle.setText(getIntent().getStringExtra("courseTitle"));
        courseDescription.setText(getIntent().getStringExtra("courseDescription"));
        courseAttachment.setText(getIntent().getStringExtra("attachmentName"));
        ImageView courseAttachmentIcon = (ImageView) findViewById(R.id.attachmentIcon);

        AppBarLayout appBarLayout= (AppBarLayout) findViewById(R.id.appBarLayout);

        String imgAdress=getIntent().getStringExtra("img_id");
        if (imgAdress.isEmpty() || imgAdress.compareTo("none")==0)
            appBarLayout.setExpanded(false);
        else
            Picasso.with(this).load(imgAdress)
                    .error(android.R.drawable.ic_dialog_alert)
                    .into(imgView);

        if (courseAttachment.getText().toString().equals("none")){
            courseAttachment.setVisibility(View.GONE);
            courseAttachmentIcon.setVisibility(View.GONE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(courseTitle.getText());
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        courseAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(DetailedCourseActivity.this, R.style.AlertDialogCustom);
                dlgAlert.setMessage("Do you want to download this file?");
                dlgAlert.setTitle("Downloading...");
                dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileDownload();
                    }
                });
                dlgAlert.setNegativeButton("Cancel", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return false;
    }

    private void fileDownload(){
        if (!isStoragePermissionGranted()) return;
        String mURL = getIntent().getStringExtra("attachmentURL");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mURL));
        request.setTitle("File download...");
        request.setDescription("File is being downloaded...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        String fileName = URLUtil.guessFileName(mURL, null, MimeTypeMap.getFileExtensionFromUrl(mURL));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("permission","Permission is granted");
                return true;
            } else {
                Log.v("permission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        return true;
    }
}
