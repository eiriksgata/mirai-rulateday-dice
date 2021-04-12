import indi.eiriksgata.rulateday.mapper.NamesCorpusMapper;
import indi.eiriksgata.rulateday.service.impl.HumanNameServiceImpl;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Random;

/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date: 2021/4/7
 **/
public class NameTextToDb {

    @Test
    void run() throws IOException {
        File file = new File("D:\\workspace\\mirai-rulateday-dice\\LoadFileTest.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        int readLength;
        int countLength = 0;
        byte[] bufferContent = new byte[1048576];
        while (true) {
            readLength = fileInputStream.read(bufferContent, countLength, 1024);
            if (readLength == -1) {
                break;
            }
            countLength += readLength;
        }
        byte[] result = new byte[countLength];
        System.arraycopy(bufferContent, 0, result, 0, countLength);
        System.out.println(
                new String(result)
        );

    }


    @Test
    void testRandomNameService() {
        String result = new HumanNameServiceImpl().randomName(5);
        System.out.println(result);
    }

    @Test
    void createRandomName() {
        NamesCorpusMapper mapper = MyBatisUtil.getSqlSession().getMapper(NamesCorpusMapper.class);
        System.out.println(mapper.getCNAncientRandomName());
        System.out.println(mapper.getEnglishRandomName());
        System.out.println(mapper.getJapaneseRandomName());

    }

    @Test
    void testCnAncient() {
        //筛选10分3的名字数据
        File file = new File("D:\\workspace\\mirai-rulateday-dice\\Ancient_Names_Corpus.txt");
        importData(file, "cn_ancient");
    }

    @Test
    void importEnglish() {
        File file = new File("D:\\workspace\\mirai-rulateday-dice\\English_Cn_Name_Corpus.txt");
        importData(file, "english");
    }

    @Test
    void importJapanese() {
        File file = new File("D:\\workspace\\mirai-rulateday-dice\\Japanese_Names_Corpus.txt");
        importData(file, "japanese");
    }

    private void importData(File file, String type) {
        NamesCorpusMapper mapper = MyBatisUtil.getSqlSession().getMapper(NamesCorpusMapper.class);
        try {
            InputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            Random random = new Random();
            while (true) {
                try {
                    String lineText = bufferedReader.readLine();
                    if (lineText == null) {
                        break;
                    }
                    int randomValue = random.nextInt(10);
                    if (randomValue < 4) {
                        if (type.equals("cn_ancient")) {
                            mapper.insertCNAncient(lineText);
                        }
                        if (type.equals("english")) {
                            mapper.insertEnglishCN(lineText);
                        }
                    }
                    if (type.equals("japanese")) {
                        mapper.insertJapanese(lineText);
                    }
                } catch (IOException e) {
                    break;
                }
            }
            MyBatisUtil.getSqlSession().commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
