package com.eventi.left.promotion.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eventi.left.promotion.mapper.PromotionBoardMapper;
import com.eventi.left.promotion.service.PromotionService;
import com.eventi.left.promotion.service.PromotionVO;
import com.eventi.left.reply.service.ReplyVO;

@Service
public class PromotionServiceImpl implements PromotionService{
	
	@Autowired 
	PromotionBoardMapper proMapper;

	//홍보게시글 전체조회
	@Override
	public List<PromotionVO> proList(PromotionVO promotionVO) {
		// TODO Auto-generated method stub
		return proMapper.proList(promotionVO);
	}
	
	//게시글 상세조회
	@Override
	public PromotionVO proDetail(PromotionVO promotionVO) {
		proMapper.seeUp(promotionVO);
		return proMapper.proDetail(promotionVO);
	}	

	//게시글 등록
	@Override
	public int proInsert(PromotionVO promotionVO, MultipartFile uploadFile) {
		
		//사진등록
		String realFolder = "/files/pro";
		File dir = new File(realFolder);
		if(!dir.isDirectory()) {
			dir.mkdirs();
		}
				
		//파일 이름 저장
		String img = uploadFile.getOriginalFilename();
				
		//VO에 IMG 부분에 파일 이름 저장
		promotionVO.setImg(img);
				
		return proMapper.proInsert(promotionVO);
	}

	//게시글 수정
	@Override
	public int proUpdate(PromotionVO promotionVO) {
		// TODO Auto-generated method stub
		return proMapper.proUpdate(promotionVO);
	}
	
	//게시글 삭제
	@Override
	public int proDelete(PromotionVO promotionVO) {
		// TODO Auto-generated method stub
		return proMapper.proDelete(promotionVO);
	}
	
	//댓글 조회
	@Override
	public List<ReplyVO> proReply(ReplyVO replyVO) {
		return proMapper.proReply(replyVO);
	}

}
