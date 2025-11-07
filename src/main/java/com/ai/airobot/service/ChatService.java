package com.ai.airobot.service;

import com.ai.airobot.utils.Response;
import com.ai.airobot.vo.NewChatReqVO;
import com.ai.airobot.vo.NewChatRspVO;

public interface ChatService {
    /**
     * 新建对话
     * @param newChatReqVO
     * @return
     */
    Response<NewChatRspVO> newChat(NewChatReqVO newChatReqVO);
}
