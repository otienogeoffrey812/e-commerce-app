package com.example.slickkwear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.io.UnsupportedEncodingException;

public class SmsGateway {


    private String mBaseUrl;
    private int mPartnerId;
    private String mApiKey;
    private String mShortCode;
    private HttpURLConnection connection;

    public SmsGateway(String baseUrl, int partnerId, String apiKey, String shortCode) {
        this.mBaseUrl = baseUrl;
        this.mPartnerId = partnerId;
        this.mApiKey = apiKey;
        this.mShortCode = shortCode;
    }


    private String getFinalURL(String mobile, String message) {

        String encodedMessage = null;
        try {
            encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String encodedMobiles = null;
        try {
            encodedMobiles = URLEncoder.encode(mobile, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return mBaseUrl + "?apikey=" + mApiKey + "&partnerID=" + mPartnerId + "&shortcode=" +
                mShortCode + "&mobile=" + encodedMobiles + "&message=" + encodedMessage;
    }

    public String sendSingleSms(String message, String mobile) throws IOException {

        String finalUrl = getFinalURL(mobile, message);

        return makeHttpGetRequest(finalUrl);
    }

    public String sendBulkSms(String message, String[] mobiles) throws IOException {

        String numbers = Arrays.toString(mobiles)
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");

        String finalUrl = getFinalURL(numbers, message);

        return makeHttpGetRequest(finalUrl);
    }


    private String makeHttpGetRequest(String urlString) throws IOException {
        URL url = makeURL(urlString);

        if (connection == null) {

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
        }

        StringBuilder content;

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;

        content = new StringBuilder();

        while ((line = in.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
        }

        return content.toString();
    }


    private URL makeURL(String urlString) throws MalformedURLException {
        return new URL(urlString);
    }
}