function js_rm_topAndBottom(){
    let rmtb_a = document.querySelectorAll("header"),
        rmtb_b = document.querySelectorAll("nav"),
        rmtb_c = document.querySelectorAll("footer");

    if (rmtb_a.length>0){
        rmtb_a[0].style.display ="none";
    }else{
        toErrMsg("rmtb_a");
    }

    if (rmtb_b.length>0){
        rmtb_b[0].style.display ="none";
    }else{
        toErrMsg("rmtb_b");
    }

    if (rmtb_c.length>0){
        rmtb_c[0].style.display ="none";
    }else{
        toErrMsg("rmtb_c");
    }
}

function page2top(){
    document.body.scrollTop = document.documentElement.scrollTop = 0;
}

function js_comic(){
//    <!--设置漫画页面的跳页按钮点击后滚回顶部-->
    $("#prev").attr("onclick","page2top()");
    $("#next").attr("onclick","page2top()");
    $("a[data-action='prev']").attr("onclick","page2top()");
    $("a[data-action='next']").attr("onclick","page2top()");
}

window.onload = function(){
    js_rm_ad();
    js_conf();
    js_rm_topAndBottom();
    js_comic();
}