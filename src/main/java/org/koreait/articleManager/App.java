package org.koreait.articleManager;

import org.koreait.controller.ArticleController;
import org.koreait.controller.MemberController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    public void run() {
        System.out.println("== 프로그램 시작 ==");

        while (true) {
            System.out.print("명령어 > ");
            String cmd = Container.getScanner().nextLine().trim();

            Connection conn = null;

            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            String url = "jdbc:mariadb://127.0.0.1:3306/AM_JDBC_2024_07?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";

            try {
                conn = DriverManager.getConnection(url, "root", "");

                int actionResult = action(conn, cmd);

                if (actionResult == -1) {
                    System.out.println("== 프로그램 종료 ==");
                    Container.close();
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

    private static int action(Connection conn, String cmd) {
        if (cmd.equals("exit")) {
            System.out.println("== 프로그램 종료 ==");
            return -1;
        }

        ArticleController articleController = new ArticleController(conn);
        MemberController memberController = new MemberController(conn);

        if (cmd.equals("article write")) {
            articleController.doWrite();
        } else if (cmd.startsWith("article list")) {
            articleController.showList(cmd);
        } else if (cmd.startsWith("article modify")) {
            articleController.doModify(cmd);
        } else if (cmd.startsWith("article detail")) {
            articleController.showDetail(cmd);
        } else if (cmd.startsWith("article delete")) {
            articleController.doDelete(cmd);
        } else if (cmd.equals("member join")) {
            memberController.doJoin();
        } else if (cmd.equals("member login")) {
            memberController.doLogin();
        } else if (cmd.equals("member logout")) {
            memberController.doLogout();
        } else {
            System.out.println("올바르지 않은 명령어입니다.");
        }
        return 0;
    }
}
