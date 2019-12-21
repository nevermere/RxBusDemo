package com.hz.rxbus.demo.bean;

/**
 * 描述：
 *
 * @author fzJiang
 * @date 2019-12-16 16:16.
 */
public class FileBean {

    private String name;
    private String path;

    public FileBean(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "name:" + name + ",path:" + path;
    }
}