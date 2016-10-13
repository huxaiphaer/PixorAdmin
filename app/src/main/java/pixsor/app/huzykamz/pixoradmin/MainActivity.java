package pixsor.app.huzykamz.pixoradmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerview;
    private DatabaseReference mDatabase;
    private ProgressDialog mDialog;
    private Context c;
  //  final   ImageView imagePost;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthLitsener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        mAuthLitsener =new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(firebaseAuth.getCurrentUser() == null){


                Intent loginIntent = new Intent (MainActivity.this,RegisterActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }



            }
        };


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        try {
            mDatabase.keepSynced(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        mRecyclerview =(RecyclerView)findViewById(R.id.mRecyclerview);
        mRecyclerview.hasFixedSize();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
      //  imagePost  =(ImageView) findViewId(R.id.post_image);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PostActivity.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


        mAuth.addAuthStateListener(mAuthLitsener);

        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(Blog.class,
                R.layout.item_activity,
                BlogViewHolder.class,
                mDatabase) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
               viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage_(getApplicationContext(), model.getImageUrl());


                //adding onClick litsener on the picture


                viewHolder.imagePost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this,"Clicked",Toast.LENGTH_SHORT).show();
                      //  Intent intent = new Intent(MainActivit y.this.c, ViewSingleImage.class);


                       // c.startActivity(intent);

                    }
                });



            }
        };

        mRecyclerview.setAdapter(firebaseRecyclerAdapter);
    }



    public static class BlogViewHolder extends RecyclerView.ViewHolder{


        View mView;

        private ImageView imagePost;
        public BlogViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            imagePost =(ImageView)mView.findViewById(R.id.post_image);
        }
        public void setTitle(String title){

            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);

        }

        public void setDesc(String desc){

            TextView post_desc = (TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }


        public void setImage_(final Context c,final String imageUrl){

       //   imagePost =(ImageView)mView.findViewById(R.id.post_image);
           // imagePost.setImageBitmap(imageUrl);

            Picasso.with(c).load(imageUrl).networkPolicy(NetworkPolicy.OFFLINE).into(imagePost, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    //Reloading an image again ...
                    Picasso.with(c).load(imageUrl).into(imagePost);
                }
            });

        }

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        //noinspection SimplifiableIfStatement
        if (item.getItemId() ==R.id.logout) {

            logout();

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
       mAuth.signOut();
    }
}
