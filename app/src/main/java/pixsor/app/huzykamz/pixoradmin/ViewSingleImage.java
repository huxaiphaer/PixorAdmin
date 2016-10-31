package pixsor.app.huzykamz.pixoradmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewSingleImage extends AppCompatActivity {


    private DatabaseReference mDatabase;

    String eventname;
    private ImageView mImageSingle;
    private TextView mDescription;
    private TextView mTitle;
    String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_view);
        //setting an action bar..
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Post");

        mImageSingle =(ImageView)findViewById(R.id.imageView_single);
        mDescription =(TextView)findViewById(R.id.desc_single_txt);
        mTitle=(TextView)findViewById(R.id.title_single_txt);
        Intent inn = getIntent();
        try {
            if (null != inn) {
                eventname = inn.getStringExtra("EventName");
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog").child("SingleEvent").child(eventname);

            }
        }

        catch (Exception ex){
            System.out.println("Error "+ ex);


        }
        post_key = getIntent().getExtras().getString("Blog");
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eventdescription = (String)dataSnapshot.child("EventDescription").getValue();
                String eventtitle = (String)dataSnapshot.child("EventTitle").getValue();
                String eventimage = (String)dataSnapshot.child("EventImage").getValue();

                mTitle.setText(eventtitle);
                mDescription.setText(eventdescription);
                Picasso.with(getApplicationContext()).load(eventimage).into(mImageSingle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.delete:

                mDatabase.child(post_key).removeValue();
                Toast.makeText(getApplicationContext(),"The Post has been removed Successfully  ",Toast.LENGTH_LONG).show();

                break;



        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }



}
