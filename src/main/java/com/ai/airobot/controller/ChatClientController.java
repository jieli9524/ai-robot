package com.ai.airobot.controller;


import com.ai.airobot.model.AiResponse;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
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
    private ChatClient chatClient;

    /**
     * 普通对话
     * @param message
     * @return
     */
//    @GetMapping("/generate")
    @GetMapping(value = "/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AiResponse> generate(@RequestParam(value = "message", defaultValue = "你是谁？") String message,
                                     @RequestParam(value = "chatId") String chatId) {

        return chatClient.prompt()
                .user(message)
                //为当前聊天请求绑定一个会话 ID
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content()
                .map(content -> AiResponse.builder().v(content).build());

                    // 构建提示词
            //    Prompt prompt = new Prompt(new UserMessage(message));
            //
            //    // 流式输出
        //        return chatModel.stream(prompt)
        //            .mapNotNull(chatResponse -> {
        //        Generation generation = chatResponse.getResult();
        //        String text = generation.getOutput().getText();
        //        return AIResponse.builder().v(text).build();
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
