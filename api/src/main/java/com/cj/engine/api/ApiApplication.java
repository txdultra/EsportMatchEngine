package com.cj.engine.api;

import com.cj.engine.EngineApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication(scanBasePackages = "com.cj")
public class ApiApplication {

    public static void main(String[] args) {
        //SpringApplication.run(ApiApplication.class, args);
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.CONSOLE)
                .sources(EngineApplication.class, ApiApplication.class)
                .run(args);
    }
}
