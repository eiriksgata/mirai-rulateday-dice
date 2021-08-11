package indi.eiriksgata.rulateday.utlis;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.trpgservice.utils
 * @date:2020/5/6
 **/
public class ExceptionUtils {

    public static String getExceptionAllInfo(Exception ex) {
        ByteArrayOutputStream out = null;
        PrintStream pout = null;
        String ret = "";
        try {
            out = new ByteArrayOutputStream();
            pout = new PrintStream(out);
            ex.printStackTrace(pout);
            ret = out.toString();
            out.close();
        } catch (Exception e) {
            return ex.getMessage();
        } finally {
            if (pout != null) {
                pout.close();
            }
        }
        return ret;
    }

}
