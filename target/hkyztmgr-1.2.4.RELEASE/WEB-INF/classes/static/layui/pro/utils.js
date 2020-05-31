function getLanguage() {
    var rest = window.location.pathname;
    //var index = rest.lastIndexOf("\/");
    var str = rest.substring(rest.lastIndexOf('/'), rest.length);
    console.log("攫取的url:"+str);
    if(str.indexOf("SC") != -1){
        str = "sc"
    }else if(str.indexOf("EN") != -1){
        str = "en"
    }else{
        str = "hk"
    }
    return str;
}

//加载搜索项
function initSearch(){
    var language = getLanguage();
    layui.use(['laydate'], function(){
        var laydate = layui.laydate;
        if(language == "en"){
            laydate.render({
                elem: '#strStartTime',
                value:getCurrentMonthFirst()+" 00:00:00",
                theme: '#1e9fff',
                type: 'datetime'
                ,lang: 'en'
                // max : getCurrentMonthFirst()
            });
            laydate.render({
                elem: '#strEndTime',
                value:"",
                theme: '#1e9fff',
                type: 'datetime'
                ,lang: 'en'
                //  max : getNowFormatDate()
            })
        }else{
            laydate.render({
                elem: '#strStartTime',
                value:getCurrentMonthFirst()+" 00:00:00",
                theme: '#1e9fff',
                type: 'datetime'
                // max : getCurrentMonthFirst()
            });
            laydate.render({
                elem: '#strEndTime',
                value:"",
                theme: '#1e9fff',
                type: 'datetime'

                //  max : getNowFormatDate()
            })
        }

    });
}
//获取当前月的第一天
function getCurrentMonthFirst(){
    var date = new Date();
    date.setDate(1);
    var month = parseInt(date.getMonth()+1);
    var day = date.getDate();
    if (month < 10) {
        month = '0' + month
    }
    if (day < 10) {
        day = '0' + day
    }
    return date.getFullYear() + '-' + month + '-' + day;
}
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month
        + seperator1 + strDate + " " + date.getHours() + seperator2
        + date.getMinutes() + seperator2 + date.getSeconds();
    return currentdate;
}
