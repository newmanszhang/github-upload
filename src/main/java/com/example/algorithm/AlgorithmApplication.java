package com.example.algorithm;

import com.example.algorithm.someone.GA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AlgorithmApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(AlgorithmApplication.class, args);
//    }


    public static void main(String[] args) {
        GA ga=new GA();
        ga.GA();
    }


}
