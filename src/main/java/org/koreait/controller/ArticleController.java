package org.koreait.controller;

import org.koreait.dto.Article;
import org.koreait.util.DBUtil;
import org.koreait.util.SecSql;
import org.koreait.util.Util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ArticleController extends Controller {
    private Scanner sc;
    private Connection conn;

    public ArticleController(Scanner sc, Connection conn) {
        this.sc = sc;
        this.conn = conn;
    }

    public void doWrite() {
        System.out.println("== 게시글 작성 ==");

        System.out.print("제목 : ");
        String title = sc.nextLine();
        System.out.print("내용 : ");
        String body = sc.nextLine();

        SecSql sql = new SecSql();

        sql.append("INSERT INTO article");
        sql.append("SET regDate = NOW(),");
        sql.append("updateDate = NOW(),");
        sql.append("title = '" + title + "',");
        sql.append("`body` = '" + body + "';");

        int id = DBUtil.insert(conn, sql);

        System.out.printf("%d번 게시글이 작성되었습니다.\n", id);

    }

    public void showList() {
        System.out.println("== 게시글 목록 ==");

        List<Article> articles = new ArrayList<Article>();

        SecSql sql = new SecSql();
        sql.append("SELECT * FROM article ORDER BY id DESC;");

        List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

        for (Map<String, Object> articleMap : articleListMap) {
            articles.add(new Article(articleMap));
        }

        if (articles.isEmpty()) {
            System.out.println("작성된 게시글이 없습니다.");
            return;
        }

        System.out.println("  번호  /     작성일     /       제목       /      내용    ");
        System.out.println("=".repeat(60));
        for (int i = articles.size() - 1; i >= 0; i--) {
            Article article = articles.get(i);
            if (article.getRegDate().split(" ")[0].equals(Util.getNow().split(" ")[0])) {
                System.out.printf("   %d   /    %s    /       %s       /      %s     \n", article.getId(), article.getRegDate().split(" ")[1], article.getTitle(), article.getBody());
            } else
                System.out.printf("   %d   /    %s     /      %s      /      %s     \n", article.getId(), article.getRegDate().split(" ")[0], article.getTitle(), article.getBody());
        }

    }

    public void doModify(String cmd) {
        System.out.println("== 게시글 수정 ==");

        String[] cmdBits = cmd.split(" ");

        int id;

        try {
            id = Integer.parseInt(cmdBits[2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("숫자를 입력하세요.");
            return;
        }

        SecSql sql = new SecSql();

        sql.append("SELECT * FROM article WHERE id = " + id + ";");

        Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

        if (articleMap.isEmpty()) {
            System.out.printf("%d번 게시글은 없습니다.\n", id);
            return;
        }

        System.out.print("새 제목 : ");
        String newTitle = sc.nextLine();
        System.out.print("새 내용 : ");
        String newBody = sc.nextLine();

        SecSql sql2 = new SecSql();

        sql2.append("UPDATE article");
        sql2.append("SET updateDate = NOW()");
        if (!newTitle.isEmpty()) {
            sql2.append(", title = '" + newTitle + "'");
        }
        if (!newBody.isEmpty()) {
            sql2.append(", body = '" + newBody + "'");
        }
        sql2.append("WHERE id = " + id + ";");

        DBUtil.update(conn, sql2);

        System.out.printf("%d번 글이 수정되었습니다.\n", id);

    }

    public void showDetail(String cmd) {
        System.out.println("== 게시글 상세보기 ==");

        String[] cmdBits = cmd.split(" ");

        int id;
        try {
            id = Integer.parseInt(cmdBits[2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("숫자를 입력하세요.");
            return;
        }

        SecSql sql = new SecSql();
        sql.append("SELECT * FROM article WHERE id = " + id + ";");

        Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

        if (articleMap.isEmpty()) {
            System.out.printf("%d번 게시글은 없습니다.\n", id);
            return;
        }

        Article article = new Article(articleMap);

        System.out.printf("번호 : %d\n", article.getId());
        System.out.printf("작성날짜 : %s\n", article.getRegDate());
        System.out.printf("수정날짜 : %s\n", article.getUpdateDate());
        System.out.printf("제목 : %s\n", article.getTitle());
        System.out.printf("내용 : %s\n", article.getBody());

    }

    public void doDelete(String cmd) {
        System.out.println("== 게시글 삭제 ==");

        String[] cmdBits = cmd.split(" ");

        int id;
        try {
            id = Integer.parseInt(cmdBits[2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("숫자를 입력하세요.");
            return;
        }

        SecSql sql = new SecSql();

        int foundArticleId = 0;

        sql.append("DELETE FROM article WHERE id = " + id + ";");

        foundArticleId = DBUtil.delete(conn, sql);

        if (foundArticleId == 0) {
            System.out.printf("%d번 게시글은 없습니다.\n", id);
            return;
        }

        System.out.printf("%d번 게시글이 삭제되었습니다.\n", foundArticleId);

    }
}
