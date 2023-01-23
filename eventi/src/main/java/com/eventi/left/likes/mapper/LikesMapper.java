package com.eventi.left.likes.mapper;

import java.util.List;

import com.eventi.left.common.PagingVO;
import com.eventi.left.likes.service.LikesVO;

public interface LikesMapper {

	// 좋아요
	public List<LikesVO> userlikeList(LikesVO LikesVO); // 전체조회,세션아이디 조회.

	public int userlikecheck(String targetNo,String category, String userId); // 세션아이디 좋아요확인처리
	
	public int setTotalRecord(String category, String userId); // 세션아이디 좋아요리스트 개수(페이징)
	
	public List<LikesVO> likeList(LikesVO LikesVO); // 전체조회

	public LikesVO getLike(LikesVO LikesVO); // 1건조회
	
	//좋아요 개수 조회
	public int countLike(LikesVO LikesVO);

	public int likeInsert(LikesVO LikesVO); // 좋아요 입력

	public int likeDelete(LikesVO LikesVO); // 좋아요 취소
}
