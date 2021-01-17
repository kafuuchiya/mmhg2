function toErrMsg(err){
    window.toAndroid.toErr(err);
}

function toMobile(url){
    window.toAndroid.toNext(url);
}

function historyEvent(){
    let a = $("#popHistory>ul a");

    if (a.length > 0){
        a.forEach(function(x){
            if(x.href.indexOf("javascript") != -1){
                return;
            }
            $(x).attr("onclick","closeDiv()");
            changeAtag(x)
        });
    }else{
        console.log("mmhg2-log: historyEvent()");
    }
}

function closeDiv(){
    let a = document.querySelectorAll("#popHistory");
    if (a.length > 0){
        a[0].style.display = "none";
        con_f.removeClass("icon-history-on");
    }
}

function changeAtag(e){
    let temp = e.href;
    e.href="javascript:toMobile(\""+temp+"\");";
}


function js_rm_ad(){
    let rm_a = document.querySelectorAll("div#rn_ad_native_ai63w"),
        rm_b = document.querySelectorAll("body > div.book-detail > iframe"),
        rm_c = document.querySelectorAll("body > iframe"),
        rm_d = document.querySelectorAll("div.sitemaji_banner");

    if (rm_a.length>0){
        rm_a[0].style.display="none";
    }else{
        toErrMsg("rm_a");
    }

    if (rm_b.length>0){
        rm_b[0].style.display="none";
    }else{
        toErrMsg("rm_b");
    }

    if (rm_c.length>0){
        rm_c[0].style.display="none";
    }else{
        toErrMsg("rm_c");
    }

    if (rm_d.length>0){
        rm_d.forEach(x => x.style.display="none")
    }else{
        toErrMsg("rm_d");
    }
}

function js_conf(){
    let con_a = document.querySelectorAll("body > nav > ul > li:nth-child(3) > a");
        con_b = $(".top-slider-wrap>li").children(),
        con_c = $(".main-list-wrap>ul>li").children(),
        con_d = $("#detail"),
        con_e = $("#detail>li>a"),
        con_f = $("#history");

//    <!--设置漫画大全页面的显示锁定为日本和最近更新-->
    if ( con_a.length > 0 ) {
        con_a[0].href = "/list/japan/update.html";
    }else{
        toErrMsg("con_a");
    }

//    <!--设置首页的漫画元素与Android交互-->
    if ( con_b.length > 0 ) {
        con_b.forEach(function(x){
            changeAtag(x);
        });
    }else{
        toErrMsg("con_b");
    }

    if ( con_c.length > 0 ) {
        con_c.forEach(function(x){
            changeAtag(x);
        });
    }else{
        toErrMsg("con_c");
    }

    if ( con_d.length > 0 ) {

//        <!--对列表默认显示的20个元素进行修改-->
        if( con_e.length==20 ) {
            con_e.forEach(function(x){
                changeAtag(x);
            });
        }else{
            toErrMsg("con_e");
        }

//        <!--对列表新增元素进行修改-->
//        <!--监听的目标-->
        var targetNode = con_d[0];
//        <!--监听的属性，有很多！详细谷歌！-->
        var options = {childList: true};
//        <!--回调事件-->
        function callback(mutationsList, observer) {
            mutationsList.forEach(function(e){
                var x = e.addedNodes[0].children[1];
                changeAtag(x)
            });
        }
        var mutationObserver = new MutationObserver(callback);
        mutationObserver.observe(targetNode, options);
    }else{
        toErrMsg("con_d");
    }

    if (con_f.length>0){
        con_f.attr("onclick","historyEvent()");
    }else{
        toErrMsg("con_f");
    }

}

window.onload = function(){
    js_rm_ad();
    js_conf();
}


