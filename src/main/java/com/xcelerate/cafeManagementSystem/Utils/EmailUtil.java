package com.xcelerate.cafeManagementSystem.Utils;

import java.net.HttpURLConnection;
import java.net.URL;

public class EmailUtil {
    public static boolean verifyEmailFormat(String email) {
        return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }
}
