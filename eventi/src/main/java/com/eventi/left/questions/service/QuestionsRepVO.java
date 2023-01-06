package com.eventi.left.questions.service;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
public class QuestionsRepVO { 
	String rNo;		//답변번호
	String qNo;		//문의번호
	String userId;	//작성자Id
	@DateTimeFormat(pattern = "yyyy-MM-dd") 
	Date writingDt;	//작성일자
	String rCntn;	//답변내용
	
}