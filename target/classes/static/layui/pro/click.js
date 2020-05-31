var result = new Array();
var form;
var currentPage = 1;// 当前页码
var pageSize = 10;// 每页数量
var starttime;
var endtime;
var u_question;
var catname;
var b_question;
var answer;
var userId;
var source;
var statistics;
var language;
$(function () {
    language = getLanguage();
    document.getElementById("resetButtonClick").onclick = function () {
        var myInput = document.getElementById("strStartTime");
        myInput.defaultValue = myInput.value;
        document.forms[0].reset()
    }
    layui.use('form', function () {//声明使用layUI的form
        form = layui.form;
        form.render();//刷新所有渲染效果，也可以单独熟悉某个效果
        //监听提交
        form.on('submit(searchBtn)', function (data) {
            console.log(data) //当前容器的全部表单字段，名值对形式：{name: value}
            // layer.msg(JSON.stringify(data.field));
            starttime = data.field.strStartTime;
            endtime = data.field.strEndTime;
            u_question = data.field.u_question;
            catname = data.field.catname;
            b_question = data.field.b_question;
            answer = data.field.answer;
            userId = data.field.userId;
            source = data.field.source;
            statistics = data.field.statistics;
            getlist(starttime, endtime, u_question, catname, b_question, answer, userId, source, statistics, currentPage, pageSize);
            return false;
        });
        form.on('submit(exportBtn)', function (data) {
            console.log(data) //当前容器的全部表单字段，名值对形式：{name: value}
            // layer.msg(JSON.stringify(data.field));
            starttime = data.field.strStartTime;
            endtime = data.field.strEndTime;
            u_question = data.field.u_question;
            catname = data.field.catname;
            b_question = data.field.b_question;
            answer = data.field.answer;
            userId = data.field.userId;
            source = data.field.source;
            statistics = data.field.statistics;
            location.href = "../click/exceportClick?starttime=" + starttime + "&endtime=" + endtime + "&language="+language+
                "&u_question=" + u_question + "&catname=" + catname + "&b_question=" + b_question + "&answer=" + answer + "&userId=" + userId + "&=source" + source + "&statistics=" + statistics;
            return false;
        });
        form.on('submit(moreBtn)', function (data) {
            if(language == "en"){
                if (data.elem.offsetParent.lastElementChild.style.display == "none") {
                    data.elem.offsetParent.lastElementChild.style.display = "block";
                    data.elem.innerText = "Cover"
                } else {
                    data.elem.offsetParent.lastElementChild.style.display = "none";
                    data.elem.innerText = "More"
                }
            }else if(language == "sc"){
                if (data.elem.offsetParent.lastElementChild.style.display == "none") {
                    data.elem.offsetParent.lastElementChild.style.display = "block";
                    data.elem.innerText = "隐藏"
                } else {
                    data.elem.offsetParent.lastElementChild.style.display = "none";
                    data.elem.innerText = "更多"
                }
            }else{
                if (data.elem.offsetParent.lastElementChild.style.display == "none") {
                    data.elem.offsetParent.lastElementChild.style.display = "block";
                    data.elem.innerText = "隱藏"
                } else {
                    data.elem.offsetParent.lastElementChild.style.display = "none";
                    data.elem.innerText = "更多"
                }
            }


            return false;
        });
    });
    layui.use(['form', 'upload', 'layer'], function () {
        var form = layui.form;
        //检查项目添加到下拉框中
        $.ajax({
            url: '../click/getCategoryName',
            data: {"language": language},
            dataType: 'json',
            type: 'get',
            success: function (data) {
                console.log(data);
                $.each(data, function (index, item) {
                    if (index == "data") {
                        $.each(item, function (index, value) {
                            $('#catname').append(new Option(value.name, value.name));// 下拉菜单里添加元素
                        });
                    }
                });
                layui.form.render("select");
//重新渲染 固定写法
            }
        })

    });
    //transformLang();
    initSearch();
    getlist(starttime, endtime, u_question, catname, b_question, answer, userId, source, statistics, currentPage, pageSize);

});

