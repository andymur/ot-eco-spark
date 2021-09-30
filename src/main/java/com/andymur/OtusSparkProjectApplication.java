package com.andymur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class OtusSparkProjectApplication implements WebMvcConfigurer
{

    public static void main(String[] args)
    {
        try {
            SpringApplication.run(OtusSparkProjectApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        //registry.addInterceptor(new LoggingInterceptor()).addPathPatterns("/**");
    }

}
