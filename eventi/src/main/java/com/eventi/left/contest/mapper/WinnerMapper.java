package com.eventi.left.contest.mapper;

import java.util.List;

import com.eventi.left.contest.service.WinnerVO;

public interface WinnerMapper {
	
	//상금 및 우승자 정보
		public List<WinnerVO> winnerList(WinnerVO WinnerVO); //공모전 전체조회 
		public WinnerVO getWinner(WinnerVO WinnerVO); //1건조회 
		public int insertWinner(WinnerVO WinnerVO); //추가
		public int updateWinner(WinnerVO WinnerVO); //수정
		public int deleteWinner(String wNo); //삭제

}