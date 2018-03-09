package hackersweek.a3in.com.apphackersweek.tenor.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Adrian on 09/03/2018.
 */

public class Tenor {
    private static final String APITenor = "QI846KU84QP7";

    public static JSONObject getSearchResults(String anonId, String searchTerm) {

        // make search request - using default locale of EN_US
        final String url = String.format("https://api.tenor.com/v1/random?q=%1$s&key=%2$s&anon_id=%3$s&limit=%4$s",
                searchTerm, APITenor, anonId, 1);
        try {
            return get(url);
        } catch (IOException | JSONException ignored) {
        }
        return null;
    }

    public static String getAnonId() {
        final String url = String.format("https://api.tenor.com/v1/anonid?key=%s", APITenor);
        try {
            JSONObject response = get(url);
            return response.getString("anon_id");
        } catch (IOException | JSONException ignored) {
        }
        return "";
    }

    private static JSONObject get(String url) throws IOException, JSONException {
        HttpURLConnection connection = null;
        try {
            // Get request
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Handle failure
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK && statusCode != HttpURLConnection.HTTP_CREATED) {
                String error = String.format("HTTP Code: '%1$s' from '%2$s'", statusCode, url);
                throw new ConnectException(error);
            }

            // Parse response
            return parser(connection);
        } catch (Exception ignored) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return new JSONObject("");
    }

    private static JSONObject parser(HttpURLConnection connection) throws JSONException {
        char[] buffer = new char[1024 * 4];
        int n;
        InputStream stream = null;
        try {
            stream = new BufferedInputStream(connection.getInputStream());
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            StringWriter writer = new StringWriter();
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
            return new JSONObject(writer.toString());
        } catch (IOException ignored) {
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return new JSONObject("");
    }

    public static String getGifUrl(JSONObject jsonObject){
        try {
            JSONArray results = jsonObject.getJSONArray("results");
            JSONObject response = results.getJSONObject(0);
            JSONArray media = response.getJSONArray("media");
            JSONObject media0 = media.getJSONObject(0);
            JSONObject gif = media0.getJSONObject("nanogif");
            return gif.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
