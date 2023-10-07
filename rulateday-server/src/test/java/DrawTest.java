import com.github.eiriksgata.trpg.dice.message.handle.InstructHandle;
import com.github.eiriksgata.trpg.dice.vo.MessageData;
import org.junit.jupiter.api.Test;

/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date: 2021/5/10
 **/
public class DrawTest {

    @Test
    void inputData() throws Exception {
        MessageData messageData = new MessageData();
        messageData.setQqID(123456789L);
        InstructHandle instruct = new InstructHandle();
        messageData.setMessage(".cardsAdd扑克Test 红桃9,黑桃9,方块9,梅花9,小王,大王");
        System.out.println(instruct.instructCheck(messageData));
    }

    @Test
    void outData() throws Exception {
        MessageData messageData = new MessageData();
        messageData.setQqID(123456789L);
        InstructHandle instruct = new InstructHandle();
        messageData.setMessage("3456");
        System.out.println(instruct.instructCheck(messageData));
    }

    @Test
    void getList() throws Exception {
        MessageData messageData = new MessageData();
        messageData.setQqID(123456789L);
        InstructHandle instruct = new InstructHandle();
        messageData.setMessage("123");
        System.out.println(instruct.instructCheck(messageData));
    }


}
