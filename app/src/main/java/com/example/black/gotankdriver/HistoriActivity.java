package com.example.black.gotankdriver;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.black.gotankdriver.adapter.HistoriAdapter;
import com.example.black.gotankdriver.converter.WrappedListResponse;
import com.example.black.gotankdriver.model.PemesanModel;
import com.example.black.gotankdriver.service.HistoriService;
import com.example.black.gotankdriver.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoriActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    HistoriAdapter adapter;
    List<PemesanModel> productHistoriList;
    private HistoriService historiService = ApiUtils.getHistoriService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori);
        productHistoriList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewHistori);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoriAdapter(this,productHistoriList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private int getId(){
        SharedPreferences sp = getSharedPreferences("TOKEN",MODE_PRIVATE);
        int id = sp.getInt("DRIVER_ID", 0);
        return id;
    }

    private void loadData() {
        String id = String.valueOf(getId());
        historiService.showPemesan(id).enqueue(new Callback<WrappedListResponse<PemesanModel>>() {
            @Override
            public void onResponse(Call<WrappedListResponse<PemesanModel>> call, Response<WrappedListResponse<PemesanModel>> response) {
                if (response.isSuccessful()){
                    productHistoriList.clear();
                    WrappedListResponse body = response.body();
                    if(body != null && body.getStatus() == 1){
                        productHistoriList = body.getData();
                        recyclerView.setAdapter(new HistoriAdapter( HistoriActivity.this, productHistoriList));
                    }else {
                        //Belum ada data
                        Toast.makeText(HistoriActivity.this, "Belum Ada Data", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(HistoriActivity.this, "Kesalahan saat mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedListResponse<PemesanModel>> call, Throwable t) {
//                Log.d("GINK", t.getMessage());
                Toast.makeText(HistoriActivity.this, "Tidak dapat mengambil data dari server", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
