package com.eventi.left.contest.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eventi.left.bboard.service.BboardVO;
import com.eventi.left.common.PagingVO;
import com.eventi.left.common.SessionUtil;
import com.eventi.left.common.service.CodeService;
import com.eventi.left.contest.service.ContestService;
import com.eventi.left.contest.service.ContestVO;
import com.eventi.left.contest.service.WinnerService;
import com.eventi.left.contest.service.WinnerVO;
import com.eventi.left.design.service.DesignService;
import com.eventi.left.design.service.DesignVO;
import com.eventi.left.files.service.FilesService;
import com.eventi.left.likes.service.LikesService;
import com.eventi.left.likes.service.LikesVO;
import com.eventi.left.member.service.MemberVO;

import groovy.util.logging.Log4j;

@Controller
@RequestMapping("/contest")
@Log4j
public class ContestController {

	@Autowired
	ContestService service;
	@Autowired
	WinnerService wService;
	@Autowired
	FilesService fService;
	@Autowired
	DesignService dService;
	@Autowired
	CodeService codeService;
	@Autowired
	LikesService likeService;

	// 공모전 전체리스트(첫페이지)
	@GetMapping("/List")
	public String contestList(Model model, ContestVO vo, PagingVO paging) {
		model.addAttribute("contestList", service.contestList(vo, paging));
		return "content/contest/contestList";
	}

	// 공모전 전체리스트(ajax)
	@PostMapping("/List")
	@ResponseBody
	public Map<String, Object> changeList(ContestVO vo, PagingVO paging) {

		// 리턴할 최종Map(contest,paging VO)
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("contest", service.contestList(vo, paging));
		result.put("paging", paging);
		return result;
	}

	// 1.공모전 상세리스트(get)
	@GetMapping("/select")
	public String contestSelect(Model model, ContestVO cVo, PagingVO paging) {

		// 로그인 회원정보
		MemberVO user = (MemberVO) SessionUtil.getSession().getAttribute("member");
		String sessionId = user.getUserId();

		// 객체생성 및 코드별 문자열 변환후 세팅
		ContestVO contest = service.getContest(cVo.getcNo());

		// 상세리스트 1건의 회원의 좋아요 체크 확인을 위한 정보세팅.
		LikesVO likeCheck = new LikesVO();
		likeCheck.setTargetNo(cVo.getcNo());
		likeCheck.setUserId(sessionId);

		// 모델 넘겨주기.
		model.addAttribute("likeCheck", likeService.getLike(likeCheck));
		model.addAttribute("fileList", fService.fileList(cVo.getcNo()));
		model.addAttribute("winnerList", wService.winnerList(cVo.getcNo()));
		model.addAttribute("contest", contest); // 모델 넘겨주기.

		return "content/contest/contest";
	}

	// 2.공모전 상세리스트(요청사항ajax)
	@PostMapping("/cntnSelect")
	@ResponseBody
	public ContestVO contestSelect(ContestVO vo) {
		ContestVO contest = service.getContest(vo.getcNo());
		return contest;// 리스트 1건 반환.
	}

	// 등록화면이동
	@GetMapping("/insert")
	public String contestInsert(Model model) {
		model.addAttribute("cNo", service.getSequence()); // 입력할 공모전번호
		return "content/contest/contestInsertForm";
	}

	// 등록처리
	@PostMapping("/insert")
	public String contestInsertForm(Model model, ContestVO vo, WinnerVO wvo, PagingVO paging) {

		service.insertContest(vo, wvo);

		// 임시저장일 경우 공모전 작성 리스트 이동
		if (vo.getSave().equals("Y")) {
			// 전체데이터 받기
			List<ContestVO> contests = service.myContestList(vo, paging);
			model.addAttribute("contestList", contests);// 모델에 담기.
			return "redirect:/contest/mySelect"; // 나의 공모전리스트.
		}
		// 등록인경우 결제 후 공모전상세페이지 이동.
		return "redirect:/contest/select?cNo=" + vo.getcNo();
	}

