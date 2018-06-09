$.extend({
    ajaxNews: function (options, callbackSuc, callbackErr) {
        options = $.extend(options, {"_r": Math.random()});
        $.ajax({
            type: options.ajaxtype,
            url: options.url,
            async: true,
            data: options,
            dataType: "json",//数据类型为jsonp
            success: function (data) {
                if ($.isFunction(callbackSuc)) callbackSuc(data);
            },
            error: function (data) {
                if ($.isFunction(callbackErr)) callbackErr(data);
            }
        });
    },
    //get提交加载
    loadingGet: function (param, callbackSuc, callbackErr) {
        param = $.extend(param, {"ajaxtype": "GET"});
        $.ajaxNews(param, callbackSuc, callbackErr);
    },
    //post提交加载
    loadingPost: function (param, callbackSuc, callbackErr) {
        param = $.extend(param, {"ajaxtype": "POST"});
        $.ajaxNews(param, callbackSuc, callbackErr);
    },
    //加载更多
    loadingMore: function (ob) {
        $.unbindloding();
        var param = {"ctime": ctime, "url": "/article/more"};
        $.loadingPost(param, function (data) {
        	var hasMore=true;
        	if(data.code=200)
    		{
        		if(data.ch_msg.length>0)
        		{
        			ob.append($.moreNews(data.ch_msg));
        			hasMore=true;
        		}
        		else 
    			{
        			hasMore=false;
        			$.hasNone();
    			}
    		}
        	if(hasMore)$.moreBtn();
        }, function () {
            $.moreBtn();
        });
    },
    unbindloding: function () {
        $("#m-more-news").unbind();
        $("#m-more-news").html("<div align='center'><img style='width:0.6rem;height:0.64rem' src='/static/img/loding.gif'></div>");
    },
    moreBtn: function () {
        $("#m-more-news").html('<p id="p_more1" style="text-align:center;cursor:pointer;clear: both;font-size: 15px;padding-top: 30px;"><span style="padding: 15px 50px;background-color: #fafafb;border: 1px solid #f1f1f1;margin: 10px 0 0 86px;">加载更多</span></p>');
        $("#m-more-news").bind("click", function () {
            $.loadingMore($("#u_news_list"));
        });
    },
    hasNone:function()
    {
    	 $("#m-more-news").unbind();
         $("#m-more-news").html('<p id="p_more1" style="text-align:center;cursor:pointer;clear: both;font-size: 15px;padding-top: 30px;"><span style="padding: 15px 50px;background-color: #fafafb;border: 1px solid #f1f1f1;margin: 10px 0 0 86px;">已无更多数据</span></p>');
    },
    moreNews:function(data){
    	var html="";
    	for (var int = 0; int <data.length; int++) {
			html+='<li onclick="$.liClick(this);">';
			html+='<a href="#;" id="'+data[int].id+'">';
			html+='	<div class="topic-img pull-left">';
			html+='		<img class="path" src="'+data[int].shortImg+'">';
			html+='	</div>';
			html+='	<div class="topic-word pull-left">';
			html+='		<h5 class="title">'+data[int].title+'</h5>';
			html+='<p class="description">'+data[int].remark+'</p>';
			
			html+='	<div class="topic-icon">';
			html+='	<span class="glyphicon glyphicon-time" aria-hidden="true"></span>';
			html+='	<span class="update_time">'+data[int].ctime_str+'</span>';
			html+='	</div>';
			html+='	<div class="topic-icon">';
			html+='	<span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>';
			html+='	<span class="view">'+data[int].viewers+' </php>  阅读 </span>';
			html+='	</div>';
			html+='</div>';
			html+='</a>';
			html+='</li>';
			ctime=data[int].ctime;
		}
    	
    	return html;
    },
    detail:function(titleStr,id)
    {
    	layer.open({
    	      type: 2,
    	      title: titleStr,
    	      shadeClose: false,
    	      shade: true,
    	      maxmin: true, //开启最大化最小化按钮
    	      area: ['70%', '90%'],
    	      content: '/article/'+id
    	    });
    },
    bindDetail:function(){
    	$("li").on("click",function(){
    		$.liClick(this);
    	});
    },
    liClick:function(ob)
    {
    	var id = $(ob).find("a").attr("id");
    	if(typeof id == "undefined" || id=="")
    		return;
		var title = $(ob).find("h5").text();
		if(typeof title == "undefined" || title=="") title=$(ob).find(".title-left").text()
		if(typeof title == "undefined" || title=="") title=$(ob).find("a").text()
		$.detail(title,id);
    },
    searchBtn:function()
    {
    	$("#btnSearch").on("click",function(){
    		$.search();
    	});
    },
    search:function()
    {
    	var keywords=$.trim($("#keywords").val());
		if(keywords.length<=0)
		{
			return layer.alert("请输入搜索关键词");
		}
		window.location.href="/article/list?key="+keywords;
    },
    lazyLoadingImg: function (select,param,callback)//对图片进行懒加载
    {
        $(select).find("a").each(function (i) {
            $(this).empty().append('<img  src="' + imgs[i] + '" >');
         });
        
    },
    nvation:function()
    {
    	$(".topic-nav > span").each(function(){
    		var target=$(this);
    		target.click(function(){
    			target.siblings("span").removeClass("curr");
    			target.addClass("curr");
    			ctime=0;
    			$("#u_news_list").empty();
    			$.loadingMore($("#u_news_list"),target.attr("value"));
    		});
    	});
    },
    mechined: function () {
        var u = navigator.userAgent;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; // android终端
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); // ios终端
        if (isAndroid) return 0;
        if (isiOS) return 1;
        else return -1;
    },
    isPC: function () {
        var userAgentInfo = navigator.userAgent;
        var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
        var flag = true;
        for (var v = 0; v < Agents.length; v++) {
            if (userAgentInfo.indexOf(Agents[v]) > 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }
});
$(function(){$.bindDetail()});