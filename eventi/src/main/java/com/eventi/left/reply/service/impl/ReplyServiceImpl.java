package com.eventi.left.reply.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventi.left.reply.mapper.ReplyMapper;
import com.eventi.left.reply.service.ReplyService;
import com.eventi.left.reply.service.ReplyVO;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired ReplyMapper mapper;
		
	@Override
	public List<ReplyVO> replyList(ReplyVO ReplyVO) {
		return mapper.replyList(ReplyVO);
	}

	@Override
	public ReplyVO getReply(ReplyVO ReplyVO) {
		return mapper.getReply(ReplyVO);
	}
	
	//댓글 삭제
	@Override
	public int replyDelete(ReplyVO ReplyVO) {
		// TODO Auto-generated method stub
		return mapper.replyDelete(ReplyVO);
	}

}
