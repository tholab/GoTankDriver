package com.example.black.gotankdriver.utils;

import com.example.black.gotankdriver.network.RetrofitClient;
//import com.example.black.gotankdriver.service.CompanyService;
import com.example.black.gotankdriver.service.DriverService;
import com.example.black.gotankdriver.service.HistoriService;
import com.example.black.gotankdriver.service.PemesanService;

public class ApiUtils {
    private ApiUtils(){

    }

//    //pake ini untuk koneksi heroku
//    private static final String API_URL = "https://gotank.herokuapp.com/api/";
//    public static final String ENDPOINT = "https://gotank.herokuapp.com/";


    public static final String ENDPOINT = "http://192.168.43.108:81/";
    private static final String API_URL = "http://192.168.43.108:81/api/";

    public static final DriverService getDriverService(){
        return RetrofitClient.getClient(API_URL).create(DriverService.class);
    }
    public static final PemesanService getPemesanService(){
        return RetrofitClient.getClient(API_URL).create(PemesanService.class);
    }
    public static final HistoriService getHistoriService(){
        return RetrofitClient.getClient(API_URL).create(HistoriService.class);
    }

}
