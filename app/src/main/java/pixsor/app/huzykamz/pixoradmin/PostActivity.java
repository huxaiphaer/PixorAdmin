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

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");



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
  if((!TextUtils.isEmpty(title_val) &&!TextUtils.isEmpty(desc_val) && imageUri!=null)
          ||(!TextUtils.isEmpty(title_val) &&!TextUtils.isEmpty(desc_val) && imageUri==null)){
      mProgress.show();
      StorageReference filepath = mStorage.child("BlogImages").child(imageUri.getLastPathSegment());
      filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


              Uri downloadUri = taskSnapshot.getDownloadUrl();
              DatabaseReference newPost = mDatabase.push();
              newPost.child("title").setValue(title_val);
              newPost.child("description").setValue(desc_val);
              newPost.child("imageUrl").setValue(downloadUri.toString());
            //  newPost.child("uid").setValue(FirebaseAuth.getc);


              mProgress.dismiss();

              startActivity(new Intent(PostActivity.this,MainActivity.class));
          }
      });


  }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {


             imageUri = data.getData();
            Picasso.with(c).load(imageUri).fit().into(imageSelect);
            //imageSelect.setImageURI(imageUri);
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
