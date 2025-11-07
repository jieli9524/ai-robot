package com.ai.airobot.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ai.airobot.domain.mapper")
public class MybatisPlusConfig {
}
