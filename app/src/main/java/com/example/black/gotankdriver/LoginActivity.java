package com.example.black.gotankdriver;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.black.gotankdriver.converter.WrappedResponse;
import com.example.black.gotankdriver.model.Driver;
import com.example.black.gotankdriver.service.DriverService;
import com.example.black.gotankdriver.utils.ApiUtils;
import com.example.black.gotankdriver.utils.PrefManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private DriverService userService = ApiUtils.getDriverService();
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private SharedPreferences settings;
    private Driver mDriver = new Driver();
    private static final String TAG = "LoginActivity";
    private ProgressBar mProgressBar;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        getSupportActionBar().hide();
        initComp();

        prefManager = new PrefManager(this);
        requestPermission();

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
//                startActivity(intent);
//            }
//        });
        doLogin();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (!isNotLoggedIn()){
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
            finish();
        }
    }
    private void initComp(){
        etUsername = findViewById(R.id.etUsername);
        etPassword= findViewById(R.id.etPass);
        mProgressBar =findViewById(R.id._loader);
        btnLogin = findViewById(R.id.btn_login);
        settings = getSharedPreferences("TOKEN",MODE_PRIVATE);
    }

    private void doLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = etUsername.getText().toString().trim();
                String p = etPassword.getText().toString().trim();
                if(e.isEmpty()){
                    etUsername.setError("Username harus diisi");
                    etUsername.requestFocus();
                    return;
                }
                if(p.isEmpty()){
                    etPassword.setError("Password harus diisi");
                    etPassword.requestFocus();
                    return;
                }
                if(p.length() < 7 ){
                    etPassword.setError("Password harus minimal 8 character");
                    etPassword.requestFocus();
                    return;
                }

                mProgressBar.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                Call<WrappedResponse<Driver>> user = userService.login(e,p);
                user.enqueue(new Callback<WrappedResponse<Driver>>() {
                    @Override
                    public void onResponse(Call<WrappedResponse<Driver>> call, Response<WrappedResponse<Driver>> response) {
                        if (response.isSuccessful()){
                            WrappedResponse<Driver> body = response.body();
                            if (body.getStatus() == 1){
                                mDriver = body.getData();
                                if (mDriver != null){
                                    System.out.println(mDriver.getApi_token());
                                    setLoggedIn(mDriver.getApi_token(), mDriver.getId());
                                    Toast.makeText(LoginActivity.this,"Login Berhasil",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(LoginActivity.this,"Response success with error", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this,"Login Gagal", Toast.LENGTH_SHORT).show();
                                System.err.println("wwww"+response.body());

                            }
                        }else {
                            Toast.makeText(LoginActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();

                        }
                        mProgressBar.setVisibility(View.INVISIBLE);
                        btnLogin.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<WrappedResponse<Driver>> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,"Cannot request to server. Please check your connection",Toast.LENGTH_SHORT).show();
                        System.err.println("wwwwww"+t.getMessage());
                        mProgressBar.setVisibility(View.INVISIBLE);
                        btnLogin.setEnabled(true);

                    }
                });
           }
        });
    }

    private boolean isNotLoggedIn(){
        String token = settings.getString("TOKEN","UNDEFINED");
        return token == null || token.equals("UNDEFINED");
    }

    private void setLoggedIn (String token, int driver_id){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("TOKEN",token);
        editor.putInt("DRIVER_ID", driver_id);
        editor.commit();
        startActivity(new Intent(LoginActivity.this, MenuActivity.class));
        finish();
    }

    private void requestPermission(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {openMultipleThings();}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {}
        }).check();
    }

    private void openMultipleThings(){
//        Toast.makeText(this,"Sukses",Toast.LENGTH_LONG).show();
    }



}
