import com.github.eiriksgata.rulateday.service.impl.UserInitiativeServerImpl;
import org.junit.jupiter.api.Test;

public class InitiativeAtkTest {

    @Test
    void showLIst() {
        String result = new UserInitiativeServerImpl().showInitiativeList("1001");
        System.out.println(result);
    }

    @Test
    void addAtk() {
        new UserInitiativeServerImpl().addInitiativeDice("1001", 2353686862L, "eiriksgata", 23);
    }


}
