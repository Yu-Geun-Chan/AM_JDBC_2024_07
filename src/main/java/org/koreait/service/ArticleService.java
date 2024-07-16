package org.koreait.service;


import org.koreait.dao.ArticleDao;
import org.koreait.dto.Article;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleService {
    private ArticleDao articleDao;

    public ArticleService(Connection conn) {
        this.articleDao = new ArticleDao(conn);
    }

    public int doWrite(int memberId, String title, String body) {
        return articleDao.doWrite(memberId, title, body);
    }

    public Article getArticleById(int id) {
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

        List<Article> articles = new ArrayList<>();

        if (!searchWord.isEmpty() || searchWord != null) {
            System.out.printf("검색어 : %s\n",searchWord);

            for (Article article : articleDao.getArticles()) {
                if (article.getTitle().contains(searchWord)) {
                    articles.add(article);
                }
            }
            if (articles.isEmpty()) {
                System.out.println("해당 게시글이 없습니다.");
                return articles;
            }
        }
        return articles;
    }

    public int getSize() {
        return articleDao.getSize();
    }
}