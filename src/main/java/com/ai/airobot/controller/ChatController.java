package com.ai.airobot.controller;

import com.ai.airobot.aspect.ApiOperationLog;
import com.ai.airobot.service.ChatService;
import com.ai.airobot.utils.Response;
import com.ai.airobot.vo.NewChatReqVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Resource
    private ChatService chatService;

    @PostMapping("/new")
    @ApiOperationLog(description = "新建对话")
    public Response<?> newChat(@RequestBody @Valid NewChatReqVO newChatReqVO) {
        return chatService.newChat(newChatReqVO);
    }

}