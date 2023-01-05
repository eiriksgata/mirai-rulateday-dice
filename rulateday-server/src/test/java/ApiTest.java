import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import indi.eiriksgata.rulateday.utlis.RestUtil;
import org.junit.jupiter.api.Test;

public class ApiTest {


    @Test
    void youduApiTest() {
        String url = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=";
        String cn = "";
        String result = RestUtil.get(url + cn);
        System.out.println(result);
        JSONObject jsonObject = JSON.parseObject(result);
        String en = jsonObject.getJSONArray("translateResult").getJSONArray(0).getJSONObject(0).getString("tgt");
        System.out.println(en);

    }

    @Test
    void yinhuaTest(){
        String result = RestUtil.get("https://www.dmoe.cc/random.php?return=json");
        System.out.println(result);
    }

    @Test
    void xiaowaiTest(){
        String result = RestUtil.get("https://api.ixiaowai.cn/api/api.php");
        System.out.println(result);
    }

    @Test
    void suiyuexiaozhu(){
        String result = RestUtil.get("https://api.ixiaowai.cn/api/api.php");
    }
}
