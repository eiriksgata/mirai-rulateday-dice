package indi.eiriksgata.rulateday.utlis;

public class HexConvertUtil {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * 十六进制字符串转字节数组
     *
     * @param hexString 如：FE00120F0E
     * @return
     */
    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        if (length % 2 == 1) {
            hexString = "0" + hexString;
            length++;
        }
        byte[] buffer = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            buffer[i / 2] = (byte) ((toByte(hexString.charAt(i)) << 4) | toByte(hexString.charAt(i + 1)));
        }
        return buffer;
    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    private static int toByte(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

}
