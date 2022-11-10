package trpg.game;

import indi.eiriksgata.rulateday.trpggame.RoleDataHandler;
import org.junit.jupiter.api.Test;

public class RoleDataHandlerTest {


    @Test
    void attribute() {

        System.out.println(RoleDataHandler.attributeAdd("力量20敏捷10", "敏捷", "50"));
        System.out.println(RoleDataHandler.attributeSet("力量40", "敏捷", "50"));
        System.out.println(RoleDataHandler.attributeReduce("敏捷40力量50", "敏捷", "50"));


    }
}
