var result = new Array();
var form;
var currentPage;// 当前页码
var pageSize;// 每页数量
var starttime;
var endtime ;
var u_question;
var a_question;
var selectType = 0;
var language;
$(function () {
    language = getLanguage();
    //重置
    document.getElementById("resetButtonClick").onclick = function(){
        var myInput = document.getElementById("strStartTime");
        myInput.defaultValue = myInput.value;
        document.forms[0].reset()
    }
    layui.use('form', function(){//声明使用layUI的form
        form = layui.form;
        form.render();//刷新所有渲染效果，也可以单独熟悉某个效果
        //监听提交
        //搜索
        form.on('submit(searchBtn)', function(data){
            console.log(data) //当前容器的全部表单字段，名值对形式：{name: value}
           // layer.msg(JSON.stringify(data.field));
             starttime = data.field.strStartTime;
             endtime = data.field.strEndTime;
             u_question = data.field.u_question;
            selectType = data.field.reportType;
            getlist(starttime,endtime,u_question,selectType,currentPage,pageSize);
            return false;
        });
        //替換
        form.on('submit(exportBtn)', function(data){
             console.log(data) //当前容器的全部表单字段，名值对形式：{name: value}
             starttime = data.field.strStartTime;
             endtime = data.field.strEndTime;
             u_question = data.field.u_question;
             a_question = data.field.a_question;
             selectType = data.field.reportType;
             updateQuestion(starttime,endtime,u_question,a_question,selectType);
             return false;
        });
        //监听select
        form.on('select(reportType)', function(data){
            if(data.value == "2"){
                selectType = 2;
            }else if(data.value == "1"){
                selectType = 1;
            }else{
                selectType = 0;
            }
            getlist(starttime,endtime,null,selectType,currentPage,pageSize);
            console.log(data.elem); //得到select原始DOM对象
            console.log(data.value); //得到被选中的值
            console.log(data.othis); //得到美化后的DOM对象
        });
    });
    initSearch();
    getlist(starttime,endtime,u_question,selectType,currentPage,pageSize);

});
function getlist(starttime,endtime,u_question,selectType,currentPage,pageSize) {
    if(currentPage == undefined || currentPage == ""){
        currentPage = 1;// 当前页码
    }
    if(pageSize == undefined || pageSize == ""){
        pageSize = 10;// 每页数量
    }

    $.ajax({
        type : "get",
        async : false,
        url : "../sensitive/list",
        data : {"starttime":starttime,
            "endtime":endtime,
             "language":language,
            "user_question":u_question,
            "selectType": selectType,
            "page": currentPage,
            "limit":pageSize
        },
        dataType : "json",
        //  traditional: true,//这里设为true就可以了
        success : function(result) {

            if(selectType == 0){
                tableForFaq(result);
            }else if(selectType == 1){
                tableForEva(result);
            }else{
                tableForchilck(result);
            }
            setTimeout(function(){layui.use(['laypage', 'layer'], function () {
                var page = layui.laypage;
                if(language == "sc"){
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
                                getlist(starttime,endtime,u_question,selectType,currentPage,pageSize);
                            }
                        }
                    })
                }else if(language == "en"){
                    page.render({
                        elem: 'layui',
                        count: result.count,
                        curr: currentPage,
                        limit: pageSize,
                        groups:5,
                        first: "First",
                        last: "Last",
                        prev:"Prev",
                        next:"Next",
                        layout: ['count', 'prev', 'page', 'next', 'limit'],
                        jump: function (obj, first) {
                            if (!first) {
                                currentPage = obj.curr;
                                if(pageSize != obj.limit){
                                    currentPage = 1;
                                }
                                pageSize = obj.limit;
                                getlist(starttime,endtime,u_question,selectType,currentPage,pageSize);
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
                                getlist(starttime,endtime,u_question,selectType,currentPage,pageSize);
                            }
                        }
                    })
                }


            })},800);


        }
    })
}

/**
 * 替换功能
 * @param starttime
 * @param endtime
 * @param u_question
 * @param a_question
 */
function updateQuestion(starttime,endtime,u_question,a_question) {
    $.ajax({
        type : "get",
        async : false,
        url : "../sensitive/update",
        data : {
            "starttime":starttime,
            "endtime":endtime,
            "language":language,
            "selectType": selectType,
            "user_question":u_question,
            "a_question": a_question
        },
        dataType : "json",
        //  traditional: true,//这里设为true就可以了
        success : function(result) {
            getlist(starttime,null,null,selectType,currentPage,pageSize);

        }
    })

}

/**
 * 加载评价报表数据
 * @param result
 */
