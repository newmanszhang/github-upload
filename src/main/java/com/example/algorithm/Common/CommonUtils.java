package com.example.algorithm.Common;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CommonUtils {
    /**
     * @description: 生成不重复的随机数: [0, endNum)
     * @author : yex
     * @param endNum : 生成随机数的上限，但不包括上限
     * @param length : 需要生成随机数的个数
     * @return result : 生成的随机集合
     */
    public List<Integer> generateDifferentRandomNumbers(int endNum, int length) {
        if (endNum == 0 || length == 0 || length > endNum) {
            return null;
        }
        // 生成数组 numbers = [0,1,2,3 ... endNum - 1]
        List<Integer> result;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < endNum; i++) {
            numbers.add(i);
        }

        // 随机生成索引randIndex，并将该位置元素与selectedIndex位置元素进行交换
        Random random = new Random();
        int selectedIndex = endNum - 1;
        for (int i = 1; i < length; i++) {
            int randIndex = random.nextInt(endNum - i);
            Collections.swap(numbers, randIndex, selectedIndex);
            selectedIndex--;
        }

        // 截取numbers的后length位元素
        result = numbers.subList(selectedIndex, endNum);
        return result;
    }

    /**
     * @description 在一个排序好的数组中，查找第一个不小于value的元素下标
     * @author : yex
     * @param nums
     * @param value
     * @return 返回-1，说明不存在 即 value > v for v in nums
     */
    public int lowerBoundSearch(List<Double> nums, double value) {
        int pos = 0;
        int n = nums.size();
        int l = 0, r = n - 1;
        while (l <= r) {
            int mid = l + ((r - l) >> 1);
            if (nums.get(mid) < value) {
                l = mid + 1;
                pos = l;
            } else {
                r = mid - 1;
            }
        }
        if (pos >= n) pos = -1;
        return pos;
    }
    /**
     * @description  从已知列表中随机选取不同对象
     * @author : wzm
     * @param n 选取的个数
     * @param size 已知列表的个数
     * @return 选取下表
     */
    public static List<Integer> randomList(int n,int size) {
        Random rand = new Random();
        List<Integer> list = new ArrayList<>();
        int i = 1;
        while (i <= n) {
            int num = rand.nextInt(size) ;
            if (!list.contains(num)) {
                list.add(num);
                i++;
            }
        }
        return list;
    }
    //深拷贝代码
    public static <T> List<T> deepCopy(List<T> list) {
        //判断是否实现序列化
        if (list instanceof Serializable){
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            try {
                ObjectOutputStream out = new ObjectOutputStream(byteOut);
                out.writeObject(list);

                ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                ObjectInputStream inStream = new ObjectInputStream(byteIn);
                List<T> ts = (List<T>) inStream.readObject();
                return ts;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    //求方差
    public static double Variance(double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        return dVar/m;
    }

    //标准差
    public static double StandardDiviation(double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        //reture Math.sqrt(dVar/(m-1));
        return Math.sqrt(dVar/m);
    }
    public static double[] ListTodouble(List<Double> list){
        Double[] doubles=new Double[list.size()];
        list.toArray(doubles);
        if(doubles==null){
            return null;
        }
        double[] result=new double[doubles.length];
        for(int i=0;i<doubles.length;i++){
            result[i]=doubles[i].doubleValue();
        }
        return result;
    }
}
