package edu.escuelaing.co.demosecurityservice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static spark.Spark.*;

public class service {

    public static void main(String[] args) {
        secure("keyscerts/ecikeystore.p12","587Ujn.*+","keyscerts/myTrustStore","587Ujn.*+");
        port(getPort());
        get("/helloSecureService", (req, res) -> "Hello From Secure Service,today is "+now());
    }
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
    private static String now() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}

