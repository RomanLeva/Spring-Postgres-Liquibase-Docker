package group.demoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication //(exclude = {DataSourceAutoConfiguration.class })
@EnableRetry
@ComponentScan(basePackages =
        {"group.demoapp.repository", "group.demoapp.controller", "group.demoapp.service", "group.demoapp.aspect", "group.demoapp.config"})
public class DemoAppApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoAppApplication.class, args);
    }

}
