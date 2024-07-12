package org.koreait;

import org.koreait.util.DBUtil;
import org.koreait.util.SecSql;
import org.koreait.util.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {
    private static Member loginedMember = null;

    public void run() {
        System.out.println("== 프로그램 시작 ==");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("명령어 > ");
            String cmd = sc.nextLine().trim();

            Connection conn = null;

            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            String url = "jdbc:mariadb://127.0.0.1:3306/AM_JDBC_2024_07?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";

            try {
                conn = DriverManager.getConnection(url, "root", "");

                int actionResult = doAction(conn, sc, cmd);

                if (actionResult == -1) {
                    System.out.println("== 프로그램 종료 ==");
                    sc.close();
                    break;
                }

            } catch (SQLException e) {
                System.out.println("에러 1 : " + e);
            } finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int doAction(Connection conn, Scanner sc, String cmd) {

        if (cmd.equals("exit")) {
            System.out.println("== 프로그램 종료 ==");
            return -1;
        }

        if (cmd.equals("article write")) {
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

        } else if (cmd.equals("article list")) {
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
                return 0;
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


        } else if (cmd.startsWith("article modify")) {
            System.out.println("== 게시글 수정 ==");

            String[] cmdBits = cmd.split(" ");

            int id;

            try {
                id = Integer.parseInt(cmdBits[2]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("숫자를 입력하세요.");
                return 0;
            }

            SecSql sql = new SecSql();

            sql.append("SELECT * FROM article WHERE id = " + id + ";");

            Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

            if (articleMap.isEmpty()) {
                System.out.printf("%d번 게시글은 없습니다.\n", id);
                return 0;
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

        } else if (cmd.startsWith("article detail")) {
            System.out.println("== 게시글 상세보기 ==");

            String[] cmdBits = cmd.split(" ");

            int id;
            try {
                id = Integer.parseInt(cmdBits[2]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("숫자를 입력하세요.");
                return 0;
            }

            SecSql sql = new SecSql();
            sql.append("SELECT * FROM article WHERE id = " + id + ";");

            Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

            if (articleMap.isEmpty()) {
                System.out.printf("%d번 게시글은 없습니다.\n", id);
                return 0;
            }

            Article article = new Article(articleMap);

            System.out.printf("번호 : %d\n", article.getId());
            System.out.printf("작성날짜 : %s\n", article.getRegDate());
            System.out.printf("수정날짜 : %s\n", article.getUpdateDate());
            System.out.printf("제목 : %s\n", article.getTitle());
            System.out.printf("내용 : %s\n", article.getBody());

        } else if (cmd.startsWith("article delete")) {
            System.out.println("== 게시글 삭제 ==");

            String[] cmdBits = cmd.split(" ");

            int id;
            try {
                id = Integer.parseInt(cmdBits[2]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("숫자를 입력하세요.");
                return 0;
            }

            SecSql sql = new SecSql();

            int foundArticleId = 0;

            sql.append("DELETE FROM article WHERE id = " + id + ";");

            foundArticleId = DBUtil.delete(conn, sql);

            if (foundArticleId == 0) {
                System.out.printf("%d번 게시글은 없습니다.\n", id);
                return 0;
            }

            System.out.printf("%d번 게시글이 삭제되었습니다.\n", foundArticleId);


        } else if (cmd.startsWith("article detail")) {
            System.out.println("== 게시글 상세보기 ==");

            String[] cmdBits = cmd.split(" ");

            int id;
            try {
                id = Integer.parseInt(cmdBits[2]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("숫자를 입력하세요.");
                return 0;
            }

            SecSql sql = new SecSql();
            sql.append("SELECT * FROM article WHERE id = " + id + ";");

            Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

            if (articleMap.isEmpty()) {
                System.out.printf("%d번 게시글은 없습니다.\n", id);
                return 0;
            }

            Article article = new Article(articleMap);

            System.out.printf("번호 : %d\n", article.getId());
            System.out.printf("작성날짜 : %s\n", article.getRegDate());
            System.out.printf("수정날짜 : %s\n", article.getUpdateDate());
            System.out.printf("제목 : %s\n", article.getTitle());
            System.out.printf("내용 : %s\n", article.getBody());

        } else if (cmd.equals("member join")) {
            System.out.println("== 회원가입 ==");
            String loginId = null;
            while (true) {
                System.out.print("아이디 : ");
                loginId = sc.nextLine().trim();

                if (loginId.isEmpty() || loginId.contains(" ")) {
                    System.out.println("아이디를 입력해주세요.");
                    continue;
                }

                SecSql sql = new SecSql();
                sql.append("SELECT COUNT(*) > 0 FROM `member` WHERE loginId = '" + loginId + "';");

                boolean isLoginIdDup = DBUtil.selectRowBooleanValue(conn, sql);

                if (isLoginIdDup) {
                    System.out.printf("[%s] 아이디는(은) 이미 사용중입니다.\n", loginId);
                    continue;
                }
                break;
            }
            String loginPw = null;
            while (true) {
                System.out.print("비밀번호 : ");
                loginPw = sc.nextLine().trim();

                if (loginPw.isEmpty() || loginPw.contains(" ")) {
                    System.out.println("비밀번호를 입력해주세요.");
                    continue;
                }

                boolean loginPwCheck = true;

                while (true) {
                    System.out.print("비밀번호 확인 : ");
                    String checkLoginPw = sc.nextLine().trim();

                    if (checkLoginPw.isEmpty() || checkLoginPw.contains(" ")) {
                        System.out.println("비밀번호 확인을 입력해주세요.");
                        continue;
                    }

                    if (!loginPw.equals(checkLoginPw)) {
                        System.out.println("비밀번호와 확인이 일치하지 않습니다.");
                        loginPwCheck = false;
                    }
                    break;
                }
                if (loginPwCheck) {
                    break;
                }

            }
            String name = null;
            while (true) {
                System.out.print("이름 : ");
                name = sc.nextLine();

                if (name.isEmpty() || name.contains(" ")) {
                    System.out.println("이름을 입력해주세요.");
                    continue;
                }
                break;
            }

            SecSql sql = new SecSql();
            sql.append("INSERT INTO `member`");
            sql.append("SET regDate = NOW(),");
            sql.append("SET updateDate = NOW(),");
            sql.append("loginId = '" + loginId + "',");
            sql.append("loginPw = '" + loginPw + "',");
            sql.append("name = '" + name + "';");

            int id = DBUtil.insert(conn, sql);

            System.out.printf("[%s]님 환영합니다.\n", name);

        } else {
            System.out.println("올바르지 않은 명령어입니다.");
        }
        return 0;

    }

}



