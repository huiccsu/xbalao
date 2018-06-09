<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="utf-8">
	<title>区块链见闻 | 最专业的区块链资讯平台,区块链新闻,区块链创投融资平台,前沿行业动态,项目股权投资网站,区块链开发</title>
	<!--#include file="/static/model/header.html"-->
</head>

<body>

<!-- 头部导航栏 -->
<div class="navbar">

	<!--#include file="/static/model/top.html"-->
	
	<!--#include file="/static/model/menu.html"-->	
	
<!-- 页面主体部分-->

	<div id="bdw" class="main-top">
		<div class="wrap cf clearfix">
			<!--#include file="/static/cache/type.html"-->
			<div class="article_box_left f_l">
				<div class="list">
					<ul class="article_cate">
						 <#list articles as item>
							<li class="article_cate_li">
									<a href="#;" id="${item.id}">
										<img data-original="${item.shortImg}" class="a_h_img" >
										<div class="article_cate_li_info">
											<div class="mod_box ">
												<div class="mod_title">
													<p class="title-left" >${item.title}</p>
													<div class="atc_rt_tm">
														<span class="article_cate_li_info_date">${item.ctime_str}</span>
														<div class="topic-icon">
															<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
															<span class="view"> ${item.viewers}  阅读 </span>
														</div>
													</div>
		
												</div>
												<div class="mod_center">
													<div class="article_cate_li_info_content" style="width:100%;">
														${item.remark}
												</div>
											</div>
										</div>
									</a>
								</li>
						 </#list>
					</ul>
				</div>
				<div class="think-page">${page}</div>
			</div>
			<div class="article_box_right f_r">
				
			</div>
		</div>
		
		<!--#include file="/static/model/footer.html"-->	
		
	</div>
</body>
<script>
$(function() {$("img").lazyload();});
</script>
</html>
