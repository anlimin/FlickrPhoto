package ca.limin.flickrphoto.json_parser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import ca.limin.flickrphoto.model.Photo;

public class JsonParser {
    public static String inputToString(String url) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpsURLConnection httpsURLConnection;

        try {
            httpsURLConnection = (HttpsURLConnection)new URL(url).openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static List<Photo> parseJson(String str) {
        List<Photo> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String title = item.getString("title");
                String link = item.getString("link");
                JSONObject mediaObject = item.getJSONObject("media");
                String media = mediaObject.getString("m");
                String date = item.getString("date_taken");
                String published = item.getString("published");
                String author = item.getString("author");
                String authorId = item.getString("author_id");
                String tag = item.getString("tags");
                list.add(new Photo(title, link, media, date,published, author, authorId, tag));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
