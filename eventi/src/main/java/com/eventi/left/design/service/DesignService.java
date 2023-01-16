package com.eventi.left.design.service;

import java.util.List;

import com.eventi.left.common.PagingVO;

public interface DesignService {
	public List<DesignVO> designList(DesignVO vo, PagingVO paging);
	
	public List<DesignVO> contestDesignList(String cNo); //공모전 1건에 대한 리스트.
}	
