<div id="myCarousel" class="carousel slide">
			<!-- 轮播（Carousel）指标 -->
			<ol class="carousel-indicators">
				<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
				<li data-target="#myCarousel" data-slide-to="1"></li>
				<li data-target="#myCarousel" data-slide-to="2"></li>
			</ol>
			<!-- 轮播（Carousel）项目 -->
			<div class="carousel-inner" style="height:300px">
			 <#list scollerimgs as item>
				<div class="item <#if item_index==0>active </#if>">
					<a target="_blank" href="${item.url}">
						<img  src="${item.img}" alt="${item.desc}" title="${item.desc}">
					</a>
				</div>
			 </#list>

			</div>
			<!-- 轮播（Carousel）导航 -->
			<a class="carousel-control left" href="#myCarousel"
			   data-slide="prev"></a>
			<a class="carousel-control right" href="#myCarousel"
			   data-slide="next"></a>
</div>
				
			