function tableForEva(result){
      layui.use(['table','laypage'], function(){
          var table = layui.table;
          var question ;
          if(language == "sc"){
              //展示已知数据
              table.render({
                  elem: '#test'
                  ,cellMinWidth: 80
                  ,height: 'full-200'
                  ,cols: [[ //标题栏type:'numbers'
                      {field:'number',title:'序号',width:70,align: 'left'}
                      ,{field: 'user_id', title: '用户id',width:120 /*width: w1,*/}
                      ,{field: 'vote_time', title: '访问时间',width:160/*, width: w3*/,sort: true}
                      ,{field: 'user_question', title: '用户问题'/*, width: w3*/}
                      ,{field: 'question', title: '标准问题'/*, width: w2*/}
                      ,{field: 'answer', title: '标准答案'/*, width: w2*/}
                      ,{field: 'reason', title: '原因'/*, width: w2*/}
                      ,{field: 'platform', title: '渠道',width:70/*, width: w3*/}
                  ]]
                  ,limit:result.data.length//要传向后台的每页显示条数
                  //,page:true//(自带的这个要注掉)
                  ,data: result.data
                  ,done:function(res,currentPage,pageSize){
                      tdTitle();
                  }

              });
          }else if(language == "en"){
              //展示已知数据
              table.render({
                  elem: '#test'
                  ,cellMinWidth: 80
                  ,height: 'full-200'
                  ,cols: [[ //标题栏type:'numbers'
                      {field:'number',title:'NO.',width:70,align: 'left'}
                      ,{field: 'user_id', title: 'User id',width:120 /*width: w1,*/}
                      ,{field: 'vote_time', title: 'Time Star',width:160/*, width: w3*/,sort: true}
                      ,{field: 'user_question', title: 'Utterance'/*, width: w3*/}
                      ,{field: 'question', title: 'Standard Intent'/*, width: w2*/}
                      ,{field: 'answer', title: 'Answer'/*, width: w2*/}
                      ,{field: 'reason', title: 'Reason'/*, width: w2*/}
                      ,{field: 'platform', title: 'Platform',width:70/*, width: w3*/}
                  ]]
                  ,limit:result.data.length//要传向后台的每页显示条数
                  //,page:true//(自带的这个要注掉)
                  ,data: result.data
                  ,done:function(res,currentPage,pageSize){
                      tdTitle();
                  }

              });
          }else{
              //展示已知数据
              table.render({
                  elem: '#test'
                  ,cellMinWidth: 80
                  ,height: 'full-200'
                  ,cols: [[ //标题栏type:'numbers'
                      {field:'number',title:'序号',width:70,align: 'left'}
                      ,{field: 'user_id', title: '用戶id',width:120 /*width: w1,*/}
                      ,{field: 'vote_time', title: '存取時間',width:160/*, width: w3*/,sort: true}
                      ,{field: 'user_question', title: '用戶問題'/*, width: w3*/}
                      ,{field: 'question', title: '標準問題'/*, width: w2*/}
                      ,{field: 'answer', title: '標準答案'/*, width: w2*/}
                      ,{field: 'reason', title: '原因'/*, width: w2*/}
                      ,{field: 'platform', title: '渠道',width:70/*, width: w3*/}
                  ]]
                  ,limit:result.data.length//要传向后台的每页显示条数
                  //,page:true//(自带的这个要注掉)
                  ,data: result.data
                  ,done:function(res,currentPage,pageSize){
                      tdTitle();
                  }

              });
          }

      });
}

/**
 * 加载点击报表数据
 * @param result
 */
function tableForchilck(result){
    layui.use(['table','laypage'], function(){
        var table = layui.table;
        var question ;
        //展示已知数据
        if(language == "sc"){
            table.render({
                elem: '#test'
                ,cellMinWidth: 80
                ,height: 'full-200'
                ,cols: [[ //标题栏type:'numbers'
                    {field:'number',title:'序号',width:70,align: 'left'}
                    ,{field: 'userId', title: '用户id',width:120/*width: w1,*/}
                    ,{field: 'trigger_time', title: '访问时间',width:160/*, width: w3*/,sort: true}
                    ,{field: 'u_question', title: '用户问题'/*, width: w3*/}
                    ,{field: 'b_question', title: '标准问题'/*, width: w2*/}
                    ,{field: 'answer', title: '标准答案'/*, width: w2*/}
                    ,{field: 'platform', title: '渠道',width:70/*, width: w3*/}
                ]]
                ,limit:result.data.length//要传向后台的每页显示条数
                //,page:true//(自带的这个要注掉)
                ,data: result.data
                ,done:function(res,currentPage,pageSize){
                    tdTitle();
                }

            });
        }else if(language == "en"){
            table.render({
                elem: '#test'
                ,cellMinWidth: 80
                ,height: 'full-200'
                ,cols: [[ //标题栏type:'numbers'
                    {field:'number',title:'NO.',width:70,align: 'left'}
                    ,{field: 'userId', title: 'User id',width:120/*width: w1,*/}
                    ,{field: 'trigger_time', title: 'Time Star',width:160/*, width: w3*/,sort: true}
                    ,{field: 'u_question', title: 'Utterance'/*, width: w3*/}
                    ,{field: 'b_question', title: 'Standard Intent'/*, width: w2*/}
                    ,{field: 'answer', title: 'Answer'/*, width: w2*/}
                    ,{field: 'platform', title: 'Platform',width:70/*, width: w3*/}
                ]]
                ,limit:result.data.length//要传向后台的每页显示条数
                //,page:true//(自带的这个要注掉)
                ,data: result.data
                ,done:function(res,currentPage,pageSize){
                    tdTitle();
                }

            });
        }else{
            table.render({
                elem: '#test'
                ,cellMinWidth: 80
                ,height: 'full-200'
                ,cols: [[ //标题栏type:'numbers'
                    {field:'number',title:'序号',width:70,align: 'left'}
                    ,{field: 'userId', title: '用戶id',width:120/*width: w1,*/}
                    ,{field: 'trigger_time', title: '存取時間',width:160/*, width: w3*/,sort: true}
                    ,{field: 'u_question', title: '用戶問題'/*, width: w3*/}
                    ,{field: 'b_question', title: '標準問題'/*, width: w2*/}
                    ,{field: 'answer', title: '標準答案'/*, width: w2*/}
                    ,{field: 'platform', title: '渠道',width:70/*, width: w3*/}
                ]]
                ,limit:result.data.length//要传向后台的每页显示条数
                //,page:true//(自带的这个要注掉)
                ,data: result.data
                ,done:function(res,currentPage,pageSize){
                    tdTitle();
                }

            });
        }

    });
}

