package pixsor.app.huzykamz.pixoradmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by HUZY_KAMZ on 10/4/2016.
 */
public class LoginActivity extends AppCompatActivity {


    private EditText et_email_;
    private EditText et_password_;
    private Button button_login;
    private FirebaseAuth mAuth;
    private Button notYet_Registered;
    private DatabaseReference mDatabaseUsers;
    private ProgressDialog mDialoglogin;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth =FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);

        et_email_ =(EditText) findViewById(R.id.et_email_);
        et_password_ =(EditText) findViewById(R.id.et_password_);
        button_login=(Button) findViewById(R.id.button_login);

        mDialoglogin = new ProgressDialog(this);
        notYet_Registered =(Button) findViewById(R.id.notYet_Registered);
        notYet_Registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadRegister = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(loadRegister);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkLogin();

            }
        });





    }

    private void checkLogin() {

        String email = et_email_.getText().toString().trim();
        String password = et_password_.getText().toString().trim();


        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mDialoglogin.setMessage("Logging in...");
            mDialoglogin.show();

          mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){

                   checkUserExists();
                      mDialoglogin.dismiss();
                  }
                  else{

                      Toast.makeText(LoginActivity.this,"Error while Logging in..",Toast.LENGTH_LONG).show();
                      mDialoglogin.dismiss();
                  }
              }
          });

        }
    }

    private void checkUserExists() {

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.hasChild(user_id)){


                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }else {
                    Intent setUpIntent = new Intent(LoginActivity.this,SetupActivity.class);
                    setUpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setUpIntent);
                 //   mDialoglogin.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
