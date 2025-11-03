package com.ai.airobot.config;


import com.ai.airobot.advisor.LoggerAdvisor;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Resource
    private ChatMemory chatMemory;
    /**
     * 初始化 ChatClient 客户端
     * @param chatModel
     * @return
     */
    @Bean
    public ChatClient chatClient(ZhiPuAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("请你扮演一名教java的老师")
                // 添加 Spring AI 内置的日志记录功能 SimpleLoggerAdvisor
                // 添加自定义的日志打印 LoggerAdvisor
                .defaultAdvisors(new SimpleLoggerAdvisor(),
//                        new LoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
