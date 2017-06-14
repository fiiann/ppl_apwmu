package com.mcn.apwmu.app;

/**
 * Created by root on 5/6/17.
 */

public class AppConfig {
    public String dashboard_nim;
    public static final String LOGIN_URL = "http://192.168.1.31/kuliah/ppl/api/login.php";

    // Server user register url
    public static final String REGISITER_URL = "http://192.168.1.31/kuliah/ppl/api/register.php";
    String url = "http://192.168.1.31/kuliah/ppl/driver_api/detail.php?nim="+dashboard_nim;

}
