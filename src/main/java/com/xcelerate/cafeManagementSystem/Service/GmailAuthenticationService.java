package com.xcelerate.cafeManagementSystem.Service;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Set;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;

@Service
public class GmailAuthenticationService {

    private static Gmail service;

    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(GmailAuthenticationService.class.getResourceAsStream("/credentials.json")));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static Gmail getService() {
        if(service == null) {
            try {
                NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                service = new Gmail.Builder(httpTransport, GsonFactory.getDefaultInstance(), getCredentials(httpTransport, GsonFactory.getDefaultInstance()))
                        .setApplicationName("Cafe Management System")
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return service;
    }

}