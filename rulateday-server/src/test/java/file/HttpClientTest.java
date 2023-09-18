package file;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class HttpClientTest {

    public File downloadFile(String url) {
        try {
            HttpRequest request = HttpRequest.get(url);
            File file = null;
            if (request.ok()) {
                file = File.createTempFile("download-" + UUID.randomUUID(), ".tmp");
                request.receive(file);
            }
            return file;
        } catch (HttpRequest.HttpRequestException | IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }


}
