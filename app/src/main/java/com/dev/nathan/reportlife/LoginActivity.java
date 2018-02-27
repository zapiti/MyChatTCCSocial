package com.dev.nathan.reportlife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity {

    //region <!Declaraçao de input e buttons !>
    private TextInputEditText mLoginEmail;
    private TextInputEditText mLoginPassword;
    private Button mLoginBtn;
    //endregion

    //region <!Declaraçao de Firebase Autenticaçao e progressdialog !>
    private FirebaseAuth mAuth;
    private ProgressDialog mLoginProgressDialog;
    //endregion

    //region <!Declaraçao toolbar!>
    private Toolbar mToolbar;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //region <!Firebase Autenticaçao instancia !>
        mAuth = FirebaseAuth.getInstance();
        //endregion

        //region <!ProgressDialog!>
        mLoginProgressDialog = new ProgressDialog(this);
        //endregion

        //region <!Toolbar set !>
        mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.regisToolbarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //endregion


        //region <!Busca display e fields !>
        mLoginEmail = (TextInputEditText)  findViewById(R.id.login_email);
        mLoginPassword =  (TextInputEditText) findViewById(R.id.login_password);
        mLoginBtn = (Button)  findViewById(R.id.login_btn);
        //endregion

        //region <!Evento de click do botao de login!>
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region <!Pegando valores dos inputs!>
                String email = mLoginEmail.getText().toString();
                String password = mLoginPassword.getText().toString();
                //endregion

                //region <!Verificando se os campos estao vazios e chamando o metodo de login de usuario!>
                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    mLoginProgressDialog.setTitle(getString(R.string.logging_in));
                    mLoginProgressDialog.setMessage(getString(R.string.logging_check_msg));
                    mLoginProgressDialog.setCanceledOnTouchOutside(false);
                    mLoginProgressDialog.show();
                    loginUser(email,password);
                }
                //endregion
            }
        });
        //endregion
    }

    //region  <!Funçao de autenticaçao de usuario e verificaçao de sucesso de login!>
    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //region  <!verificaçao de requisiçao!>
                if (task.isSuccessful()){
                    mLoginProgressDialog.dismiss();
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    //limpa tarefa anterior
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }else {
                    mLoginProgressDialog.hide();
                    Toast.makeText(LoginActivity.this, R.string.login_error,Toast.LENGTH_LONG).show();

                }
                //endregion
            }
        });
    }
    //endregion
}
