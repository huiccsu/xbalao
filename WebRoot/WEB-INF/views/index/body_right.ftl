<div class="col-lg-4 con-right">
				<!-- 热点 -->
				<div>
					<div class="input-group">
						<input type="text" class="form-control" id="keywords" placeholder="搜索...">
			      <span class="input-group-btn">
			        <button class="btn btn-info" type="button" id="btnSearch" >搜索</button>
			      </span>
					</div>
					<div class="con-nav">
						<span class="sep">|</span>
						<span>最新热点</span>
						<span class="pull-right"><a href="/article/hot"> 更多>></a></span>
					</div>
					<div class="con-new">
						<ul class="u-hot-news">
							 <#list toplist as item>
									<li>
										<div  class="con-xh  <#if item_index lt 3 > con-bg </#if> pull-left" >${item_index+1}</div>
										<span><a href="#;" id="${item.id}">${item.title}</a></span>
									</li>
							 </#list>
						</ul>
					</div>
				</div>
			<script>
				//相应回车键
				$("#keywords").keydown(function(e){
					if(e.keyCode == 13){
						$.search();
					}
				});
				$.searchBtn();
			</script>

			
				<!--  -->
				<div class="hot">
					<div class="hot-new">
						<div class="con-nav">
							<span class="sep">|</span>
							<span>新闻标签</span>
						</div>
						<div class="hot-button">
							<a href="/article/list?key=区块链" class="btn btn-default topic-2"  >区块链</a><a href="/article/list?key=虚拟币投资" class="btn btn-default topic-3" style="margin-left:5%;margin-right:5%;" >虚拟币投资</a><a href="/article/list?key=区块链技术学习" class="btn btn-default topic-4"  >区块链技术学习</a><a href="/article/list?key=比特币" class="btn btn-default topic-5"  >比特币</a><a href="/article/list?key=区块链应用" class="btn btn-default topic-6" style="margin-left:5%;margin-right:5%;" >区块链应用</a><a href="/article/list?key=行业大佬怎么说" class="btn btn-default topic-1"  >行业大佬怎么说</a><a href="/article/list?key=深度观察" class="btn btn-default topic-2"  >深度观察</a><a href="/article/list?key=区块链政策" class="btn btn-default topic-3" style="margin-left:5%;margin-right:5%;" >区块链政策</a><a href="/article/list?key=大公司之区块链" class="btn btn-default topic-4"  >大公司之区块链</a>						
						</div>
						<div>
							<div id="ad_0"></div>

							<div class="con-nav">
								<span class="sep">|</span>
								<span>推荐阅读</span>
							</div>
							<div class="hot-img">
								<ul>
									  <#list viewerslist1 as item>
										<li>
												<p class="pull-left">
													<a class="l_words" href="#;" id="${item.id}">${item.title}</a>
													<span class="topic-icon">
														<span class="glyphicon glyphicon-time" aria-hidden="true"></span>
														<span class="update_time">${item.ctime_str}  &nbsp;&nbsp; </span>
													</span>
													<span class="topic-icon">
														<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
														<span class="view"> ${item.viewers}</php>阅读</span>
													</span>
												</p>
												<img class="pull-right" src="${item.shortImg}!small">
											</li>
									 </#list>
								</ul>
							</div>

							<div id="ad_1"></div>
							
							<div class="hot-img">
								<ul>
									  <#list viewerslist2 as item>
										<li>
												<p class="pull-left">
													<a class="l_words"  href="#;" id="${item.id}">${item.title}</a>
													<span class="topic-icon">
														<span class="glyphicon glyphicon-time" aria-hidden="true"></span>
														<span class="update_time">${item.ctime_str}  &nbsp;&nbsp; </span>
													</span>
													<span class="topic-icon">
														<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
														<span class="view"> ${item.viewers}</php>阅读</span>
													</span>
												</p>
												<img class="pull-right" src="${item.shortImg}!small">
											</li>
									 </#list>
								</ul>
							</div>

							<div id="ad_2"></div>
							
							<div class="hot-img">
								<ul>
									  <#list viewerslist3 as item>
										<li>
												<p class="pull-left">
													<a class="l_words"  href="#;" id="${item.id}">${item.title}</a>
													<span class="topic-icon">
														<span class="glyphicon glyphicon-time" aria-hidden="true"></span>
														<span class="update_time">${item.ctime_str}  &nbsp;&nbsp; </span>
													</span>
													<span class="topic-icon">
														<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
														<span class="view"> ${item.viewers}</php>阅读</span>
													</span>
												</p>
												<img class="pull-right" src="${item.shortImg}!small">
											</li>
									 </#list>
								</ul>
							</div>

							<div id="ad_3"></div>
				

						</div>
					</div>
				</div>
			</div>
			
			<script>
				eachAd();
				function eachAd()
				{
					var html="";
					 <#list ads as item>
					 	html="";
					 	html+='<div class="r_ads_img">';
						html+='		<a target="_blank" href="${item.url}">';
						html+='			<img src="${item.img}" alt="${item.desc}" title="${item.desc}">';
						html+='		</a>';
						html+='	</div>';
						$("#ad_"+${item_index}).html(html);
					 </#list>
				}
			</script>