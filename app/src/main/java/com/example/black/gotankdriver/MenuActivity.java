package com.example.black.gotankdriver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        settings = getSharedPreferences("TOKEN",MODE_PRIVATE);

    }

    @Override
    protected void onResume(){
        super.onResume();
        if (isNotLoggedIn()){
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            finish();
        }
    }

    private boolean isNotLoggedIn() {
        String token = settings.getString("TOKEN","UNDEFINED");
        return token == null || token.equals("UNDEFINED");

    }

    public void bantuan(View view) {
        Intent intent = new Intent(MenuActivity.this,BantuanActivity.class);
        startActivity(intent);
    }

    public void pengaturan(View view) {
        Intent intent = new Intent(MenuActivity.this,PengaturanActivity.class);
        startActivity(intent);
    }

    public void list_pemesan(View view) {
        Intent intent = new Intent(MenuActivity.this,ListPemesanActivity.class);
        startActivity(intent);
    }

    public void histori(View view) {
        Intent intent = new Intent(MenuActivity.this,HistoriActivity.class);
        startActivity(intent);
    }
}
