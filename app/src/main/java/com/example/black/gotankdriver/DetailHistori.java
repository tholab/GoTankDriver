package com.example.black.gotankdriver;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.black.gotankdriver.converter.WrappedResponse;
import com.example.black.gotankdriver.model.PemesanModel;
import com.example.black.gotankdriver.service.HistoriService;
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

public class DetailHistori extends AppCompatActivity implements OnMapReadyCallback {

    TextView namaDetailHistori, alamatDetailHistori, hpDetailHistori, statusDetailHistori;
    HistoriService api;
    Button btn_selesai;
    private PemesanModel mDetailPemesan = new PemesanModel();
    private LinearLayout rootKonfirmasi;

    double latitude ,longitude;
    GoogleMap mMap;
    SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_histori);
        rootKonfirmasi = findViewById(R.id.ln_selesai);
        btn_selesai = findViewById(R.id.btn_selesai);
        btn_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String status = "Selesai".trim();
                konfirmasiSelesai(getId(),status);
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);


        initComponent();
        fetchDataDetailHistori();

    }
    private void initComponent(){
        namaDetailHistori = findViewById(R.id.tvNamaDetailHistori);
        alamatDetailHistori = findViewById(R.id.tvAlamatDetailHistori);
        hpDetailHistori = findViewById(R.id.tvHpDetailHistori);
        statusDetailHistori = findViewById(R.id.tvStatusDetailHistori);
        api = ApiUtils.getHistoriService();
    }

    private String getId(){
        return String.valueOf(getIntent().getIntExtra("ID",0));
    }

    private void konfirmasiSelesai(String id, String status){
        api.konfirmasiSelesai(id,status).enqueue(new Callback<WrappedResponse<PemesanModel>>() {
            @Override
            public void onResponse(Call<WrappedResponse<PemesanModel>> call, Response<WrappedResponse<PemesanModel>> response) {
                if (response.isSuccessful()){
                    WrappedResponse<PemesanModel> body = response.body();
                    if (body.getStatus() == 1){
                        mDetailPemesan = body.getData();
                        if(mDetailPemesan != null){
                            Intent intent = new Intent(DetailHistori.this,HistoriActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(DetailHistori.this,"Berkerja Selesai",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(DetailHistori.this,"Pesan Gagal",Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(DetailHistori.this,"Pesan Gagal",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<PemesanModel>> call, Throwable t) {
                Toast.makeText(DetailHistori.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fetchDataDetailHistori(){
        Call<WrappedResponse<PemesanModel>> request = api.detailPemesanHistori(getId());
        request.enqueue(new Callback<WrappedResponse<PemesanModel>>() {
            @Override
            public void onResponse(Call<WrappedResponse<PemesanModel>> call, Response<WrappedResponse<PemesanModel>> response) {

                if (response.isSuccessful()){
                    WrappedResponse body = response.body();
                    if (body.getStatus() == 1){
                        if(body.getData() != null){
                            PemesanModel pemesanDetailHistori = (PemesanModel) body.getData();
                            fillDetailPemesan(pemesanDetailHistori);
                        }
                    }
                }else {
                    Toast.makeText(DetailHistori.this, "Cannot get data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<PemesanModel>> call, Throwable t) {
                Toast.makeText(DetailHistori.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillDetailPemesan(PemesanModel pemesanModel){
        namaDetailHistori.setText(pemesanModel.getName());
        alamatDetailHistori.setText(pemesanModel.getAddress());
        hpDetailHistori.setText(pemesanModel.getPhone());
        statusDetailHistori.setText(pemesanModel.getStatus());
        if(pemesanModel.getStatus().equals("Selesai")){
            rootKonfirmasi.setVisibility(View.GONE);
        }else{
            rootKonfirmasi.setVisibility(View.VISIBLE);
        }
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


    public void btn_map(View view) {
        String label = namaDetailHistori.getText().toString();
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
