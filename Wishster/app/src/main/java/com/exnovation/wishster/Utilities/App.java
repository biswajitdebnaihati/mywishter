package com.exnovation.wishster.Utilities;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class App {
    public static int isForgot = 0;
    public static String UserId="";
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static String fullname = "^[a-zA-Z\\s]+";
    public static String date_pattern = "^\\d{2}-\\d{2}-\\d{4}$";
    public static String token = "";
    public static boolean bool =false;
    public static boolean isEmail =false;
    public static boolean isPhone = false;
    public static boolean loginStatus = false;
    public static boolean isAdult = false;
    public static GoogleSignInClient mGoogleSignInClient;
    public static boolean social_types=false;;
}
