package com.dev.nathan.reportlife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //region <!Declaraçao de input e buttons !>
    private TextInputEditText mDisplayName;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private Button mCreateBtn;
    //endregion

    //region <!Declaraçao toolbar!>
    private Toolbar mToolbar;
    //endregion

    //region <!Declaraçao de Firebase Autenticaçao e progressdialog !>
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgressDialog;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //region <!ProgressDialog!>
       mProgressDialog = new ProgressDialog(this);
       //endregion

        //region <!Toolbar set !>
        mToolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.regisToolbarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion

        //region <!Firebase Autenticaçao instancia !>
        mAuth = FirebaseAuth.getInstance();
        //endregion

        //region <!Busca display e fields !>
        mDisplayName = (TextInputEditText) findViewById(R.id.reg_display_name);
        mEmail = (TextInputEditText)  findViewById(R.id.reg_email);
        mPassword =  (TextInputEditText) findViewById(R.id.reg_password);
        mCreateBtn = (Button)  findViewById(R.id.reg_create_btn);
        //endregion

       //region <!Evento de click do botao de registro!>
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region <!Pegando valores dos inputs!>
                String display_name = mDisplayName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                //endregion

                //region <!Verificando se os campos estao vazios e chamando o metodo de registro de usuario!>
                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password)){
                    mProgressDialog.setTitle(getString(R.string.regiTituloProgressDialog));
                    mProgressDialog.setMessage(getString(R.string.regProgressDialog));
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();

                    register_user(display_name, email ,password);

                }
                //endregion

            }
        });
        //endregion !

    }

    //region  <!funçao de autenticaçao de usuario e verificaçao de sucesso de cadastro!>
    private void register_user(final String display_name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //region  <!verificaçao de requisiçao!>
                if(task.isSuccessful()){

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("status","Hi there I'm using chat App.");
                    userMap.put("image","default");
                    userMap.put("thumb_image", "default");

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()){

                                 mProgressDialog.dismiss();
                                 Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                 mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 startActivity(mainIntent);
                                 finish();
                             }
                        }
                    });
                }else {
                    mProgressDialog.hide();
                    Toast.makeText(RegisterActivity.this, R.string.regis_error_registre,Toast.LENGTH_LONG).show();

                }
                //endregion
            }
        });
    }

    //endregion
}
