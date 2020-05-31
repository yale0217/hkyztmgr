var result = new Array();
var form;

var language;
$(function () {
    language = getLanguage();
    getlist();
});

function getlist() {
    $.ajax({
        type : "get",
        async : false,
        url : "../crawler/list",
        data : {
            "language":language
        },
        dataType : "json",
        //  traditional: true,//这里设为true就可以了
        success : function(result) {
            table(result);


        }
    })
}
function table(result){
      layui.use(['table','laypage'], function(){
          var table = layui.table;

          //展示已知数据
          if(language == "en"){
              table.render({
                  elem: '#test'
                  ,cellMinWidth: 80
                  // ,height: 'full-30'
                  ,cols: [[ //标题栏type:'numbers'
                      {field: 'number', title: 'NO.', width: 30,align: 'center', type:'numbers'}
                      ,{field: 'excelName', title: 'Data', /*width: w1,*/}
                      ,{field: 'URL', title: 'Download URL',event: 'classStatus'}


                  ]]
                  ,limit:result.data.length//要传向后台的每页显示条数
                  ,page:true//(自带的这个要注掉)
                  /// ,page:
                  ,data: result.data
                  ,done:function(res,currentPage,pageSize){
                      tdTitle();
                      $("[data-field='excelName']").children().each(function(){
                          if($(this).text()){
                              var dateString = $(this).text().split(".")[0];
                              var pattern = /(\d{4})(\d{2})(\d{2})/;
                              var formatedDate = dateString.replace(pattern, '$1-$2-$3');
                              $(this).text(formatedDate)
                          }
                      }), $("[data-field='URL']").children().each(function(){
                          var rest = $(this).text()
                          $(this).text(rest)
                      })

                  }

              });
              table.on('tool(table_test)', function(obj) { //test为你table的lay-filter对应的值
                  if (obj.event === 'classStatus') {
                      window.open(obj.data.URL);

                  }
              });
          }else if(language == "sc"){
              table.render({
                  elem: '#test'
                  ,cellMinWidth: 80
                  // ,height: 'full-30'
                  ,cols: [[ //标题栏type:'numbers'
                      {field: 'number', title: '序号', width: 30,align: 'center', type:'numbers'}
                      ,{field: 'excelName', title: '日期', /*width: w1,*/}
                      ,{field: 'URL', title: '链接',event: 'classStatus'}


                  ]]
                  ,limit:result.data.length//要传向后台的每页显示条数
                  ,page:true//(自带的这个要注掉)
                  /// ,page:
                  ,data: result.data
                  ,done:function(res,currentPage,pageSize){
                      tdTitle();
                      $("[data-field='excelName']").children().each(function(){
                          if($(this).text()){
                              var dateString = $(this).text().split(".")[0];
                              var pattern = /(\d{4})(\d{2})(\d{2})/;
                              var formatedDate = dateString.replace(pattern, '$1-$2-$3');
                              $(this).text(formatedDate)
                          }
                      }), $("[data-field='URL']").children().each(function(){
                          var rest = $(this).text()
                          $(this).text(rest)
                      })

                  }

              });
              table.on('tool(table_test)', function(obj) { //test为你table的lay-filter对应的值
                  if (obj.event === 'classStatus') {
                      window.open(obj.data.URL);

                  }
              });
          }else{
              table.render({
                  elem: '#test'
                  ,cellMinWidth: 80
                  // ,height: 'full-30'
                  ,cols: [[ //标题栏type:'numbers'
                      {field: 'number', title: '序號', width: 30,align: 'center', type:'numbers'}
                      ,{field: 'excelName', title: '日期', /*width: w1,*/}
                      ,{field: 'URL', title: '鏈接',event: 'classStatus'}


                  ]]
                  ,limit:result.data.length//要传向后台的每页显示条数
                  ,page:true//(自带的这个要注掉)
                  /// ,page:
                  ,data: result.data
                  ,done:function(res,currentPage,pageSize){
                      tdTitle();
                      $("[data-field='excelName']").children().each(function(){
                          if($(this).text()){
                              var dateString = $(this).text().split(".")[0];
                              var pattern = /(\d{4})(\d{2})(\d{2})/;
                              var formatedDate = dateString.replace(pattern, '$1-$2-$3');
                              $(this).text(formatedDate)
                          }
                      }), $("[data-field='URL']").children().each(function(){
                          var rest = $(this).text()
                          $(this).text(rest)
                      })

                  }

              });
              table.on('tool(table_test)', function(obj) { //test为你table的lay-filter对应的值
                  if (obj.event === 'classStatus') {
                      window.open(obj.data.URL);

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