/**
 * 加载自动问答明细报表数据
 * @param result
 */
function tableForFaq(result){
    layui.use(['table','laypage'], function(){
        var table = layui.table;
        var question ;
        //展示已知数据
        if(language == "sc"){
            table.render({
                elem: '#test'
                ,cellMinWidth: 80
                ,height: 'full-200'
                ,cols: [[ //标题栏type:'numbers'
                    {field:'numeber',title:'序号',width:70,align: 'left'}
                    ,{field: 'user_id', title: '用戶id',width:120}
                    ,{field: 'visit_time', title: '访问时间',width:160/*, width: w3*/,sort: true}
                    ,{field: 'user_question', title: '用户问题'/*, width: w3*/}
                    ,{field: 'question', title: '标准问题'/*, width: w2*/}
                    ,{field: 'answer', title: '标准答案'/*, width: w2*/}
                    ,{field: 'platform', title: '渠道',width:70/*, width: w3*/}
                ]]
                ,limit:result.data.length//要传向后台的每页显示条数
                //,page:true//(自带的这个要注掉)
                ,data: result.data
                ,done:function(res,currentPage,pageSize){
                    tdTitle();
                }

            });
        }else if(language == "en"){
            table.render({
                elem: '#test'
                ,cellMinWidth: 80
                ,height: 'full-200'
                ,cols: [[ //标题栏type:'numbers'
                    {field:'numeber',title:'NO.',width:70,align: 'left'}
                    ,{field: 'user_id', title: 'User id',width:120}
                    ,{field: 'visit_time', title: 'Time Star',width:160/*, width: w3*/,sort: true}
                    ,{field: 'user_question', title: 'Utterance'/*, width: w3*/}
                    ,{field: 'question', title: 'Standard Intent'/*, width: w2*/}
                    ,{field: 'answer', title: 'Answer'/*, width: w2*/}
                    ,{field: 'platform', title: 'Platform',width:70/*, width: w3*/}
                ]]
                ,limit:result.data.length//要传向后台的每页显示条数
                //,page:true//(自带的这个要注掉)
                ,data: result.data
                ,done:function(res,currentPage,pageSize){
                    tdTitle();
                }

            });
        }else{
            table.render({
                elem: '#test'
                ,cellMinWidth: 80
                ,height: 'full-200'
                ,cols: [[ //标题栏type:'numbers'
                    {field:'numeber',title:'序号',width:70,align: 'left'}
                    ,{field: 'user_id', title: '用戶id',width:120}
                    ,{field: 'visit_time', title: '存取時間',width:160/*, width: w3*/,sort: true}
                    ,{field: 'user_question', title: '用戶問題'/*, width: w3*/}
                    ,{field: 'question', title: '標準問題'/*, width: w2*/}
                    ,{field: 'answer', title: '標準答案'/*, width: w2*/}
                    ,{field: 'platform', title: '渠道',width:70/*, width: w3*/}
                ]]
                ,limit:result.data.length//要传向后台的每页显示条数
                //,page:true//(自带的这个要注掉)
                ,data: result.data
                ,done:function(res,currentPage,pageSize){
                    tdTitle();
                }

            });
        }


    });
}

function tdTitle(){
    $('th').each(function(index,element){
        $(element).attr('title',$(element).text());
    });
    $('td').each(function(index,element){
        $(element).attr('title',$(element).text());
    });
};
