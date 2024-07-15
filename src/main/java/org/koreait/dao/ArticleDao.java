package org.koreait.dao;

import org.koreait.controller.Controller;
import org.koreait.dto.Article;
import org.koreait.util.DBUtil;
import org.koreait.util.SecSql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleDao {
    private Connection conn;

    public ArticleDao(Connection conn) {
        this.conn = conn;
    }

    public int doWrite(String title, String body) {
        SecSql sql = new SecSql();

        sql.append("INSERT INTO article");
        sql.append("SET regDate = NOW(),");
        sql.append("updateDate = NOW(),");
        sql.append("loginId = '" + Controller.loginedMember.getLoginId() + "',");
        sql.append("title = '" + title + "',");
        sql.append("`body` = '" + body + "';");

        return DBUtil.insert(conn, sql);
    }

    public Map<String, Object> getArticleById(int id) {
        SecSql sql = new SecSql();

        sql.append("SELECT * FROM article WHERE id = " + id + ";");

        return DBUtil.selectRow(conn, sql);
    }

    public void doModify(int id, String newTitle, String newBody) {
        SecSql sql = new SecSql();

        sql.append("UPDATE article");
        sql.append("SET updateDate = NOW(),");
        if (!newTitle.isEmpty()) {
            sql.append("title = '" + newTitle + "',");
        }
        if (!newBody.isEmpty()) {
            sql.append("`body` = '" + newBody + "',");
        }
        sql.append("WHERE id = " + id + ";");
    }

    public int doDelete(int id) {
        SecSql sql = new SecSql();

        sql.append("DELETE FROM article WHERE id = " + id + ";");

        return DBUtil.delete(conn, sql);
    }

    public List<Article> getArticles() {
        SecSql sql = new SecSql();
        sql.append("SELECT * FROM article ORDER BY id DESC;");

        List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

        List<Article> articles = new ArrayList<>();

        for (Map<String, Object> articleMap : articleListMap) {
            articles.add(new Article(articleMap));
        }
        return articles;
    }

    public int getSize() {
        return getArticles().size();
    }
}
