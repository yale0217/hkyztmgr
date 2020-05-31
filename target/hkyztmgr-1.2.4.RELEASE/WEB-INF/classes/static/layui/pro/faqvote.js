var result = new Array();
var form;
var currentPage;// 当前页码
var pageSize;// 每页数量
var language;
$(function () {
    language = getLanguage();
    layui.use('form', function(){//声明使用layUI的form
        form = layui.form;
        form.render();//刷新所有渲染效果，也可以单独熟悉某个效果
        //监听提交
        form.on('submit(searchBtn)', function(data){

            console.log(data) //当前容器的全部表单字段，名值对形式：{name: value}
           // layer.msg(JSON.stringify(data.field));
            var starttime = data.field.strStartTime;
            var endtime = data.field.strEndTime;
            var user_question = data.field.user_question;
            var faqvote_type = data.field.faqvote_type;
            var platform = data.field.platform;
            var answer = data.field.answer;
            faqvotelist(starttime,endtime,user_question,faqvote_type,platform,answer,currentPage,pageSize);
            return false;
        });
        form.on('submit(exportBtn)', function(data){

            console.log(data) //当前容器的全部表单字段，名值对形式：{name: value}
            // layer.msg(JSON.stringify(data.field));
            var starttime = data.field.strStartTime;
            var endtime = data.field.strEndTime;
            var user_question = data.field.user_question;
            var faqvote_type = data.field.faqvote_type;
            var platform = data.field.platform;
            var answer = data.field.answer;

            location.href= "../faqvote/exceportFaqvote?starttime="+starttime+"&endtime="+endtime+
                "&user_question="+user_question+"&faqvote_type="+faqvote_type+"&platform="+platform+"&answer="+answer;
            return false;
        });
    });
    //transformLang();
    initSearch();
    faqvotelist();

});
//加载搜索项
// function initSearch(){
//     layui.use(['laydate'], function(){
//         var laydate = layui.laydate;
//         laydate.render({
//             elem: '#strStartTime',
//             value:getCurrentMonthFirst()+" 00:00:00",
//             theme: '#1e9fff',
//             type: 'datetime'
//            // max : getCurrentMonthFirst()
//         });
//         laydate.render({
//             elem: '#strEndTime',
//             value:"",
//             theme: '#1e9fff',
//             type: 'datetime'
//           //  max : getNowFormatDate()
//         })
//     });
// }
//获取当前月的第一天
// function getCurrentMonthFirst(){
//     var date = new Date();
//     date.setDate(1);
//     var month = parseInt(date.getMonth()+1);
//     var day = date.getDate();
//     if (month < 10) {
//         month = '0' + month
//     }
//     if (day < 10) {
//         day = '0' + day
//     }
//     return date.getFullYear() + '-' + month + '-' + day;
// }

