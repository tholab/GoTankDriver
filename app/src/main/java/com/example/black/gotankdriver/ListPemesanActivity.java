package com.example.black.gotankdriver;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.black.gotankdriver.adapter.ListAdapter;
import com.example.black.gotankdriver.converter.WrappedListResponse;
import com.example.black.gotankdriver.model.PemesanModel;
import com.example.black.gotankdriver.service.PemesanService;
import com.example.black.gotankdriver.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPemesanActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ListAdapter adapter;
    List<PemesanModel> productPemesanList;
    private PemesanService pemesanService = ApiUtils.getPemesanService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pemesan);
        productPemesanList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewPemesan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        pemesanService.showPemesan(id).enqueue(new Callback<WrappedListResponse<PemesanModel>>() {
            @Override
            public void onResponse(Call<WrappedListResponse<PemesanModel>> call, Response<WrappedListResponse<PemesanModel>> response) {
                if (response.isSuccessful()){
                    productPemesanList.clear();
                    WrappedListResponse body = response.body();
                    if(body != null && body.getStatus() == 1){
                        productPemesanList = body.getData();
                        recyclerView.setAdapter(new ListAdapter( ListPemesanActivity.this, productPemesanList));
                    }else {
                        //Belum ada data
                        Toast.makeText(ListPemesanActivity.this, "Belum Ada Data", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ListPemesanActivity.this, "Kesalahan saat mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedListResponse<PemesanModel>> call, Throwable t) {
//                Log.d("GINK", t.getMessage());
                Toast.makeText(ListPemesanActivity.this, "Tidak dapat mengambil data dari server", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
