package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

@SpringBootApplication
public class DemoApplication {
    private static final String SENTENCE_TERMINATORS = "。！？!?.,;:，；：\\\n";

    public static void main(String[] args) {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomNumber = random.nextInt(10000000); // 生成 0 到 9999 的随机数

        String timestampString = String.valueOf(timestamp);
        String randomNumberString = String.format("%07d", randomNumber); // 格式化随机数为 4 位
        System.out.println(timestampString + randomNumberString);
        System.out.println(timestampString.length());
        System.out.println(randomNumberString.length());
        System.out.println((timestampString + randomNumberString).length());
        SpringApplication.run(DemoApplication.class, args);

        String inputString = "可以是蔬菜汤或烤鱼\n记得多喝水";
        String s = inputString.replaceAll("\"^[。！？!?.,;:，；：\\n]+|[。！？!?.,;:，；：\\n]+$\"", " ");
        System.out.println(s);
        System.out.println("end");
    }

}
