package trpg.game;

import com.alibaba.fastjson2.TypeReference;
import com.github.eiriksgata.rulateday.trpggame.RoleDataHandler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

public class RoleDataHandlerTest {


    @Test
    void attribute() {

        System.out.println(RoleDataHandler.attributeAdd("力量20敏捷10", "敏捷", "50"));
        System.out.println(RoleDataHandler.attributeSet("力量40", "敏捷", "50"));
        System.out.println(RoleDataHandler.attributeReduce("敏捷40力量50", "敏捷", "50"));
    }

    @Test
    void typeTest() {
        TypeReference type = new TypeReference<Map<String, String>>() {
        };
        System.out.println(convert(type));

    }

    public static <T> Class<T> convert(TypeReference<T> ref) {
        return (Class<T>) ((ParameterizedType) ref.getType()).getRawType();
    }
}
