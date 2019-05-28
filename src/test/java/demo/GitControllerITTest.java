package demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitControllerITTest {

    @LocalServerPort
    private int port;
    private URL base;
    private URL getDescriptionURL;
    private URL wrongURL;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        this.getDescriptionURL = new URL("http://localhost:" + port + "/repositories/twbs/bootstrap");
        this.wrongURL = new URL("http://localhost:"+ port + "/wrong/endpoint");
    }

    @Test
    public void getHello() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        assertThat(response.getBody(), equalTo("Hi, try using /repositories/{owner}/{repositoryName} endpoint :)"));
    }

    @Test
    public void getDescription() {
        ResponseEntity<String> response = template.getForEntity(getDescriptionURL.toString(), String.class);
        assertThat(response.getBody(), containsString("\"fullName\": \"twbs/bootstrap\""));
        assertThat(response.getBody(), containsString("\"description\": \"The most popular HTML, CSS, and JavaScript"));
        assertThat(response.getBody(), containsString("\"cloneUrl\": \"https://github.com/twbs/bootstrap.git\""));
        assertThat(response.getBody(), containsString("\"stars\": "));
        assertThat(response.getBody(), containsString("\"createdAt\": \"2011-07-29T21:19:00Z\"\n"));
    }
    @Test
    public void errorHandling(){
        ResponseEntity<String> responseEntity = template.getForEntity(wrongURL.toString(), String.class);
        assertThat(responseEntity.getBody(), equalTo("Error handling, probably wrong endpoint used"));
    }
}
