package com.ai.airobot.controller;


import com.ai.airobot.model.AiResponse;
import com.ai.airobot.tools.DateTimeTools;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/v2/ai")
public class ChatClientController {


    @Resource
    private ZhiPuAiChatModel chatModel;
    @Resource
    private ChatClient chatClient;

    @Value("classpath:/prompts/code-assistant.st")
    private org.springframework.core.io.Resource templateResource;

    /**
     * 普通对话
     * @param message
     * @return
     */

    @GetMapping(value = "/generateModel", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AiResponse> generateStream(@RequestParam(value = "message", defaultValue = "你是谁？") String message,
                                           @RequestParam(value = "lang") String lang) {
        // 构建提示词
        PromptTemplate promptTemplate = new PromptTemplate(templateResource);

        // 填充提示词占位符，转换为 Prompt 提示词对象
        Prompt prompt = promptTemplate.create(Map.of("description", message, "lang", lang));


        // 流式输出
        return chatModel.stream(prompt)
                .filter(chatResponse -> chatResponse.getResult() != null)
                .mapNotNull(chatResponse -> {
                    Generation generation = chatResponse.getResult();
                    String text = generation.getOutput().getText();
                    if (text != null && !text.isEmpty()) {
                        return AiResponse.builder().v(text).build();
                    }
                    return null;
                })
                .filter(aiResponse -> aiResponse != null && aiResponse.getV() != null && !aiResponse.getV().isEmpty());



//        return chatModel.stream(prompt)
//                .mapNotNull(chatResponse -> {
//                    Generation generation = chatResponse.getResult();
//                    String text = generation.getOutput().getText();
//                    return AiResponse.builder().v(text).build();
//                });

    }




    @GetMapping(value = "/generateClient", produces = "text/html;charset=utf-8")
    public Flux<String> generateCall(@RequestParam(value = "message", defaultValue = "你是谁？") String message,
                                     @RequestParam(value = "chatId") String chatId) {
        // 一次性返回结果
        return  chatClient.prompt()
                .tools(new DateTimeTools()) // Function Call
//                .system("请你扮演一名犬小哈 Java 项目实战专栏的客服人员")
                .user(message) // 提示词
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();

    }


}
