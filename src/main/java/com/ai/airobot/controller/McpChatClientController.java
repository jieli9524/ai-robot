package com.ai.airobot.controller;

import com.ai.airobot.model.AiResponse;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.Generation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/mcp/ai")
public class McpChatClientController {

    @Resource
    private ChatClient chatClient;

    /**
     * 流式对话
     * @param message
     * @return
     */
    @GetMapping(value = "/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AiResponse> generateStream(@RequestParam(value = "message") String message,
                                           @RequestParam(value = "chatId") String chatId) {

        // 流式输出
        return chatClient.prompt()
                .user(message) // 提示词
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .chatResponse()
                .filter(chatResponse -> chatResponse.getResult() != null)
                .mapNotNull(chatResponse -> {
                    Generation generation = chatResponse.getResult();
                    String text = generation.getOutput().getText();
                    if (text != null && !text.isEmpty()) {
                        return AiResponse.builder().v(text).build();
                    }
                    return null;
                })
                .filter(aiResponse -> aiResponse != null
                        && aiResponse.getV() != null && !aiResponse.getV().isEmpty());





    }

}
