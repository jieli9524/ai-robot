package com.ai.airobot.controller;


import com.ai.airobot.model.AiResponse;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v2/ai")
public class ChatClientController {
    @Resource
    private ZhiPuAiChatModel chatModel;
    @Resource
    private ChatClient chatClient;

    /**
     * 普通对话
     * @param message
     * @return
     */

    @GetMapping(value = "/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AiResponse> generateStream(@RequestParam(value = "message", defaultValue = "你是谁？") String message) {
        // 构建提示词
        Prompt prompt = new Prompt(new UserMessage(message));

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



    @GetMapping("/generate")
    public String generateCall(@RequestParam(value = "message", defaultValue = "你是谁？") String message){
        // 一次性返回结果
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }


}
