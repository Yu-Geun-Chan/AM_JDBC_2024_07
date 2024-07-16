package org.koreait.dto;

import java.util.Map;

public class Article {
    private int id;
    private String regDate;
    private String updateDate;
    private int memberId;
    private String title;
    private String body;

    private String name;

    public Article(int id, String title, String body) {

    }

    public Article(int id, String regDate, String updateDate, int memberId, String title, String body) {
        this.id = id;
        this.regDate = regDate;
        this.updateDate = updateDate;
        this.memberId = memberId;
        this.title = title;
        this.body = body;
    }

    public Article(Map<String, Object> articleMap) {
        this.id = (int) articleMap.get("id");
        this.regDate = (String) articleMap.get("regDate");
        this.updateDate = (String) articleMap.get("updateDate");
        this.memberId = (int) articleMap.get("memberId");
        this.title = (String) articleMap.get("title");
        this.body = (String) articleMap.get("body");
        this.name = (String) articleMap.get("name");

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
