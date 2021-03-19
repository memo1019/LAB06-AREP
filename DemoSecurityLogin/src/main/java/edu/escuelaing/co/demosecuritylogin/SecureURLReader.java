package edu.escuelaing.co.demosecuritylogin;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class SecureURLReader {
    public static void main() throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, KeyManagementException {

        // Create a file and a password representation
        File trustStoreFile = new File("keyscerts/myTrustStore");
        char[] trustStorePassword = "587Ujn.*+".toCharArray();
        // Load the trust store, the default type is "pkcs12", the alternative is "jks"
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);
        // Get the singleton instance of the TrustManagerFactory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // Init the TrustManagerFactory using the truststore object
        tmf.init(trustStore);
        //Set the default global SSLContext so all the connections will use it
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);
    }

    public static String readURL(String site) throws IOException {

        // Crea el objeto que representa una URL
        URL siteURL=null;
        try {
            siteURL = new URL(site);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SecureURLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Crea el objeto que URLConnection
        URLConnection urlConnectionsite = siteURL.openConnection();
        // Obtiene los campos del encabezado y los almacena en un estructura Map
        // Obtiene los campos del encabezado y los almacena en un estructura Map
        HttpURLConnection urlConnection = (HttpURLConnection) urlConnectionsite;
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        System.out.println("-------message-from server 2------");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            return response.toString();
        }
    }
}

