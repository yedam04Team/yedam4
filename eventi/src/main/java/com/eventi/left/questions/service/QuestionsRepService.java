package com.eventi.left.questions.service;

import java.util.List;

import com.eventi.left.contest.service.WinnerVO;

public interface QuestionsRepService {
	
	//문의답변
	public List<QuestionsRepVO> questionsRepList(QuestionsRepVO QuestionsRepVO); //전체조회 
	public QuestionsRepVO getQuestionsRep(QuestionsRepVO QuestionsRepVO); //1건조회 


}