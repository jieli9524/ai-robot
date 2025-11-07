package com.ai.airobot.service.impl;

import com.ai.airobot.domain.dos.ChatDO;
import com.ai.airobot.domain.mapper.ChatMapper;
import com.ai.airobot.service.ChatService;
import com.ai.airobot.utils.Response;
import com.ai.airobot.utils.StringUtil;
import com.ai.airobot.vo.NewChatReqVO;
import com.ai.airobot.vo.NewChatRspVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@Slf4j
public class ChatServiceImpl implements ChatService {
    @Resource
    private ChatMapper chatMapper;

    /**
     * 新建对话
     *
     * @param newChatReqVO
     * @return
     */
    @Override
    public Response<NewChatRspVO> newChat(NewChatReqVO newChatReqVO) {

        // 用户发送的消息
        String message = newChatReqVO.getMessage();

        // 生成对话 UUID
        String uuid = UUID.randomUUID().toString();
        // 截取用户发送的消息，作为对话摘要
        String summary = StringUtil.truncate(message, 20);

        // 存储对话记录到数据库中
        chatMapper.insert(ChatDO.builder()
                .summary(summary)
                .uuid(uuid)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build());

        // 将摘要、UUID 返回给前端
        return Response.success(NewChatRspVO.builder()
                .uuid(uuid)
                .summary(summary)
                .build());
    }

}
