package org.koreait.service;


import org.koreait.dao.ArticleDao;
import org.koreait.dto.Article;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleService {
    private ArticleDao articleDao;

    public ArticleService(Connection conn) {
        this.articleDao = new ArticleDao(conn);
    }

    public int doWrite(String title, String body) {
        return articleDao.doWrite(title, body);
    }

    public Map<String, Object> getArticleById(int id) {
        return articleDao.getArticleById(id);
    }

    public void doModify(int id, String newTitle, String newBody) {
        articleDao.doModify(id, newTitle, newBody);
    }

    public int doDelete(int id) {
        return articleDao.doDelete(id);
    }

    public List<Article> getArticles() {
        return articleDao.getArticles();
    }

    public List<Article> getForPrintArticles(String searchWord) {
        List<Article> forPrintArticles = new ArrayList<>();

        if (!searchWord.isEmpty() || searchWord != null) {
            System.out.printf("검색어 : %s\n",searchWord);

            for (Article article : articleDao.getArticles()) {
                if (article.getTitle().contains(searchWord)) {
                    forPrintArticles.add(article);
                }
            }
            if (forPrintArticles.isEmpty()) {
                System.out.println("해당 게시글이 없습니다.");
                return forPrintArticles;
            }
        }
        return forPrintArticles;
    }

    public int getSize() {
        return articleDao.getSize();
    }
}