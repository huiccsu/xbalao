/**
 * Created by wo on 2018/2/28.
 */

$(function(){

    $("#myCarousel").carousel();
});

function get_market_timer(){
    get_market_24_detail();

}

//鑾峰彇 Market Detail 24灏忔椂鎴愪氦閲忔暟鎹�
function get_market_24_detail(){
    $.post("/index.php?c=index&a=get_market_24_detail", {}, function(data){
        if(data.code == '0'){
            var list = data.data;
            var tr_ = $("#market_tab tr:eq(1)").clone();
            $("#market_tab > tbody").html("");
            for(var i = 0; i < list.length; i++){
                var tr = tr_.clone();
                var rose = (list[i]['rose']*100).toFixed(2);
                tr.find("td:eq(0)").html(list[i]['symbol']);
                tr.find("td:eq(1)").html(list[i]['close']);
                tr.find("td:eq(2)").html(rose + "%");
                tr.find("td:eq(3)").html(list[i]['amount'].toFixed(0));
                tr.find("td:eq(4)").html(list[i]['high']);
                tr.find("td:eq(5)").html(list[i]['low']);

                if(rose > 0.00){
                    tr.find("td:eq(1),td:eq(2)").attr("class", "sec");
                }else if(rose < 0.00){
                    tr.find("td:eq(1),td:eq(2)").attr("class", "red");
                }else{
                    tr.find("td:eq(1),td:eq(2)").attr("class", "");
                }

                $("#market_tab > tbody").append(tr);
            }


        }
    }, 'json');
}

$(".topic-nav>span").click(function(e){
    $(this).addClass("curr").siblings("span").removeClass("curr");
    var value = $(this).attr("value");
    $("#u_news_list").html("");
    pageIndex = 0;
    $("#p_more1").click();
});


/*棣栭〉鍔犺浇鏂囩珷 start */
var pageIndex = 2;
var li_clone = $("#u_news_list>li:eq(0)").clone();
$("#p_more1").click(function(){
    var sType=$(".topic-nav>span.curr").attr("value");
    $.post("/index.php?c=index&a=get_more_news", { pageIndex : pageIndex, s_type: sType}, function(data){
        if(data && data.code == '0') {
            pageIndex++;
            var rows = data.data;
            for(var i = 0; i < rows.length; i++){
                var li = li_clone.clone();
                var href = '/article/'+ rows[i]['id'] + '.html';
                var title = rows[i]['title'];
                var description = rows[i]['description'];
                var update_time = rows[i]['update_time'];
                update_time = timestampToTime(update_time).substr(0, 16);
                var view = (rows[i]['view'] * 99 + 1) + ' 闃呰 ';
                li.find("a").attr("href", href);
                li.find(".title").html(title);
                li.find(".description").html(description);
                li.find(".update_time").html(update_time);
                li.find(".view").html(view);

                var path = "/Public/Home/images/loaction0.jpg";
                if(rows[i]['path']){
                    path = rows[i]['path'];
                }
                li.find(".path").attr("src", path);
                $("#u_news_list").append(li);
            }

        }
    }, 'json');
});

function timestampToTime(timestamp) {
    var date = new Date(timestamp * 1000);//鏃堕棿鎴充负10浣嶉渶*1000锛屾椂闂存埑涓�13浣嶇殑璇濅笉闇€涔�1000
    Y = date.getFullYear() + '-';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    D = add0(date.getDate()) + ' ';
    h = add0(date.getHours()) + ':';
    m = add0(date.getMinutes()) + ':';
    s = add0(date.getSeconds());
    return Y+M+D+h+m+s;
}
function add0(m){return m<10?'0'+m:m }

$(window).scroll(function(){
    var scrollTop = $(this).scrollTop();
    var scrollHeight = $(document).height();
    var windowHeight = $(this).height();
    if(scrollTop + windowHeight == scrollHeight){
        if(pageIndex < 3){
            $("#p_more1").click();
        }
    }
});



