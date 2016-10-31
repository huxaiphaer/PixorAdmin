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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerview;
    private DatabaseReference mDatabseUsers;
    private DatabaseReference mDatabase;
    private ProgressDialog mDialog;
    private Context c;
     private   static  String eventname="";
    private FirebaseAuth mAuth;
    private String EName="Huzy";
    private FirebaseAuth.AuthStateListener mAuthLitsener;
    private static String EventName= "";
    private String title_eve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent in_ = getIntent();

        if (null != in_) {
            title_eve = in_.getStringExtra("PARTY_NAME");
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title_eve);



        mAuth = FirebaseAuth.getInstance();
        mAuthLitsener =new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if(firebaseAuth.getCurrentUser() == null){


                Intent loginIntent = new Intent (MainActivity.this,MainActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }



            }
        };



        Intent inn = getIntent();
     try {
         if (null != inn) {
             eventname = inn.getStringExtra(EventViewActivity.EventViewHolder.KEY_PARTY_NAME);
             mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog").child("SingleEvent").child(eventname);

         }
     }

     catch (Exception ex){
         System.out.println("Error "+ ex);


     }







        System.out.println("Output here   :  " + mDatabase);
        Toast.makeText(getApplicationContext(), "" + mDatabase, Toast.LENGTH_LONG).show();


        mDatabseUsers =FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabseUsers.keepSynced(true);
        try {
            mDatabase.keepSynced(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        mRecyclerview =(RecyclerView)findViewById(R.id.mRecyclerview);
        mRecyclerview.hasFixedSize();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
      // imagePost  =(ImageView) findViewId(R.id.post_image);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(MainActivity.this, PostActivity.class));

                Intent loadMainActivity = new Intent(MainActivity.this,PostActivity.class);
                loadMainActivity.putExtra(EventViewActivity.EventViewHolder.KEY_PARTY_NAME, eventname);
                startActivity(loadMainActivity);
            }
        });





    }




    private void checkUserExists() {

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.hasChild(user_id)){


                    Intent setupIntent = new Intent(MainActivity.this,SetupActivity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();

        //checkUserExists();
       // mAuth.addAuthStateListener(mAuthLitsener);
try {
    FirebaseRecyclerAdapter<Posts, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, BlogViewHolder>(Posts.class,
            R.layout.item_activity,
            BlogViewHolder.class,
            mDatabase) {
        @Override
        protected void populateViewHolder(BlogViewHolder viewHolder, final Posts model, final int position) {

            final String key_post = getRef(position).getKey();
            viewHolder.setTitle(model.getEventTitle());
            viewHolder.setDesc(model.getEventDescription());
            viewHolder.setImage(c, model.getEventImage());

            viewHolder.imagePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(),""+key_post,Toast.LENGTH_LONG).show();
                    Intent singleView = new Intent(MainActivity.this,ViewSingleImage.class);
                    singleView.putExtra("Blog",key_post);
                    singleView.putExtra("EventName",eventname);
                    startActivity(singleView);
                }
            });


            //adding onClick litsener on the picture


              /*  viewHolder. mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this,"Clicked",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this.c, ViewSingleImage.class);
                         //intent.putExtra("EventName",model.getEventImage().toString());
                        c.startActivity(intent);

                    }
                });*/


        }
    };

    mRecyclerview.setAdapter(firebaseRecyclerAdapter);
}
catch (Exception ex){

    System.out.println("Error "+ ex);
}
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


        public void setImage(final Context c,final String imageUrl){

       //

            Picasso.with(c).load(imageUrl).error(R.mipmap.add_btn).fit().centerInside().placeholder(R.mipmap.add_btn)
                    .networkPolicy(NetworkPolicy.OFFLINE).into(imagePost, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    //Reloading an image again ...
                    Picasso.with(c).load(imageUrl).error(R.mipmap.add_btn).placeholder(R.mipmap.add_btn)
                            .into(imagePost);
                }
            });

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        //noinspection SimplifiableIfStatement
        if (item.getItemId() ==R.id.logout) {

          //  logout();

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
       mAuth.signOut();
    }
}
