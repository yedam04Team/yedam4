package com.eventi.left.resume.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventi.left.common.SessionUtil;
import com.eventi.left.files.service.FilesService;
import com.eventi.left.files.service.FilesVO;
import com.eventi.left.job.service.JobBoardVO;
import com.eventi.left.job.service.JobService;
import com.eventi.left.member.service.MemberVO;
import com.eventi.left.resume.service.ResumeBoardVO;
import com.eventi.left.resume.service.ResumeService;

@Controller
public class ResumeController {

	@Autowired 
	ResumeService resumeService;
	
	@Autowired 
	JobService jobService;
	//파일 service
	@Autowired FilesService filesService;
	
	//전체조회(메인구직게시판-회원ID가 작성한 글에 신청한 구직자들 전체조회)
	@RequestMapping(value = "/resumeList", method=RequestMethod.GET) 
	public String resumeList(Model model, ResumeBoardVO resumeBoardVO) {
		MemberVO user = (MemberVO) SessionUtil.getSession().getAttribute("member");
		resumeBoardVO.setSeekerId(user.getUserId());
		model.addAttribute("resumeList", resumeService.getResumeList(resumeBoardVO));
		return "content/resume/resumeList";
	}
	
	
	//전체조회(구인게시글상세에서조회-같은구직게시글(하나의 글에 대한 구직자들 전체조회))
	@RequestMapping(value = "/resumeJob", method=RequestMethod.GET) 
	public String resumeListOne(Model model, ResumeBoardVO resumeBoardVO) {
		model.addAttribute("resumeJob", resumeService.getResumeJob(resumeBoardVO));
		return "content/resume/resumeJob";
	}
	
	//구직자 상세조회
	@RequestMapping(value = "/resumeDetail", method=RequestMethod.GET) //단건조회
	public String resumeDetail(Model model, ResumeBoardVO resumeBoardVO) {
		model.addAttribute("detail", resumeService.getResumeDetail(resumeBoardVO));
		return "content/resume/resumeDetail";
	}
	
	//구직신청폼 이동
		@RequestMapping(value = "/ApplyForm", method=RequestMethod.GET) 
		public String getJob(Model model, MemberVO memberVO, JobBoardVO jobBoardVO) {
			model.addAttribute("apply", resumeService.getApplyForm(memberVO)); //값이 저장됨
			model.addAttribute("jobNo", jobBoardVO.getJobNo());
			return "content/resume/ApplyForm"; //content -html링크
	}
		
	//구직신청
	@PostMapping("/ApplyJob")
	//@ResponseBody //ajax응답
	public String applyJob(ResumeBoardVO resumeBoardVO, FilesVO filesVO, MultipartFile[] uploadFile, RedirectAttributes rttr) {
		resumeService.ApplyInsert(resumeBoardVO, filesVO, uploadFile);
		
		rttr.addFlashAttribute("result", "신청완료!"); //모달창, flash이므로 param.result에서 param빼야됨,,
		return "redirect:/jobDetail?jobNo=" + resumeBoardVO.getJobNo(); //redirect -mapping 링크, param임..
		}
	
	//채용하기 
	@PostMapping("/hireUpdate")
	@ResponseBody //ajax방식
	public int hireUpdate(ResumeBoardVO resumeBoardVO) {
		return resumeService.hireUpdate(resumeBoardVO);
			}
	
	//부분채용하기 
	@PostMapping("/hireUpdates")
	@ResponseBody //ajax방식
	public int hireUpdate(@RequestBody List<ResumeBoardVO> resumeList) {
		return resumeService.hireUpdates(resumeList);
	}
	
	//채용취소하기 
	@PostMapping("/unHireUpdate")
	@ResponseBody //ajax방식
	public int unHireUpdate(ResumeBoardVO resumeBoardVO) {
		return resumeService.unHireUpdate(resumeBoardVO);
	}
	
	//부분채용 취소하기 
	@PostMapping("/unHireUpdates")
	@ResponseBody //ajax방식
	public int unhireUpdate(@RequestBody List<ResumeBoardVO> resumeCancel) {
		return resumeService.unHireUpdates(resumeCancel);
	}
}
