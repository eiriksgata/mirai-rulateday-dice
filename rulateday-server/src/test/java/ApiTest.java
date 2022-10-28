import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import indi.eiriksgata.rulateday.utlis.RestUtil;
import org.junit.jupiter.api.Test;

public class ApiTest {


    @Test
    void youduApiTest() {
        String url = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=";
        String cn = "肌肉，胸肌，络腮胡";
        String result = RestUtil.get(url + cn);
        System.out.println(result);
        JSONObject jsonObject = JSON.parseObject(result);
        String en = jsonObject.getJSONArray("translateResult").getJSONArray(0).getJSONObject(0).getString("tgt");
        System.out.println(en);

    }
}
