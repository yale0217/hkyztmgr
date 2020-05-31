package com.xiaoi.south.manager.entity;

import lombok.Data;

import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Data
public class Crawler {
    private String id;
    private String classification;
    private String object;
    private String formname;
    private String formid;
    private String formurl;
    private String reptiletime;
    private String reptiledate;
//    @Override
//    public boolean equals(Object obj) {
//        boolean rest = false ;
//        if(classification.equals(((Crawler)obj).getClassification())) {
//             rest = true;
//        }
//        if(object.equals(((Crawler)obj).getObject())) {
//            rest = true;
//        }
//        if(formname.equals(((Crawler)obj).getFormname())) {
//            rest = true;
//        }
//        if(isNotBlank(formid)  &&  formid.equals(((Crawler)obj).getFormid())) {
//            rest = true;
//        }
//        if(isNotBlank(formurl) && formurl.equals(((Crawler)obj).getFormurl())) {
//            rest = true;
//        }
//        return rest;
//    }
    @Override
public boolean equals(Object obj) {
    if (obj instanceof Crawler) {
        Crawler u = (Crawler) obj;
        return this.classification==(u.classification) && this.object==(u.object) && this.formname==(u.formname) && this.formid==(u.formid)
                && this.formurl==(u.formurl);
    }
    return super.equals(obj);
}

}
