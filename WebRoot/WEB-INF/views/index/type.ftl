<div class="topic-nav">
	<span ><a href="/article/list">最新</a></span>
	<#list types as item>
		<span  ><a href="/article/list?key=${item.name}">${item.name}</a></span>
	</#list>			
</div>