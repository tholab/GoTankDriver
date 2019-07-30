package com.example.black.gotankdriver;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.black.gotankdriver.converter.WrappedResponse;
import com.example.black.gotankdriver.model.PemesanModel;
import com.example.black.gotankdriver.service.PemesanService;
import com.example.black.gotankdriver.utils.ApiUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemesan extends AppCompatActivity implements OnMapReadyCallback {

    TextView namaPemesan, alamatPemesan, hpPemesan;
    Button btnAcc;
    PemesanService api;
    private PemesanModel mPemesan = new PemesanModel();

    double latitude ,longitude;
    GoogleMap mMap;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesan);
        btnAcc = findViewById(R.id.btn_acc);
        btnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String status = "Sedang Dikerjakan".trim();
                konfirmasiPemesan(getId(),status);
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        initComponent();
        fetchDataDetailPemesan();
    }

    private void initComponent(){
        namaPemesan = findViewById(R.id.tvNamaDetailPemesan);
        alamatPemesan = findViewById(R.id.tvAlamatDetailPemesan);
        hpPemesan = findViewById(R.id.tvHpDetailPemesan);
        api = ApiUtils.getPemesanService();
    }

    private String getId(){
        return String.valueOf(getIntent().getIntExtra("ID",0));
    }

    private void konfirmasiPemesan(String id, String status){
//        Call<WrappedResponse<PemesanModel>> request = api.konfirmasiKerja(getId());
        api.konfirmasiKerja(id,status).enqueue(new Callback<WrappedResponse<PemesanModel>>() {
            @Override
            public void onResponse(Call<WrappedResponse<PemesanModel>> call, Response<WrappedResponse<PemesanModel>> response) {
                if (response.isSuccessful()){
                    WrappedResponse<PemesanModel> body = response.body();
                    if (body.getStatus() == 1){
                        mPemesan = body.getData();
                        if(mPemesan != null){
                            Intent intent = new Intent(DetailPemesan.this,HistoriActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(DetailPemesan.this,"Selamat Berkerja",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(DetailPemesan.this,"Pesan Gagal",Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(DetailPemesan.this,"Pesan Gagal",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<PemesanModel>> call, Throwable t) {
                Toast.makeText(DetailPemesan.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDataDetailPemesan(){
        Call<WrappedResponse<PemesanModel>> request = api.detailPemesan(getId());
        request.enqueue(new Callback<WrappedResponse<PemesanModel>>() {
            @Override
            public void onResponse(Call<WrappedResponse<PemesanModel>> call, Response<WrappedResponse<PemesanModel>> response) {
                if (response.isSuccessful()){
                    WrappedResponse body = response.body();
                    if (body.getStatus() == 1){
                        if(body.getData() != null){
                            PemesanModel pemesanDetail = (PemesanModel) body.getData();
                            fillDetailPemesan(pemesanDetail);
                        }
                    }
                }else {
                    Toast.makeText(DetailPemesan.this, "Cannot get data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<PemesanModel>> call, Throwable t) {
                Toast.makeText(DetailPemesan.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fillDetailPemesan(PemesanModel pemesanModel){
        namaPemesan.setText(pemesanModel.getName());
        alamatPemesan.setText(pemesanModel.getAddress());
        hpPemesan.setText(pemesanModel.getPhone());
        latitude = pemesanModel.getLatitude();
        longitude = pemesanModel.getLongitude();

        mMap.clear();
        LatLng point = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions marker = new MarkerOptions().position(point)
                .title("Lokasi Pemesan");
        mMap.addMarker(marker);
    }

    public void btn_track(View view) {
        String label = namaPemesan.getText().toString();
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(mapIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
    }
}
