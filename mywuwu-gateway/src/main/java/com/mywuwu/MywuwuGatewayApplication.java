package com.mywuwu;

import com.mywuwu.common.ds.DynamicDataSourceRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import tk.mybatis.spring.annotation.MapperScan;

@Import(DynamicDataSourceRegister.class)
@MapperScan(basePackages = "com.mywuwu.mapper")
@SpringBootApplication
public class MywuwuGatewayApplication {
    private static final Logger logger = LoggerFactory.getLogger(MywuwuGatewayApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(MywuwuGatewayApplication.class, args);
        logger.info(" Start APIGatewayApplication Done");
    }

}

