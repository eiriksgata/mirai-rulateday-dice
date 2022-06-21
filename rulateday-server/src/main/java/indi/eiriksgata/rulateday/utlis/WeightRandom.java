package indi.eiriksgata.rulateday.utlis;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class WeightRandom<T> {

    private final List<T> items = new ArrayList<>();
    private double[] weights;

    public WeightRandom(List<ItemWithWeight<T>> itemsWithWeight) {
        this.calWeights(itemsWithWeight);
    }

    /**
     * 计算权重，初始化或者重新定义权重时使用
     */
    public void calWeights(List<ItemWithWeight<T>> itemsWithWeight) {
        items.clear();

        // 计算权重总和
        double originWeightSum = 0;
        for (ItemWithWeight<T> itemWithWeight : itemsWithWeight) {
            double weight = itemWithWeight.getWeight();
            if (weight <= 0) {
                continue;
            }

            items.add(itemWithWeight.getItem());
            if (Double.isInfinite(weight)) {
                weight = 10000.0D;
            }
            if (Double.isNaN(weight)) {
                weight = 1.0D;
            }
            originWeightSum += weight;
        }

        // 计算每个item的实际权重比例
        double[] actualWeightRatios = new double[items.size()];
        int index = 0;
        for (ItemWithWeight<T> itemWithWeight : itemsWithWeight) {
            double weight = itemWithWeight.getWeight();
            if (weight <= 0) {
                continue;
            }
            actualWeightRatios[index++] = weight / originWeightSum;
        }

        // 计算每个item的权重范围
        // 权重范围起始位置
        weights = new double[items.size()];
        double weightRangeStartPos = 0;
        for (int i = 0; i < index; i++) {
            weights[i] = weightRangeStartPos + actualWeightRatios[i];
            weightRangeStartPos += actualWeightRatios[i];
        }
    }

    /**
     * 基于权重随机算法选择
     */
    public T choose() {
        double random = ThreadLocalRandom.current().nextDouble();
        int index = Arrays.binarySearch(weights, random);
        if (index < 0) {
            index = -index - 1;
        } else {
            return items.get(index);
        }

        if (index < weights.length && random < weights[index]) {
            return items.get(index);
        }

        // 通常不会走到这里，为了保证能得到正确的返回，这里随便返回一个
        return items.get(0);
    }

    public static class ItemWithWeight<T> {
        T item;
        double weight;

        public ItemWithWeight() {
        }

        public ItemWithWeight(T item, double weight) {
            this.item = item;
            this.weight = weight;
        }

        public T getItem() {
            return item;
        }

        public void setItem(T item) {
            this.item = item;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        // for test
        int sampleCount = 1_000_000;

        ItemWithWeight<String> server1 = new ItemWithWeight<>("server1", 1.0);
        ItemWithWeight<String> server2 = new ItemWithWeight<>("server2", 3.0);
        ItemWithWeight<String> server3 = new ItemWithWeight<>("server3", 2.0);

        WeightRandom<String> weightRandom = new WeightRandom<>(Arrays.asList(server1, server2, server3));

        // 统计 (这里用 AtomicInteger 仅仅是因为写起来比较方便，这是一个单线程测试)
        Map<String, AtomicInteger> statistics = new HashMap<>();

        for (int i = 0; i < sampleCount; i++) {
            statistics
                    .computeIfAbsent(weightRandom.choose(), (k) -> new AtomicInteger())
                    .incrementAndGet();
        }

        statistics.forEach((k, v) -> {
            double hit = (double) v.get() / sampleCount;
            System.out.println(k + ", hit:" + hit);
        });

        System.out.println(weightRandom.choose());
    }
}