package edu.escuelaing.co.demosecuritylogin;

import com.google.gson.Gson;
import edu.escuelaing.co.demosecuritylogin.model.User;
import spark.Request;
import spark.Response;
import spark.staticfiles.StaticFilesConfiguration;


import static edu.escuelaing.co.demosecuritylogin.hashservice.convertPass;
import static spark.Spark.*;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class loginSpark {
    public static void main( String[] args ) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {

        port(getPort());

        secure("keyscerts/ecikeystore.p12","587Ujn.*+","keyscerts/myTrustStore","587Ujn.*+");
        Map<String, String> users = new HashMap<>();
        users.put("test@mail.com", convertPass("123"));
        SecureURLReader.main();
        Gson gson = new Gson();

        before("protected/*", (req, response) -> {
            req.session(true);
            if (req.session().isNew()) {
                req.session().attribute("isLogged", false);
            }
            boolean isLogged = req.session().attribute("isLogged");
            if (!isLogged) {
                halt(401, "<h1> 401 NOT AUTHORIZED </h1>");
            }
        });


        before("/index.html", ((req, response) -> {
            req.session(true);
            if (req.session().isNew()) {
                req.session().attribute("isLogged", false);
            }
            boolean isLogged = req.session().attribute("isLogged");
            if (isLogged) {
                response.redirect("protected/index.html");
            }
        }));


        StaticFilesConfiguration staticHandler = new StaticFilesConfiguration();
        staticHandler.configure("/static");

        before((request, response) ->
                staticHandler.consume(request.raw(), response.raw()));

        get("/",((request, response) -> {
            response.redirect("index.html");
            return "";
        }));

        get("/areyouhere",((request, response) -> {
            return "HOLAAA";
        }));

        get("/logout",((request, response) -> {
            request.session().attribute("isLogged",false);
            return "";
        }));

        post("/login", (request, response) -> {
            request.body();
            request.session(true);
            User user = gson.fromJson(request.body(), User.class);
            if (convertPass(user.getPassword()).equals(users.get(user.getUsername()))) {
                request.session().attribute("isLogged", true);
            } else {
                return "Invalid Username or password ";
            }
            return "";
        });

//        get("/protected/service",(request, response) -> SecureURLReader.readURL("https://ec2-54-242-161-34.compute-1.amazonaws.com:9002/hello"));

    }

    /**
     * @param req This is the object that represents the HTTP request
     *            in order to retrieve a resource from the web server.
     * @param res This is the object that represents the HTTP response
     *            given by the webserver
     * @return A string with html code that will build the web page, in this case
     *          the resource located at /inputdata
     */
    private static String inputDataPage(Request req, Response res) {
        ArrayList<User> users = new ArrayList<>();
        res.header("Content-Type","application/json");
        return new Gson().toJson(users);
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

}
