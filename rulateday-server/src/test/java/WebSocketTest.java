import indi.eiriksgata.rulateday.websocket.client.WebSocketClientInit;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

public class WebSocketTest {

    @Test
    void run() throws URISyntaxException, InterruptedException {
        WebSocketClientInit.run();


        Thread.sleep(1000 * 60 * 60 * 5);


    }
}
