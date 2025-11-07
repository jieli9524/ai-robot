package com.ai.airobot.controller;

import com.ai.airobot.aspect.ApiOperationLog;
import com.ai.airobot.utils.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatControllertest {
    @PostMapping("/new")
    @ApiOperationLog(description = "新建对话")
    public Response<?> newChat() {
        // 模拟一个运行时错误（分母不能为 0）
        int i = 1 / 0;

        return Response.success();
    }
}
