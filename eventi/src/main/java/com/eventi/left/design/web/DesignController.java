package com.eventi.left.design.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eventi.left.common.PagingVO;
import com.eventi.left.common.SessionUtil;
import com.eventi.left.contest.service.ContestService;
import com.eventi.left.contest.service.ContestVO;
import com.eventi.left.contest.service.WinnerVO;
import com.eventi.left.design.service.DesignService;
import com.eventi.left.design.service.DesignVO;
import com.eventi.left.files.service.FilesService;
import com.eventi.left.files.service.FilesVO;
import com.eventi.left.member.service.MemberVO;

@Controller
@RequestMapping("/design")
public class DesignController {
	@Autowired
	DesignService service;
	@Autowired
	ContestService cService;
	@Autowired
	FilesService fService;

	// 전체 디자인 리스트 조회
	@RequestMapping("/designList")
	public String bfList(Model model, DesignVO vo, PagingVO paging) {
		model.addAttribute("designList", service.designList(vo, paging));
		model.addAttribute("paging", paging);
		System.out.println(paging);
		return "content/design/designList";
	}

	// 회원의 디자인 응모하기 폼 이동.
	@GetMapping("/designInsertForm")
	public String designInsert(Model model, DesignVO vo) {

		model.addAttribute("contest", cService.getContest(vo.getcNo()));
		model.addAttribute("dNo", service.getSequence());
		return "content/contest/contestApplyForm";
	}

	// 디자인 응모 처리(마이페이지 응모리스트 페이지.)
	@PostMapping("/designInsert")
	public String designInsert(Model model, DesignVO vo, FilesVO filesVO, MultipartFile[] uploadFile) {

		// 로그인 회원정보
		MemberVO user = (MemberVO) SessionUtil.getSession().getAttribute("member");
		String sessionId = user.getUserId();
		vo.setUserId(sessionId);
		
		System.out.println(vo);

		// 등록처리후, 로그인유저의 디자인내역리스트 이동.
		service.insert(vo, filesVO, uploadFile);
		return "redirect:/design/designMypage";
	}

	// 디자인응모리스트(마이페이지)
	@GetMapping("/designMypage")
	public String designMypage(Model model, DesignVO vo, PagingVO paging) {

		// 로그인 회원정보
		MemberVO user = (MemberVO) SessionUtil.getSession().getAttribute("member");
		String sessionId = user.getUserId();
		vo.setUserId(sessionId);

		// 모델담기
		model.addAttribute("design", service.userDesignList(vo, paging));
		model.addAttribute("paging", paging);
		return "content/myPage/myDesignList";
	}

	// 디자인 수정처리X

	// 디자인 삭제처리(링크처리는 get/ deleteMappging form)
	@PostMapping("/delete")
	@ResponseBody
	public int designDelete(DesignVO vo) {

		return service.delete(vo);
	}

	// 1.디자이너 : 응모관리 -> 디자인조회
	@GetMapping("/select")
	public String userDesignSelect(Model model, String dgnNo) {

		// 디자인 1건조회
		DesignVO design = service.getDesign(dgnNo);
		List<FilesVO> files = new ArrayList<>();

		// 1건에 대한 파일리스트 받고 디자인vo 세팅 후 모델담기.
		files = fService.fileList(design.getDgnNo());
		design.setFiles(files);
		model.addAttribute("design", design);

		return "content/myPage/design";
	}

	// 1.공모전작성자 : 공모전지원자조회 -> 디자인조회 
	@PostMapping("/ajaxSelect")
	@ResponseBody
	public DesignVO designSelect(String dgnNo) {
		// 디자인 1건조회
		DesignVO design = service.getDesign(dgnNo);
		List<FilesVO> files = new ArrayList<>();

		// 1건에 대한 파일리스트 받고
		files = fService.fileList(design.getDgnNo());
		design.setFiles(files);

		return design;
	}
	
	// 임시저장된 게시글 전체들고오기
	@PostMapping("/dSave")
	@ResponseBody
	public List<DesignVO> dSave(DesignVO vo) {
		return service.dSave(vo);
	}

	// 임시저장된 게시글 상세들고오기
	@PostMapping("/saveSelect")
	@ResponseBody
	public List<DesignVO> saveGetDesign(DesignVO vo) {
		return service.saveGetDesign(vo);
	}
	
	// 임시저장 불러오기 수정처리
	@PostMapping("/saveUpdate")
	public String saveUpdateContest(DesignVO vo, FilesVO filesVO, MultipartFile[] uploadFile) {
		service.saveUpdateDesign(vo, filesVO,uploadFile);
		return "redirect:/design/designMypage";
	}


}
