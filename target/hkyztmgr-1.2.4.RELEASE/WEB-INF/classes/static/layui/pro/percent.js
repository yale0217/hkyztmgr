//var dates= new Array();
var myChart;
var result = new Array();
var sum_repeat = new Array();
var percentNums= new Array();
var sum_trigger= new Array();
var sum_count= new Array();
var starttime;
var endtime ;
var catname;
var language;
$(function () {
    language = getLanguage();
    document.getElementById("resetButton").onclick = function(){
        var myInput = document.getElementById("strStartTime");
        myInput.defaultValue = myInput.value;
        document.forms[0].reset()
    }
    layui.use('form', function(){//声明使用layUI的form
        form = layui.form;
        form.render();//刷新所有渲染效果，也可以单独熟悉某个效果
        //监听提交
        form.on('submit(searchBtn)', function(data){
            console.log(data) //当前容器的全部表单字段，名值对形式：{name: value}
            starttime = data.field.strStartTime;
            endtime = data.field.strEndTime;
            catname = data.field.catname;

            getPercent(starttime,endtime,catname)
            return false;
        });
        form.on('submit(exportBtn)', function(data){
            console.log(data) //当前容器的全部表单字段，名值对形式：{name: value}
            starttime = data.field.strStartTime;
            endtime = data.field.strEndTime;
            catname = data.field.catname;
            location.href= "../click/exceportClickPercent?starttime="+starttime+"&endtime="+endtime+ "&catname="+catname+"&language="+language;
            return false;
        });
    });
    layui.use(['form', 'upload', 'layer'], function () {
        var form = layui.form;
        //检查项目添加到下拉框中
        $.ajax({
            url: '../click/getCategoryName',
            data:{"language":language},
            dataType: 'json',
            type: 'get',
            success: function (data) {
                console.log(data);
                $.each(data, function (index, item) {
                    if(index == "data"){
                        $.each(item,function(index ,value){
                            $('#catname').append(new Option(value.name, value.name));// 下拉菜单里添加元素
                        });
                    }
                });
                layui.form.render("select");
//重新渲染 固定写法
            }
        })

    });
    // 路径配置
    require.config({
        paths: {
            echarts: '../layui/build/dist'
        }
    });
    initSearch();
    getPercent();
});

