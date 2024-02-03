import com.github.eiriksgata.rulateday.dto.DiceMessageDTO;
import com.github.eiriksgata.trpg.dice.callback.SanCheckCallback;
import com.github.eiriksgata.trpg.dice.exception.DiceInstructException;
import com.github.eiriksgata.trpg.dice.message.handle.InstructHandle;
import com.github.eiriksgata.trpg.dice.operation.impl.RollRoleImpl;
import com.github.eiriksgata.trpg.dice.operation.impl.SanCheckImpl;

import com.github.eiriksgata.rulateday.instruction.RollController;
import com.github.eiriksgata.rulateday.init.LoadDatabaseFile;
import com.github.eiriksgata.rulateday.utlis.WeightRandom;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class DiceTest {


    @Test
    void test1() {
        LoadDatabaseFile.init();

    }


    @Test
    void stringFormatTest() {
        String result = MessageFormat.format("a:{0},b:{1}", 1, 2);
        System.out.println(result);
    }

    @Test
    void stringArrTest() {
        String[] test = {"123", "345"};
        System.out.println(test[1]);
    }


    @Test
    void instructTest() {
        DiceMessageDTO messageData = new DiceMessageDTO();
        messageData.setId(123456789L);
        messageData.setBody(".rlo201 80");
        String result = RollController.LCDSV1Check2(messageData);
        System.out.println(result);
    }


    @Test
    void rollTest() throws Exception {
        LoadDatabaseFile.initConfigCustomTextTool();
        LoadDatabaseFile.createAndLoadConfigFile();

        DiceMessageDTO messageData = new DiceMessageDTO();
        messageData.setId(123456789L);
        messageData.setBody("ra");

        InstructHandle instruct = new InstructHandle();
        try {
            System.out.println(instruct.instructCheck(messageData));
        } catch (DiceInstructException e) {
            System.out.println(e.getErrMsg());
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException ex) {
            System.out.println("call function exception");
            ex.printStackTrace();
        }
    }


    @Test
    public void setDiceFace() {
        DiceMessageDTO messageData = new DiceMessageDTO();
        messageData.setId(123456789L);
        InstructHandle instruct = new InstructHandle();
        messageData.setBody(".ww10a8+3");
        try {
            System.out.println(instruct.instructCheck(messageData));
        } catch (DiceInstructException e) {
            System.out.println(e.getErrMsg());
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException ex) {
            System.out.println("call function exception");
            ex.printStackTrace();
        }
    }


    @Test
    void callbackTest() {

        for (int i = 0; i < 1000; i++) {
            System.out.println(new SanCheckImpl().sanCheck("1d5/1d100+5d4", "san30", new SanCheckCallback() {
                @Override
                public void getResultData(String attribute, int random, int sanValue, String calculationProcess, int surplus) {

                }
            }));
        }
    }

    @Test
    void rb() throws Exception {
        DiceMessageDTO messageData = new DiceMessageDTO();
        messageData.setId(123456789L);
        InstructHandle instruct = new InstructHandle();
        messageData.setBody(".rp3 san 30");
        System.out.println(instruct.instructCheck(messageData));

    }

    @Test
    void d5r() throws Exception {
        DiceMessageDTO messageData = new DiceMessageDTO();
        messageData.setId(123456789L);
        InstructHandle instruct = new InstructHandle();
        messageData.setBody(".dr牧师:神圣领域");
        System.out.println(instruct.instructCheck(messageData));
    }


    @Test
    void rd() throws Exception {
        DiceMessageDTO messageData = new DiceMessageDTO();
        messageData.setId(123456789L);
        InstructHandle instruct = new InstructHandle();
        messageData.setBody(".raSan");
        System.out.println(instruct.instructCheck(messageData));
    }

    @Test
    void fileRead() {
        String imagesUrl = ResourceBundle.getBundle("resources").getString("resources.mm.images.url");
        String localPath = ResourceBundle.getBundle("resources").getString("resources.mm.images.path");
        System.out.println(imagesUrl);
        System.out.println(localPath);
    }

    @Test
    void weight() {

        WeightRandom.ItemWithWeight<String> item = new WeightRandom.ItemWithWeight<>("server1", 1.0);

    }


    @Test
    void rollRoleImplByDndRoleCreateTest() {
        System.out.println(new RollRoleImpl().createDnd5eRole());
    }


    @Test
    void getName() {
        String name = new File(ClassLoader.getSystemClassLoader().getResource("[mainclass]").getPath()).getAbsolutePath();
        System.out.println(name);
    }

}
