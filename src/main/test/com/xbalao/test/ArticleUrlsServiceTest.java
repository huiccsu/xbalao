package com.xbalao.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xbalao.service.impl.ArticleUrlsService;

public class ArticleUrlsServiceTest extends BaseTest {
	@Autowired
	ArticleUrlsService articleUrlsService;
	
	@Test
	public void testname() throws Exception {
		articleUrlsService.spider(1);
	}
}
