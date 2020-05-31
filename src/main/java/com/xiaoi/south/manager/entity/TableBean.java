package com.xiaoi.south.manager.entity;

import lombok.Data;

@Data
public class TableBean {
    private String bigclassification; //大分类
    private String number;//表格编码
    private String classification;//小分类
    private String tablename;//表格名称
    private String PDF_url;//pdf路径
    private String electron_url;//电子表格路径
    private String version;//版本号
}