//制作图表
function setOptionBar(percentNums,sum_repeat, sum_trigger,sum_count,dates){
    // 使用
    require(
        [
            'echarts',
            'echarts/chart/bar' ,// 使用柱状图就加载bar模块，按需加载
            'echarts/chart/line'
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            myChart = ec.init(document.getElementById('main'));
            //myChart.clear();
            if(language == "sc"){
                option = {
                    color: ['#d0e17d', '#36a9ce','#C1232B'/*,'#003366'*/],
                    tooltip : {
                        trigger: 'axis',
                        formatter: '{c}%'
                    },

                    legend: {
                        color: ['#d0e17d', '#36a9ce','#C1232B'/*,'#003366'*/],
                        data:['百分比','总访问次数','点击次数'/*,'重複點擊次數'*/]

                    },
                    toolbox: {
                        show : true,
                        feature : {
                            // mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore : {show: true}
                            // saveAsImage : {show: true}
                        }

                    },
                    calculable : true,
                    xAxis : [
                        {
                            type : 'category',
                            data : dates
                        }


                    ],
                    yAxis : [
                        {
                            type : 'value',
                            min: 0,
                            max: 5000,
                            interval: 500

                        },
                        {
                            type: 'value',
                            name: '百分比',
                            min: 0,
                            max: 100,
                            interval: 20,
                            axisLabel: {
                                formatter: '{value} %'
                            }
                        }
                    ],
                    series : [
                        {
                            name:'总访问次数',
                            type:'bar',
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'top', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'black',
                                            fontSize: 16
                                        }
                                    }
                                }
                            },
                            data:sum_count

                        },

                        {
                            name:'点击次数',
                            type:'bar',
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'top', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'black',
                                            fontSize: 16
                                        }
                                    }
                                }
                            },
                            data:sum_trigger
                        },
                        {
                            name:'百分比',
                            type:'line',
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'top', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'black',
                                            fontSize: 16
                                        },
                                        formatter: '{c}%'
                                    }
                                }

                            },
                            yAxisIndex: 1,
                            data:percentNums
                        }
                    ]
                };
            }else if(language == "en"){
                option = {
                    color: ['#d0e17d', '#36a9ce','#C1232B'/*,'#003366'*/],
                    tooltip : {
                        trigger: 'axis',
                        formatter: '{c}%'
                    },

                    legend: {
                        color: ['#d0e17d', '#36a9ce','#C1232B'/*,'#003366'*/],
                        data:['Percentage','Total visits','Cilck count'/*,'重複點擊次數'*/]

                    },
                    toolbox: {
                        show : true,
                        feature : {
                            // mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore : {show: true}
                            // saveAsImage : {show: true}
                        }

                    },
                    calculable : true,
                    xAxis : [
                        {
                            type : 'category',
                            data : dates
                        }


                    ],
                    yAxis : [
                        {
                            type : 'value',
                            min: 0,
                            max: 5000,
                            interval: 500

                        },
                        {
                            type: 'value',
                            name: 'Percentage',
                            min: 0,
                            max: 100,
                            interval: 20,
                            axisLabel: {
                                formatter: '{value} %'
                            }
                        }
                    ],
                    series : [
                        {
                            name:'Total visits',
                            type:'bar',
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'top', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'black',
                                            fontSize: 16
                                        }
                                    }
                                }
                            },
                            data:sum_count

                        },

                        {
                            name:'Cilck count',
                            type:'bar',
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'top', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'black',
                                            fontSize: 16
                                        }
                                    }
                                }
                            },
                            data:sum_trigger
                        },

                        {
                            name:'Percentage',
                            type:'line',
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'top', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'black',
                                            fontSize: 16
                                        },
                                        formatter: '{c}%'
                                    }
                                }

                            },
                            yAxisIndex: 1,
                            data:percentNums
                        }
                    ]
                };
            }else{
                option = {
                    color: ['#d0e17d', '#36a9ce','#C1232B'/*,'#003366'*/],
                    tooltip : {
                        trigger: 'axis',
                        formatter: '{c}%'
                    },

                    legend: {
                        color: ['#d0e17d', '#36a9ce','#C1232B'/*,'#003366'*/],
                        data:['百分比','總訪問次數','點擊次數'/*,'重複點擊次數'*/]

                    },
                    toolbox: {
                        show : true,
                        feature : {
                            // mark : {show: true},
                            // dataView : {show: true, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore : {show: true}
                            // saveAsImage : {show: true}
                        }

                    },
                    calculable : true,
                    xAxis : [
                        {
                            type : 'category',
                            data : dates
                        }


                    ],
                    yAxis : [
                        {
                            type : 'value',
                            min: 0,
                            max: 5000,
                            interval: 500

                        },
                        {
                            type: 'value',
                            name: '百分比',
                            min: 0,
                            max: 100,
                            interval: 20,
                            axisLabel: {
                                formatter: '{value} %'
                            }
                        }
                    ],
                    series : [
                        {
                            name:'總訪問次數',
                            type:'bar',
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'top', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'black',
                                            fontSize: 16
                                        }
                                    }
                                }
                            },
                            data:sum_count

                        },

                        {
                            name:'點擊次數',
                            type:'bar',
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'top', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'black',
                                            fontSize: 16
                                        }
                                    }
                                }
                            },
                            data:sum_trigger
                        },
                        // {
                        //     name:'重複點擊次數',
                        //     type:'bar',
                        //     itemStyle: {
                        //         normal: {
                        //             label: {
                        //                 show: true, //开启显示
                        //                 position: 'top', //在上方显示
                        //                 textStyle: { //数值样式
                        //                     color: 'black',
                        //                     fontSize: 16
                        //                 }
                        //             }
                        //         }
                        //     },
                        //     data:sum_repeat
                        //
                        // },
                        {
                            name:'百分比',
                            type:'line',
                            itemStyle: {
                                normal: {
                                    label: {
                                        show: true, //开启显示
                                        position: 'top', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'black',
                                            fontSize: 16
                                        },
                                        formatter: '{c}%'
                                    }
                                }

                            },
                            yAxisIndex: 1,
                            data:percentNums
                        }
                    ]
                };
            }


            // 为echarts对象加载数据
            myChart.setOption(option,true);

        }
    );

}
//获取查询结果
function getPercent(starttime,endtime,catname){
    var main = $('#main');
    $.ajax({
        type : "get",
        async : false,
        url : "../click/getPercentList",
        data : {
            "starttime":starttime,
            "endtime":endtime,
            "language":language,
            "catname":catname
        },
        dataType : "json",
        traditional: true,//这里设为true就可以了
        success : function(result) {
            console.log("rest::::"+result);
            percentNums=[];
            sum_trigger=[];
            sum_count=[];
            sum_repeat = []
            var dates=[];
            if ( result.code == 0 && result.data.length > 0) {
                for(var i = 0; i < result.data.length;i++ ){
                    dates.push(result.data[i].trigger_data);
                    if(result.data[i].percent != undefined){
                        if(result.data[i].percent.indexOf("%") == -1){
                            percentNums.push( result.data[i].percent);
                        }else{
                            percentNums.push( result.data[i].percent.replace(new RegExp("%","g"), ''));
                        }
                    }else{
                        percentNums.push(0);
                    }


                    sum_trigger.push(result.data[i].sum_trigger);
                    sum_count.push(result.data[i].sum_count);

                }
            }
            setOptionBar(percentNums,sum_repeat, sum_trigger,sum_count,dates);
        }
    });
    return result;
}
//加载搜索项
// function initSearch(){
//     layui.use(['laydate'], function(){
//         var laydate = layui.laydate;
//         laydate.render({
//             elem: '#strStartTime',
//             value:getCurrentMonthFirst(),
//             theme: '#1e9fff',
//            // type: 'datetime'
//             // max : getCurrentMonthFirst()
//         });
//         laydate.render({
//             elem: '#strEndTime',
//             value:"",
//             theme: '#1e9fff',
//             //type: 'datetime'
//             //  max : getNowFormatDate()
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