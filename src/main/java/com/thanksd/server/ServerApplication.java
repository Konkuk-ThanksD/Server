package com.thanksd.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = {
		io.awspring.cloud.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
		io.awspring.cloud.autoconfigure.context.ContextStackAutoConfiguration.class,
		io.awspring.cloud.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
})
public class ServerApplication {

    public static final String APPLICATION_LOCATIONS = "spring.config.additional-location="
            + "classpath:application.yml,"
            + "classpath:application-s3.yml";

    public static void main(String[] args) {
		new SpringApplicationBuilder(ServerApplication.class)
				.properties(APPLICATION_LOCATIONS)
				.run(args);
    }

}
/*

 */