function getlist(starttime, endtime, u_question, catname, b_question, answer, userId, source, statistics, currentPage, pageSize) {
    $.ajax({
        type: "get",
        async: false,
        url: "../click/list",
        data: {
            "starttime": starttime,
            "endtime": endtime,
            "userId": userId,
            "u_question": u_question,
            "catname": catname,
            "answer": answer,
            "b_question": b_question,
            "source": source,
            "statistics": statistics,
            "language": language,
            "page": currentPage,
            "limit": pageSize
        },
        dataType: "json",
        //  traditional: true,//这里设为true就可以了
        success: function (result) {
            table(result);
            setTimeout(function () {
                layui.use(['laypage', 'layer'], function () {
                    var page = layui.laypage;
                    if (language == "en") {
                        page.render({
                            elem: 'layui',
                            count: result.count,
                            curr: currentPage,
                            limit: pageSize,
                            groups: 5,
                            first: "first",
                            last: "last",
                            prev:"prev",
                            next:"next",
                            layout: ['count', 'prev', 'page', 'next', 'limit'],
                            jump: function (obj, first) {
                                if (!first) {
                                    currentPage = obj.curr;
                                    if (pageSize != obj.limit) {
                                        currentPage = 1;
                                    }
                                    pageSize = obj.limit;
                                    getlist(starttime, endtime, u_question, catname, b_question, answer, userId, source, statistics, currentPage, pageSize);
                                }
                            }
                        })
                    } else if (language == "sc") {
                        page.render({
                            elem: 'layui',
                            count: result.count,
                            curr: currentPage,
                            limit: pageSize,
                            groups: 5,
                            first: "首页",
                            last: "尾页",
                            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                            jump: function (obj, first) {
                                if (!first) {
                                    currentPage = obj.curr;
                                    if (pageSize != obj.limit) {
                                        currentPage = 1;
                                    }
                                    pageSize = obj.limit;
                                    getlist(starttime, endtime, u_question, catname, b_question, answer, userId, source, statistics, currentPage, pageSize);
                                }
                            }
                        })
                    } else {
                        page.render({
                            elem: 'layui',
                            count: result.count,
                            curr: currentPage,
                            limit: pageSize,
                            groups: 5,
                            first: "首頁",
                            last: "尾頁",
                            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
                            jump: function (obj, first) {
                                if (!first) {
                                    currentPage = obj.curr;
                                    if (pageSize != obj.limit) {
                                        currentPage = 1;
                                    }
                                    pageSize = obj.limit;
                                    getlist(starttime, endtime, u_question, catname, b_question, answer, userId, source, statistics, currentPage, pageSize);
                                }
                            }
                        })
                    }


                })
            }, 800);


        }
    })
}

