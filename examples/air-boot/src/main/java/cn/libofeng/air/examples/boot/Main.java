package cn.libofeng.air.examples.boot;

import cn.libofeng.air.examples.boot.web.controller.SampleController;
import org.springframework.boot.SpringApplication;

public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}
