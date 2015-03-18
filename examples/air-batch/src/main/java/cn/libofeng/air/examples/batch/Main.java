package cn.libofeng.air.examples.batch;

import cn.libofeng.air.examples.batch.config.BatchConfig;
import org.springframework.boot.SpringApplication;

public class Main {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(BatchConfig.class, args)));
    }
}