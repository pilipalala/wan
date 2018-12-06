package com.wyj.wan.model;

import java.util.List;

/**
 * @author wangyujie
 * @date 2018/12/3.16:40
 * @describe 添加描述
 */
public class UserEntity {

    /**
     * chapterTops : []
     * collectIds : [7448,7460]
     * email :
     * icon :
     * id : 11995
     * password :
     * token :
     * type : 0
     * username : pilipalaKing
     */

    private String email;
    private String icon;
    private int id;
    private String password;
    private String token;
    private int type;
    private String username;
    private List<?> chapterTops;
    private List<Integer> collectIds;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<?> getChapterTops() {
        return chapterTops;
    }

    public void setChapterTops(List<?> chapterTops) {
        this.chapterTops = chapterTops;
    }

    public List<Integer> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<Integer> collectIds) {
        this.collectIds = collectIds;
    }
}