	// 수정화면이동
	@GetMapping("/update")
	public String contestupdate(Model model, ContestVO vo) {
		model.addAttribute("contest", service.getContest(vo.getcNo()));
		model.addAttribute("fileList", fService.fileList(vo.getcNo()));
		model.addAttribute("winnerList", wService.winnerList(vo.getcNo()));
		return "content/contest/contestUpdateForm";
	}

	// 수정처리
	@PostMapping("/update")
	public String contestupdateForm(ContestVO vo, MultipartFile[] uploadFile) {
		service.updateContest(vo, uploadFile);
		return "redirect:/contest/select?cNo=" + vo.getcNo();
	}

	// 마감연장 수정처리
	@PutMapping("/updateExtension")
	@ResponseBody
	public int contestExtension(@RequestBody ContestVO vo, MultipartFile[] uploadFile) {
		return service.updateContest(vo, uploadFile);
	}

	// 삭제처리(링크처리는 get/ deleteMappging form)
	@PostMapping("/delete")
	@ResponseBody
	public int contestDelete(ContestVO vo) {
		// 로그인 회원정보
		MemberVO user = (MemberVO) SessionUtil.getSession().getAttribute("member");
		String sessionId = user.getUserId();
		vo.setWriter(sessionId);

		return service.deleteContest(vo);
	}

	// 공모전 좋아요리스트 페이지이동
	@GetMapping("/like")
	public String likeList() { // 로그인 회원정보
		return "content/myPage/myContestLike";
	}

	// 로그인회원의 전체 좋아요리스트
	@PostMapping("/ajaxlike")
	@ResponseBody
	public Map<String, Object> userlikeList(LikesVO vo, PagingVO paging) {

		// 로그인 회원정보
		MemberVO user = (MemberVO) SessionUtil.getSession().getAttribute("member");
		Map<String, Object> result = new HashMap<String, Object>();

		if (user == null) {
			return result;
		}
		String sessionId = user.getUserId();
		vo.setUserId(sessionId);

		// 리턴할 최종Map(contest,paging VO)
		result.put("contest", likeService.userlikeList(vo, paging));
		result.put("paging", paging);

		return result;
	}

	// 나의 공모전게시글관리
	@GetMapping("/mySelect")
	public String mySelect(Model model, ContestVO vo, PagingVO paging) {
		model.addAttribute("contestList", service.myContestList(vo, paging));
		model.addAttribute("paging", paging);
		return "content/myPage/myCotestList";
	}

	// 나의공모전관리 지원자조회페이지 이동
	@GetMapping("/designRead")
	public String designRead(Model model, String cNo) {
		model.addAttribute("contest", service.getContest(cNo));
		model.addAttribute("winner", wService.winnerList(cNo));
		return "content/contest/cotestDesignList";
	}

	// 1.나의공모전 -> 응모디자인조회
	// 2.공모전 -> 상세정보
	@PostMapping("/ajaxDesignRead")
	@ResponseBody
	public Map<String, Object> ajaxDesignRead(String cNo, PagingVO paging) {

		Map<String, Object> result = new HashMap<String, Object>();
		DesignVO dVo = new DesignVO();
		dVo.setcNo(cNo);
		// 1건의 공모전에 접수된 디자인리스트
		List<DesignVO> designs = dService.contestDesignList(dVo, paging);

		result.put("design", designs);
		result.put("paging", paging);

		return result; // 디자인리스트+파일리스트 반환.
	}

	// --------------------------------------------------------------------------
	// 공모전 나의 문의리스트 페이지이동(추가해야함)
	@GetMapping("/QnaList")
	public String ContestQnaList() {
		return "content/myPage/myContestQnaList";
	}

	// 임시저장된 게시글 전체들고오기
	@PostMapping("/cSave")
	@ResponseBody
	public List<ContestVO> cSave(ContestVO vo) {
		return service.cSave(vo);
	}

	// 임시저장된 게시글 상세들고오기
	@PostMapping("/saveSelect")
	@ResponseBody
	public ContestVO saveSelect(ContestVO vo) {
		return service.getContest(vo.getcNo());
	}

}
