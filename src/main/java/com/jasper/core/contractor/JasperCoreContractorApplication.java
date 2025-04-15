package com.jasper.core.contractor;

import com.jasper.core.contractor.utils.ApplicationContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
public class JasperCoreContractorApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(JasperCoreContractorApplication.class, args);
        ApplicationContextUtils.set(context);

    }

}
