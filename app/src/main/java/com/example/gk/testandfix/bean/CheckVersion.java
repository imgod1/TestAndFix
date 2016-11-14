package com.example.gk.testandfix.bean;

/**
 * 项目名称：TestAndFix
 * 类描述：
 * 创建人：gk
 * 创建时间：2016/11/14 15:56
 * 修改人：gk
 * 修改时间：2016/11/14 15:56
 * 修改备注：
 */
public class CheckVersion {

    /**
     * havepatch : false
     * path : Hello World
     */

    private boolean havepatch;
    private String path;

    public boolean isHavepatch() {
        return havepatch;
    }

    public void setHavepatch(boolean havepatch) {
        this.havepatch = havepatch;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
