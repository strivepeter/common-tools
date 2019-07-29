package com.peter.common.random;/**
 * Created by DELL on 2019/6/18.
 */

import java.util.Random;

/**
 * @ClassName RandomUtils
 * @Description 随机获取数字
 * @Author peter
 * @Date 2019/6/18 10:58
 * @Version 1.0
 */
public class RandomUtils {

    /**
     * 获取随机数
     *
     * @param number 要获取的位数
     * @return 返回Integer的值
     */
    public static Integer getRandomNumber(Integer number) {
        //定义变长字符串
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        //随机生成数字，并添加到字符串
        for (int i = 0; i < number; i++) {
            str.append(random.nextInt(10));
        }
        //将字符串转换为数字并输出
        int num = Integer.parseInt(str.toString());
        return num;
    }
}
