package com.digent.tim.digenttracker;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by tim on 02.05.16.
 */
public class TheTVDBInterface extends Application {
    private JSONObject loginCredentials = new JSONObject();
    private JSONObject jwtToken = null;

    public TheTVDBInterface() {
        try {
            loginCredentials.put("apikey", "E0C669A56BF70871");
            loginCredentials.put("username", "timteige");
            loginCredentials.put("userkey", "17F1A8EDC10E17A6");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJwtToken() {
        return jwtToken;
    }

    public void loginTVDB() {
        if (jwtToken == null)
            new TheTVDBLogin().execute();
    }

    public void searchTVDB(TVDBActivty activity, String path, String query) {
        new TheTVDBSearch(activity).execute(path, query);
    }

    public void getGraphicalInformation(TVDBActivty activity, String id, String keyType, String subKey) {
        new TheTVDBGetGraphical(activity).execute(id, keyType, subKey);
    }

    private void createHeader(HttpURLConnection connection) {
        try {
            String bearerAuth = "Bearer " + jwtToken.get("token").toString();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", bearerAuth);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Language", "en-US");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class TheTVDBLogin extends AsyncTask<Void, Integer, Long> {

        @Override
        protected Long doInBackground(Void... params) {
            String targetURL = "https://api.thetvdb.com/login";
            HttpsURLConnection connection = null;
            try {
                URL url = new URL(targetURL);
                connection = (HttpsURLConnection)url.openConnection();

                connection.setDoOutput(true);
                connection.setRequestMethod( "POST" );
                connection.setRequestProperty( "Content-Type", "application/json");
                int contentLength = loginCredentials.toString().length();
                connection.setRequestProperty( "Content-Length", Integer.toString(contentLength));
                connection.setChunkedStreamingMode(0);
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());

                out.writeBytes(loginCredentials.toString());
                out.flush();
                out.close();

                int respCode = connection.getResponseCode();
                if (respCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String respLine;
                    StringBuilder resp = new StringBuilder();
                    while ((respLine = in.readLine()) != null) {
                        resp.append(respLine);
                    }
                    in.close();

                    jwtToken = new JSONObject(resp.toString());
                }


            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
                Log.d(getClass().getName(), jwtToken.toString());
            }
            return null;
        }
    }

    private class TheTVDBSearch extends AsyncTask<String, Integer, String> {
        private String searchResult;
        private TVDBActivty activity;
        ProgressDialog progressDialog;
        private HttpURLConnection connection = null;

        public TheTVDBSearch(TVDBActivty obj) {
            activity = obj;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String fixedQuery = params[1].replace(" ", "%20");
                URL url = new URL(params[0] + fixedQuery);
                connection = (HttpURLConnection)url.openConnection();
                createHeader(connection);

                int status = connection.getResponseCode();

                if (status == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder resp = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        resp.append(line);
                    }
                    in.close();

                    searchResult = resp.toString();
                }
                return searchResult;

            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Error, reason: " + e);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        protected void onPostExecute(String result) {
            this.activity.setSearchResult(result);
            progressDialog.cancel();
        }
    }

    private class TheTVDBGetGraphical extends AsyncTask<String, Integer, String> {
        private String searchResult;
        private TVDBActivty activity;
        ProgressDialog progressDialog;
        private HttpURLConnection connection = null;

        public TheTVDBGetGraphical(TVDBActivty obj) {
            activity = obj;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Downloading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url;
                if (params[2].equals("")) {
                    url = new URL("https://api.thetvdb.com/series/" + params[0] + "/images/query?keyType=" + params[1]);
                } else {
                    url = new URL("https://api.thetvdb.com/series/" + params[0] + "/images/query?keyType=" + params[1] + "&subKey=" + params[2]);
                }
                connection = (HttpURLConnection) url.openConnection();
                createHeader(connection);

                int status = connection.getResponseCode();

                if (status == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    StringBuilder resp = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        resp.append(line);
                    }
                    in.close();

                    searchResult = resp.toString();
                }

                return searchResult;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        protected void onPostExecute(String result) {
            this.activity.setGraphicalInformation(result);
            progressDialog.cancel();
        }
    }
}
