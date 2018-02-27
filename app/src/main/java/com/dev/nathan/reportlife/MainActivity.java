package com.dev.nathan.reportlife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //region <!Declaraçao de variaveis!>
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        //region <!Toolbar set !>
        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.mainToolbarTitle);
        //endregion

    }
    @Override
    public void onStart() {
        super.onStart();
        //region  <!Check se o usuario esta logado ou nao e se ele nao estiver chama a funcao sendToStart !>
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
           sendToStart();
        }
        //endregion

    }

    //region  <!Funcao vai para tela de start!>
    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }
    //endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }

        return true;
    }
}
