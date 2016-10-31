package pixsor.app.huzykamz.pixoradmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class PostActivity extends AppCompatActivity {

    private ImageButton imageSelect;
    private static final int GALLERY_REQUEST = 1;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button postButton;
    private  Uri imageUri = null;
    private ProgressDialog mProgress;
    private StorageReference mStorage;
    private Context c;
    String eventname="";
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);

        //setting an action bar..
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Post a Picture");

        Intent inn = getIntent();

        try {
            if (null != inn) {
                eventname = inn.getStringExtra(EventViewActivity.EventViewHolder.KEY_PARTY_NAME);
                mStorage = FirebaseStorage.getInstance().getReference();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog").child("SingleEvent").child(eventname);
            }
        }
        catch (Exception e){

            System.out.println("Error :"+ e);
        }

        mProgress = new ProgressDialog(this);
        imageSelect = (ImageButton) findViewById(R.id.imageSelect);
        mPostTitle = (EditText) findViewById(R.id.mPostTitle);
        mPostDesc = (EditText) findViewById(R.id.mPostDesc);
        postButton = (Button) findViewById(R.id.postButton);
        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });


    }

    private void startPosting() {
        mProgress.setMessage("Uploading Image...");


        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();
        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && imageUri != null) {
            mProgress.show();
            StorageReference filepath = mStorage.child("BlogImages").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();
                    DatabaseReference c = mDatabase.push();
                    newPost.child("EventTitle").setValue(title_val);
                    newPost.child("EventDescription").setValue(desc_val);
                    newPost.child("EventImage").setValue(downloadUri.toString());
                    newPost.child("PostId").setValue(c);



                    mProgress.dismiss();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));


                }
            }


            );


        } else if (TextUtils.isEmpty(title_val) && TextUtils.isEmpty(desc_val) && imageUri != null) {
            mProgress.show();
            StorageReference filepath = mStorage.child("BlogImages").child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("EventTitle").setValue("");
                    newPost.child("EventDescription").setValue("");
                    newPost.child("EventImage").setValue(downloadUri.toString());


                    mProgress.dismiss();
                   // startActivity(new Intent(PostActivity.this, MainActivity.class));
                  Intent load=  new Intent(PostActivity.this,MainActivity.class);
                    load.putExtra(eventname,eventname);
                    startActivity(load);


                }
            }


            );

        }

        else if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && imageUri== null){

            Toast.makeText(getApplicationContext(),"Please insert an Image and Upload ! ",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {


             imageUri = data.getData();
            Picasso.with(c).load(imageUri).fit().into(imageSelect);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
