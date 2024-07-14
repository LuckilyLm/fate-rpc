package org.fate.examplespringbootprovider;

import org.fate.faterpc.springboot.starter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Fate
 * @Date: 2024/7/13 18:13
 **/

@SpringBootApplication
@EnableRpc
public class ExampleSpringbootProviderApplication
{
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootProviderApplication.class, args);
    }
}