function table(result){
    layui.use(['table','laypage'], function(){
        var table = layui.table;
        if(language == "en"){
            table.render({
                elem: '#test'
                ,cellMinWidth: 80
                ,height: 'full-200'
                ,cols: [[ //标题栏type:'numbers'
                    {field: 'number', title: 'Number', /*width: w1,*/ fixed: 'left'}
                    ,{field: 'user_id', title: 'User Id', /*width: w1,*/ fixed: 'left'}
                    ,{field: 'user_question', title: 'Utterance'/*, width: w3*/}
                    ,{field: 'question', title: 'Standard Intent'/*, width: w2*/}
                    ,{field: 'answer', title: 'Answer'/*, width: w2*/}
                    ,{field: 'vote_time', title: 'Time Start'/*, width: w3*/,sort: true}
                    ,{field: 'action', title: 'Type'/*, width: w3*/}
                    ,{field: 'reason', title: 'Reason'/*, width: w2*/}
                    ,{field: 'platform', title: 'Platform'/*, width: w1*/}

                ]]
                ,limit:result.data.length//要传向后台的每页显示条数
                // ,page: true //是否显示分页
                ,data: result.data
                ,done:function(res){
                    tdTitle();
                    $("[data-field='action']").children().each(function(){
                        if($(this).text()=='1'){
                            $(this).text("Helpful")
                        }else if($(this).text()=='2'){
                            $(this).text("NoHelpful")
                        }
                    })
                }
                // ,limit: 20 //每页默认显示的数量
            });
        }else if(language == "sc"){
            table.render({
                elem: '#test'
                ,cellMinWidth: 80
                ,height: 'full-200'
                ,cols: [[ //标题栏type:'numbers'
                    {field: 'number', title: '序号', /*width: w1,*/ fixed: 'left'}
                    ,{field: 'user_id', title: '用户id', /*width: w1,*/ fixed: 'left'}
                    ,{field: 'user_question', title: '用户问题'/*, width: w3*/}
                    ,{field: 'question', title: '标准问题'/*, width: w2*/}
                    ,{field: 'answer', title: '标准答案'/*, width: w2*/}
                    ,{field: 'vote_time', title: '存取时间'/*, width: w3*/,sort: true}
                    ,{field: 'action', title: '类型'/*, width: w3*/}
                    ,{field: 'reason', title: '原因'/*, width: w2*/}
                    ,{field: 'platform', title: '平台'/*, width: w1*/}

                ]]
                ,limit:result.data.length//要传向后台的每页显示条数
                // ,page: true //是否显示分页
                ,data: result.data
                ,done:function(res){
                    tdTitle();
                    $("[data-field='action']").children().each(function(){
                        if($(this).text()=='1'){
                            $(this).text("解决")
                        }else if($(this).text()=='2'){
                            $(this).text("未解决")
                        }
                    })
                }
                // ,limit: 20 //每页默认显示的数量
            });
        }else{
            table.render({
                elem: '#test'
                ,cellMinWidth: 80
                ,height: 'full-200'
                ,cols: [[ //标题栏type:'numbers'
                    {field: 'number', title: '序號', /*width: w1,*/ fixed: 'left'}
                    ,{field: 'user_id', title: '用戶id', /*width: w1,*/ fixed: 'left'}
                    ,{field: 'user_question', title: '用戶問題'/*, width: w3*/}
                    ,{field: 'question', title: '標準問題'/*, width: w2*/}
                    ,{field: 'answer', title: '標準答案'/*, width: w2*/}
                    ,{field: 'vote_time', title: '存取時間'/*, width: w3*/,sort: true}
                    ,{field: 'action', title: '類型'/*, width: w3*/}
                    ,{field: 'reason', title: '原因'/*, width: w2*/}
                    ,{field: 'platform', title: '平台'/*, width: w1*/}

                ]]
                ,limit:result.data.length//要传向后台的每页显示条数
                // ,page: true //是否显示分页
                ,data: result.data
                ,done:function(res){
                    tdTitle();
                    $("[data-field='action']").children().each(function(){
                        if($(this).text()=='1'){
                            $(this).text("解決")
                        }else if($(this).text()=='2'){
                            $(this).text("未解決")
                        }
                    })
                }
                // ,limit: 20 //每页默认显示的数量
            });
        }

    });
}
//获取查询结果
function faqvotelist(starttime,endtime,user_question,faqvote_type,platform,answer,currentPage,pageSize){
    if(currentPage == undefined || currentPage == ""){
        currentPage = 1;// 当前页码
    }
    if(pageSize == undefined || pageSize == ""){
        pageSize = 10;// 每页数量
    }
    var main = $('#main');
    $.ajax({
        type : "get",
        async : false,
        url : "../faqvote/list",
        data : {"starttime":starttime,
            "endtime":endtime,
            "user_question":user_question,
            "faqvote_type":faqvote_type,
            "platform":platform,
            "answer":answer,
            "page": currentPage,
            "language": language,
            "limit":pageSize
        },
        dataType : "json",
        traditional: true,//这里设为true就可以了
        success : function(result) {
            console.log("rest::::"+result.data);
            table(result);
            setTimeout(function(){layui.use(['laypage', 'layer'], function () {
                var page = layui.laypage;
                if(language == "en"){
                    page.render({
                        elem: 'layui',
                        count: result.count,
                        curr: currentPage,
                        limit: pageSize,
                        groups:5,
                        first: "Home",
                        last: "Last",
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                        jump: function (obj, first) {
                            if (!first) {
                                currentPage = obj.curr;
                                if(pageSize != obj.limit){
                                    currentPage = 1;
                                }
                                pageSize = obj.limit;
                                faqvotelist(starttime,endtime,user_question,faqvote_type,platform,answer,currentPage,pageSize);
                            }
                        }
                    })
                }else if(language == "sc"){
                    page.render({
                        elem: 'layui',
                        count: result.count,
                        curr: currentPage,
                        limit: pageSize,
                        groups:5,
                        first: "首页",
                        last: "尾页",
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                        jump: function (obj, first) {
                            if (!first) {
                                currentPage = obj.curr;
                                if(pageSize != obj.limit){
                                    currentPage = 1;
                                }
                                pageSize = obj.limit;
                                faqvotelist(starttime,endtime,user_question,faqvote_type,platform,answer,currentPage,pageSize);
                            }
                        }
                    })
                }else{
                    page.render({
                        elem: 'layui',
                        count: result.count,
                        curr: currentPage,
                        limit: pageSize,
                        groups:5,
                        first: "首頁",
                        last: "尾頁",
                        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                        jump: function (obj, first) {
                            if (!first) {
                                currentPage = obj.curr;
                                if(pageSize != obj.limit){
                                    currentPage = 1;
                                }
                                pageSize = obj.limit;
                                faqvotelist(starttime,endtime,user_question,faqvote_type,platform,answer,currentPage,pageSize);
                            }
                        }
                    })
                }


            })},800);
        }
    });
   // return result;
}
function tdTitle(){
    $('th').each(function(index,element){
        $(element).attr('title',$(element).text());
    });
    $('td').each(function(index,element){
        $(element).attr('title',$(element).text());
    });
};
// function getNowFormatDate() {
//     var date = new Date();
//     var seperator1 = "-";
//     var seperator2 = ":";
//     var month = date.getMonth() + 1;
//     var strDate = date.getDate();
//     if (month >= 1 && month <= 9) {
//         month = "0" + month;
//     }
//     if (strDate >= 0 && strDate <= 9) {
//         strDate = "0" + strDate;
//     }
//     var currentdate = date.getFullYear() + seperator1 + month
//         + seperator1 + strDate + " " + date.getHours() + seperator2
//         + date.getMinutes() + seperator2 + date.getSeconds();
//     return currentdate;
// }


// function getLanguage() {
//     var rest = window.location.pathname;
//     var index = rest.split("/")[1];
//     console.log("攫取的url"+index);
//     return index;
// }