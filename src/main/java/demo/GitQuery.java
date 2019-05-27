package demo;

import com.google.gson.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

class GitQuery {

    String getRepositoryDescription(String owner, String repoName) throws IOException {

        URL url = new URL("https://api.github.com/repos/" + owner + "/"+ repoName);
        URLConnection request = url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonObject = root.getAsJsonObject();
        return parseJson(jsonObject);
    }

    String parseJson(JsonObject jsonObject) {
        Map<String, JsonElement> map = new LinkedHashMap<>();
        map.put("fullName", jsonObject.get("full_name"));
        map.put("description", jsonObject.get("description"));
        map.put("cloneUrl", jsonObject.get("clone_url"));
        map.put("stars", jsonObject.get("stargazers_count"));
        map.put("createdAt", jsonObject.get("created_at"));

        GsonBuilder gsonMapBuilder = new GsonBuilder();
        Gson gsonObject = gsonMapBuilder.setPrettyPrinting().create();

        return gsonObject.toJson(map);
    }
}
