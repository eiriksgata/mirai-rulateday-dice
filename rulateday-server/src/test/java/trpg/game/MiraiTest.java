package trpg.game;

import com.alibaba.fastjson.JSONObject;
import indi.eiriksgata.rulateday.trpggame.RoleDataHandler;
import indi.eiriksgata.rulateday.trpggame.TrpgGameServiceImpl;
import indi.eiriksgata.rulateday.trpggame.utils.PlayerRoleAttributeSetUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class MiraiTest {


    @Test
    void run() {
        Long qqId = 2353686862L;

        String attributeSet = "克里斯,侦查80聆听80图书馆80闪避60信用30急救50,力量40体质55体型60敏捷75外貌60智力80意志50教育50";

        String fileName = "古茂密林之中.json";

        String[] inputText = attributeSet.split(",");
        PlayerRoleAttributeSetUtil.nameCheck(qqId, inputText[0]);
        PlayerRoleAttributeSetUtil.skillCheck(qqId, inputText[1]);
        PlayerRoleAttributeSetUtil.attributeCheck(qqId, inputText[2]);

        System.out.println(TrpgGameServiceImpl.loadScriptData(qqId, fileName));

         System.out.println(TrpgGameServiceImpl.loadEventText(qqId));

    }

    @Test
    void stringSplitTest() {
        String inputString = "1";
        String[] list = inputString.split(",");
        System.out.println(list[0]);
    }

    @Test
    void mapsTest() {
        Map<String, String> maps = new HashMap<>();
        maps.put("123", "456");
        maps.forEach((key, value) -> {
            System.out.println(key + "," + value);
        });
    }

    @Test
    void update() {
        System.out.println(RoleDataHandler.update("力量30敏捷50", "力量-10,敏捷+10,理智+10,智力-10"));

    }

    @Test
    void jsonObjectUpdate() {
        JSONObject data = JSONObject.parseObject("{\"name\":\"123\",\"code\":\"200\",\"data\":{\"unit\":\"123\",\"update\":{\"building\":1}}}");

        data.getJSONObject("data").put("unit", "456");
        data.getJSONObject("data").getJSONObject("update").put("building", 5);
        System.out.println(data);

    }

}