function table(result) {
    layui.use(['table', 'laypage'], function () {
        var table = layui.table;
        var question;
        //展示已知数据
        if (language == "en") {
            table.render({
                elem: '#test'
                , cellMinWidth: 80
                , height: 'full-200'
                , cols: [[ //标题栏type:'numbers'
                    {field: 'number', title: 'Number', width: 30, align: 'center'}
                    , {field: 'sessionId', title: 'sessionId', /*width: w1,*/}
                    , {field: 'userId', title: 'User id', /*width: w1,*/}
                    , {field: 'trigger_time', title: 'Time Start'/*, width: w3*/, sort: true}
                    , {field: 'u_question', title: 'Utterance'/*, width: w3*/}
                    , {field: 'b_question', title: 'Standard Intent'/*, width: w2*/}
                    , {field: 'answer', title: 'Answer'/*, width: w2*/}
                    , {field: 'source', title: 'Source'/*, width: w3*/}
                    , {field: 'catname', title: 'Intent Category'/*, width: w3*/}
                    // ,{field: 'source', title: '來源'/*, width: w3*/}
                    , {field: 'trgger_url', title: 'Click URL'/*, width: w2*/}
                    , {field: 'statistics', title: 'Click'/*, width: w1*/}

                ]]
                , limit: result.data.length//要传向后台的每页显示条数
                //  ,page:true//(自带的这个要注掉)
                /// ,page:
                , data: result.data
                , done: function (res, currentPage, pageSize) {
                    tdTitle();
                    $("[data-field='source']").children().each(function () {
                        if ($(this).text() == '1') {
                            $(this).text("Popular Forms")
                        } else {
                            $(this).text("User Input")
                        }
                    }), $("[data-field='trgger_url']").children().each(function () {
                        if ($(this).text() == 'null') {
                            $(this).text("")
                        }
                    }), $("[data-field='statistics']").children().each(function () {
                        if ($(this).text() == '0') {
                            $(this).text("NO click")
                        } else if ($(this).text() == '1') {
                            $(this).text("First click")
                        } else {
                            $(this).text("Multiple clicks")
                        }
                    }), $("[data-field='u_question']").children().each(function () {
                        if ($(this).text().indexOf("id=") != -1) {
                            $("[data-field='b_question']").children().each(function () {
                                question = $(this).text();
                            })
                            $(this).text(question);
                        }
                    })
                }
            });
        } else if (language == "sc") {
            table.render({
                elem: '#test'
                , cellMinWidth: 80
                , height: 'full-200'
                , cols: [[ //标题栏type:'numbers'
                    {field: 'number', title: '序号', width: 30, align: 'center'}
                    , {field: 'sessionId', title: '会话id', /*width: w1,*/}
                    , {field: 'userId', title: '用户id', /*width: w1,*/}
                    , {field: 'trigger_time', title: '访问时间'/*, width: w3*/, sort: true}
                    , {field: 'u_question', title: '用户问题'/*, width: w3*/}
                    , {field: 'b_question', title: '标准问题'/*, width: w2*/}
                    , {field: 'answer', title: '标准答案'/*, width: w2*/}
                    , {field: 'source', title: '来源'/*, width: w3*/}
                    , {field: 'catname', title: '知识分类'/*, width: w3*/}
                    // ,{field: 'source', title: '來源'/*, width: w3*/}
                    , {field: 'trgger_url', title: '点击URL'/*, width: w2*/}
                    , {field: 'statistics', title: '点击'/*, width: w1*/}

                ]]
                , limit: result.data.length//要传向后台的每页显示条数
                //  ,page:true//(自带的这个要注掉)
                /// ,page:
                , data: result.data
                , done: function (res, currentPage, pageSize) {
                    tdTitle();
                    $("[data-field='source']").children().each(function () {
                        if ($(this).text() == '1') {
                            $(this).text("热门表格")
                        } else {
                            $(this).text("用户输入")
                        }
                    }), $("[data-field='trgger_url']").children().each(function () {
                        if ($(this).text() == 'null') {
                            $(this).text("")
                        }
                    }), $("[data-field='statistics']").children().each(function () {
                        if ($(this).text() == '0') {
                            $(this).text("标准访问")
                        } else if ($(this).text() == '1') {
                            $(this).text("第一点击")
                        } else {
                            $(this).text("多次点击")
                        }
                    }), $("[data-field='u_question']").children().each(function () {
                        if ($(this).text().indexOf("id=") != -1) {
                            $("[data-field='b_question']").children().each(function () {
                                question = $(this).text();
                            })
                            $(this).text(question);
                        }
                    })
                }
            });
        } else {
            table.render({
                elem: '#test'
                , cellMinWidth: 80
                , height: 'full-200'
                , cols: [[ //标题栏type:'numbers'
                    {field: 'number', title: '序號', width: 30, align: 'center'}
                    , {field: 'sessionId', title: '會話id', /*width: w1,*/}
                    , {field: 'userId', title: '用戶id', /*width: w1,*/}
                    , {field: 'trigger_time', title: '訪問時間'/*, width: w3*/, sort: true}
                    , {field: 'u_question', title: '用戶問題'/*, width: w3*/}
                    , {field: 'b_question', title: '標準問題'/*, width: w2*/}
                    , {field: 'answer', title: '標準答案'/*, width: w2*/}
                    , {field: 'source', title: '來源'/*, width: w3*/}
                    , {field: 'catname', title: '知識分類'/*, width: w3*/}
                    // ,{field: 'source', title: '來源'/*, width: w3*/}
                    , {field: 'trgger_url', title: '點擊URL'/*, width: w2*/}
                    , {field: 'statistics', title: '點擊'/*, width: w1*/}

                ]]
                , limit: result.data.length//要传向后台的每页显示条数
                //  ,page:true//(自带的这个要注掉)
                /// ,page:
                , data: result.data
                , done: function (res, currentPage, pageSize) {
                    tdTitle();
                    $("[data-field='source']").children().each(function () {
                        if ($(this).text() == '1') {
                            $(this).text("熱門表格")
                        } else {
                            $(this).text("用戶輸入")
                        }
                    }), $("[data-field='trgger_url']").children().each(function () {
                        if ($(this).text() == 'null') {
                            $(this).text("")
                        }
                    }), $("[data-field='statistics']").children().each(function () {
                        if ($(this).text() == '0') {
                            $(this).text("標準訪問")
                        } else if ($(this).text() == '1') {
                            $(this).text("第一次點擊")
                        } else {
                            $(this).text("多次點擊")
                        }
                    }), $("[data-field='u_question']").children().each(function () {
                        if ($(this).text().indexOf("id=") != -1) {
                            $("[data-field='b_question']").children().each(function () {
                                question = $(this).text();
                            })
                            $(this).text(question);
                        }
                    })
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




