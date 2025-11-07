package com.ai.airobot.controller;

import com.ai.airobot.aspect.ApiOperationLog;
import com.ai.airobot.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatControllertest {

    @PostMapping("/new")
    @ApiOperationLog(description = "新建对话")
    public Response<?> newChat() {
        log.trace("这是一个 TRACE 级别日志");
        log.debug("这是一个 DEBUG 级别日志");
        log.info("这是一个 INFO 级别日志");
        log.warn("这是一个 WARN 级别日志");
        log.error("这是一个 ERROR 级别日志");

        // 模拟一个运行时错误（分母不能为 0）
        int i = 1 / 0;

        return Response.success();
    }
}
