			<div class="col-lg-8 con-left">
				<!--轮播-->
				<!--#include file="/static/cache/scoller.html"-->
				<!--#include file="/static/cache/type.html"-->		
					
				<!-- 热点详情 -->
				<div class="topic">
				
					<div class="topic-list" style="margin-top: 12px;">
						<ul id="u_news_list">
							 <#list leftList as item>
								<li>
									<a href="#;" id="${item.id}">
										<div class="topic-img pull-left">
											<img class="path" data-original="${item.shortImg}">
										</div>
										<div class="topic-word pull-left">
											<h5 class="title">${item.title}</h5>
											<p class="description">${item.remark}</p>
	
											<div class="topic-icon">
												<span class="glyphicon glyphicon-time" aria-hidden="true"></span>
												<span class="update_time">${item.ctime_str}</span>
											</div>
											<div class="topic-icon">
												<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
												<span class="view"> ${item.viewers}</php>  阅读 </span>
											</div>
	
										</div>
									</a>
								</li>
							 </#list>
						</ul>
						
						<div id="m-more-news" type="-1">
							<p id="p_more1" style="text-align:center;cursor:pointer;clear: both;font-size: 15px;padding-top: 30px;">
								<span style="padding: 15px 50px;background-color: #fafafb;border: 1px solid #f1f1f1;margin: 10px 0 0 86px;">加载更多</span>
							</p>
						</div>
						
					</div>
				</div>
			</div>
<script>
var ctime=${minctime};
$(function(){$.moreBtn(0);$.nvation()});
</script>			