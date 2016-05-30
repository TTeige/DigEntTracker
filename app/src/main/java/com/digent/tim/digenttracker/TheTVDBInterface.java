package com.digent.tim.digenttracker;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public void loginTVDB() {
        if (jwtToken == null)
            new TheTVDBLogin().execute();
    }

    public void searchTVDB(TVDBActivty activity, String path, String query) {
        new TheTVDBSearch(activity).execute(path, query);
    }

    public void seriesSearchTVDB(TVDBActivty activity, String path, String query) {
        new TheTVDBSearchSeries(activity).execute(path, query);
    }

    public void actorSearchTVDB(TVDBActivty activity, String path, String query) {
        new TheTVDBSearchActors(activity).execute(path, query);
    }

    public void bannerSearch(ActorActivity activity, String path, String query) {
        new TheTVDBSearchBanner(activity).execute(path, query);
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

    private class TheTVDBSearch extends AsyncTask<String, Integer, SearchResult> {
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
        protected SearchResult doInBackground(String... params) {
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
                return new SearchResult(new JSONObject(searchResult), null, null, null);

            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Error, reason: " + e);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        protected void onPostExecute(SearchResult result) {
            this.activity.setSearchResult(result);
            progressDialog.cancel();
        }
    }

    private class TheTVDBSearchSeries extends AsyncTask<String, Integer, SearchResult> {
        private JSONObject searchResult;
        private JSONObject graphicalInformation;
        private TVDBActivty activity;
        private Bitmap image = null;
        ProgressDialog progressDialog;
        private HttpURLConnection connection = null;

        public TheTVDBSearchSeries(TVDBActivty obj) {
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
        protected SearchResult doInBackground(String... params) {
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

                    searchResult = new JSONObject(resp.toString());
                    searchResult = searchResult.getJSONObject("data");
                }

            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Error, reason: " + e);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            try {
                URL url = new URL("https://api.thetvdb.com/series/" + searchResult.getString("id") + "/images/query?keyType=" + "series");
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

                    graphicalInformation = new JSONObject(resp.toString());
                }
            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Error, reason: " + e);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            try {
                URL url = new URL("http://api.thetvdb.com/banners/" + graphicalInformation.getJSONArray("data").getJSONObject(0).getString("fileName"));

                connection = (HttpURLConnection) url.openConnection();
                createHeader(connection);

                int status = connection.getResponseCode();

                if (status == 200) {
                    InputStream in = connection.getInputStream();
                    image = BitmapFactory.decodeStream(in);
                }

                return new SearchResult(searchResult, graphicalInformation, null, image);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        protected void onPostExecute(SearchResult result) {
            this.activity.setSearchResult(result);
            progressDialog.cancel();
        }
    }

    private class TheTVDBSearchActors extends AsyncTask<String, Integer, SearchResult> {

        private TVDBActivty activity;
        private ProgressDialog progressDialog;
        private JSONObject actors;
        private HttpURLConnection connection = null;

        public TheTVDBSearchActors(TVDBActivty obj) {
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
        protected SearchResult doInBackground(String... params) {
            try {
                URL url = new URL(params[0] + params[1] + "/actors");
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

                    actors = new JSONObject(resp.toString());
                }

            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Error, reason: " + e);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return new SearchResult(null, null, actors, null);
        }

        protected void onPostExecute(SearchResult result) {
            this.activity.setSearchResult(result);
            progressDialog.cancel();
        }
    }

    private class TheTVDBSearchBanner extends AsyncTask<String, Integer, Bitmap> {

        private ActorActivity activity;
        private ProgressDialog progressDialog;
        private Bitmap banner;
        private HttpURLConnection connection = null;

        public TheTVDBSearchBanner(ActorActivity obj) {
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
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0] + params[1]);

                connection = (HttpURLConnection) url.openConnection();
                createHeader(connection);

                int status = connection.getResponseCode();

                if (status == 200) {
                    InputStream in = connection.getInputStream();
                    banner = BitmapFactory.decodeStream(in);
                }

                return banner;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        protected void onPostExecute(Bitmap result) {
            this.activity.setActorImage(result);
            progressDialog.cancel();
        }
    }
}
