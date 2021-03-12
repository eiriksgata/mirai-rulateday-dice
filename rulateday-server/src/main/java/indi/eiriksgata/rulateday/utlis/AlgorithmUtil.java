package indi.eiriksgata.rulateday.utlis;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.utlis
 * date: 2021/3/11
 **/
public class AlgorithmUtil {
    public int[] bubbleSort(int[] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; i < data.length; i++) {
                if (data[i] > data[j]) {
                    int temp = data[j];
                    data[i] = data[j];
                    data[j] = temp;
                }
            }
        }
        return data;
    }
}
