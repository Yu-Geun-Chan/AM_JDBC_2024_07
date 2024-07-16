package org.koreait.controller;

import org.koreait.articleManager.Container;
import org.koreait.dto.Article;
import org.koreait.service.ArticleService;
import org.koreait.util.DateUtil;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ArticleController extends Controller {
    private Connection conn;
    private ArticleService articleService;

    public ArticleController(Connection conn) {
        this.conn = conn;
        this.articleService = new ArticleService(conn);
    }

    public void doWrite() {
        if (loginedMember == null) {
            System.out.println("로그인 후 이용해주세요.");
            return;
        }
        System.out.println("== 게시글 작성 ==");
        System.out.print("제목 : ");
        String title = Container.getScanner().nextLine();
        System.out.print("내용 : ");
        String body = Container.getScanner().nextLine();

        int memberId = loginedMember.getId();

        int id = articleService.doWrite(memberId, title, body);

        System.out.printf("%d번 게시글이 작성되었습니다.\n", id);
    }

    public void showList(String cmd) {
        System.out.println("== 게시글 목록 ==");

        String[] cmdBits = cmd.split(" ");
        
        if (articleService.getSize() == 0) {
            System.out.println("작성된 게시글이 없습니다.");
            return;
        }

        String searchWord = cmd.substring("article list".length()).trim();

        List<Article> articles = articleService.getForPrintArticles(searchWord);

        System.out.println("  번호  /     작성일     /   작성자   /    제목     /     내용    ");
        System.out.println("=".repeat(70));
        for (Article article : articles) {
            if (DateUtil.getNow().split(" ")[0].equals(article.getRegDate().split(" ")[0])) {
                System.out.printf("   %d   /    %s    /    %s    /    %s    /    %s     \n", article.getId(), article.getRegDate().split(" ")[1], article.getName(), article.getTitle(), article.getBody());
            } else
                System.out.printf("   %d   /    %s    /    %s    /    %s    /    %s     \n", article.getId(), article.getRegDate().split(" ")[0], article.getName(), article.getTitle(), article.getBody());
        }
    }

    public void showDetail(String cmd) {
        System.out.println("== 게시글 상세보기 ==");

        String[] cmdBits = cmd.split(" ");

        int id = 0;

        try {
            id = Integer.parseInt(cmdBits[2]);
        } catch (Exception e) {
            System.out.println("숫자를 입력하세요");
            return;
        }
        Article article = articleService.getArticleById(id);

        if (article == null) {
            System.out.printf("%d번 게시글은 없습니다.\n", id);
            return;
        }

        System.out.printf("번호 : %d\n", article.getId());
        System.out.printf("작성일 : %s\n", article.getRegDate());
        System.out.printf("수정일 : %s\n", article.getUpdateDate());
        System.out.printf("작성자 : %s\n", article.getName());
        System.out.printf("제목 : %s\n", article.getTitle());
        System.out.printf("내용 : %s\n", article.getBody());
    }

    public void doModify(String cmd) {
        if (loginedMember == null) {
            System.out.println("로그인 후 이용해주세요.");
            return;
        }
        System.out.println("== 게시글 수정 ==");

        String[] cmdBits = cmd.split(" ");

        int id = 0;

        try {
            id = Integer.parseInt(cmdBits[2]);
        } catch (Exception e) {
            System.out.println("숫자를 입력하세요");
            return;
        }

        Article article = articleService.getArticleById(id);

        if (article == null) {
            System.out.printf("%d번 게시글은 없습니다.\n", id);
            return;
        }

        if (article.getMemberId() != loginedMember.getId()) {
            System.out.println("해당 게시글에 대한 권한이 없습니다.");
            return;
        }

        System.out.printf("기존제목 : %s\n", article.getTitle());
        System.out.printf("기존내용 : %s\n", article.getBody());
        System.out.print("제목 : ");
        String newTitle = Container.getScanner().nextLine();
        System.out.print("내용 : ");
        String newBody = Container.getScanner().nextLine();

        articleService.doModify(id, newTitle, newBody);

        System.out.printf("%d번 게시글이 수정되었습니다.\n", id);
    }

    public void doDelete(String cmd) {
        if (loginedMember == null) {
            System.out.println("로그인 후 이용해주세요.");
            return;
        }

        String[] cmdBits = cmd.split(" ");

        int id = 0;

        try {
            id = Integer.parseInt(cmdBits[2]);
        } catch (Exception e) {
            System.out.println("숫자를 입력하세요.");
            return;
        }

        Article article = articleService.getArticleById(id);

        if (article == null) {
            System.out.printf("%d번 게시글은 없습니다.\n", id);
            return;
        }

        System.out.println("== 게시글 삭제 ==");

        articleService.doDelete(id);

        System.out.printf("%d번 게시글은 삭제되었습니다.\n", id);
    }
}