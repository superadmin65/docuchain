package com.ipfs.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@EnableAutoConfiguration 
@Configuration
@PropertySource({"classpath:application.properties","classpath:message.properties"})
public class App extends SpringBootServletInitializer{

    public static void main( String[] args )
    {
    	 SpringApplication.run(App.class, args);
    }
}
 