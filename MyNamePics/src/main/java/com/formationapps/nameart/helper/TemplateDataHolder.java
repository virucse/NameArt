package com.formationapps.nameart.helper;

/**
 * Created by caliber fashion on 5/8/2017.
 */

public class TemplateDataHolder {
    private String parent = "";
    private String[] childs;

    public TemplateDataHolder() {
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String[] getChilds() {
        return childs;
    }

    public void setChilds(String[] childs) {
        this.childs = childs;
    }
}
