package demo;
import com.google.gson.*;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GitController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping("/")
    public String index(){
        return "Hi, try using /repositories/{owner}/{repositoryName} endpoint :)";
    }

    @RequestMapping("/repositories/{owner}/{repositoryName}")
    public String getRepoDescription(@PathVariable("owner") String owner, @PathVariable("repositoryName") String repo) {
        GitQuery gitQuery = new GitQuery();

        String resultJSON = "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();

        try {
            resultJSON = gitQuery.getRepositoryDescription(owner, repo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonElement je = jp.parse(resultJSON);
        return gson.toJson(je);
    }
    @RequestMapping(value = PATH)
    public String error(){
        return "Error handling, probably wrong endpoint used";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
