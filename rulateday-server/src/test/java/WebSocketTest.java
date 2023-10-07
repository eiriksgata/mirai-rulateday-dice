import com.github.eiriksgata.rulateday.websocket.client.WebSocketClientInit;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

public class WebSocketTest {

    @Test
    void run() throws URISyntaxException, InterruptedException {
        WebSocketClientInit.run();


        Thread.sleep(1000 * 60 * 60 * 5);


    }

    @Test
    void error(){
        String attribute = "san60力量50";
        if (attribute.matches("san\\d{1,3}")) {
            System.out.println("error");
        }
    }
}
