package pixsor.app.huzykamz.pixoradmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


    private EditText mUsernameEdt;
    private EditText mEmailEdt;
    private EditText mPassowrdEdt;
    private Button mRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabse;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //setting an action bar..
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");



        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        mDatabse = FirebaseDatabase.getInstance().getReference().child("Users");

        mUsernameEdt =(EditText) findViewById(R.id.name_et_register);
        mEmailEdt =(EditText) findViewById(R.id.email_et_register);
        mPassowrdEdt =(EditText) findViewById(R.id.password_et_register);
        mRegister =(Button) findViewById(R.id.btRegister);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });









    }

    private void startRegister() {

        final String name = mUsernameEdt.getText().toString().trim();
        String email= mEmailEdt.getText().toString().trim();
        String password= mPassowrdEdt.getText().toString().trim();
        if(!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){


            mProgress.setMessage("Signing Up...");
            mProgress.show();

           // mAuth.createUserWithEmailAndPassword()
          mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {



                  if(task.isSuccessful()){

                      String user_id_db = mAuth.getCurrentUser().getUid();

                  DatabaseReference current_user_db =     mDatabse.child(user_id_db);
                      current_user_db.child("name").setValue(name);
                      current_user_db.child("image").setValue("default");
                       mProgress.dismiss();

                      Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                      mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      startActivity(mainIntent);

                  }
              }
          });

        }

    }

}
