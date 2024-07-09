package org.koreait;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("== 프로그램 시작 ==");
        Scanner sc = new Scanner(System.in);

        int lastArticleId = 0;

        while (true) {
            System.out.print("명령어 > ");
            String cmd = sc.nextLine().trim();

            if (cmd.equals("exit")) {
                break;
            }

            if (cmd.equals("article write")) {
                System.out.println("== 게시글 작성 ==");

                int id = lastArticleId + 1;
                System.out.print("제목 : ");
                String title = sc.nextLine();
                System.out.print("내용 : ");
                String body = sc.nextLine();

                Article article = new Article(id, title, body);

                lastArticleId++;

                Connection conn = null;
                PreparedStatement pstmt = null; // Insert Quary를 하려면 필요하다.
                ResultSet rs = null;
                int lastRsId = 0;

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://127.0.0.1:3306/JDBC?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
                    conn = DriverManager.getConnection(url, "root", "");


                    String sql = "INSERT INTO article ";
                    sql += "SET regDate = NOW(),";
                    sql += "updateDate = NOW(),";
                    sql += "title = '" + title + "',";
                    sql += "`body` = '" + body + "';";


                    pstmt = conn.prepareStatement(sql);
                    int affectedRows = pstmt.executeUpdate(); // 몇 열이 적용됐는지
                    System.out.println("affected rows: " + affectedRows);

                    sql = "SELECT * FROM article";
                    rs = pstmt.executeQuery(sql);
                    while (rs.next()) {
                        lastRsId = rs.getInt("id");
                    }

                    System.out.printf("%d번 게시글이 작성되었습니다.\n", lastRsId);

                } catch (ClassNotFoundException e) {
                    System.out.println("드라이버 로딩 실패" + e);
                } catch (SQLException e) {
                    System.out.println("에러 : " + e);
                } finally {
                    try {
                        if (conn != null && !conn.isClosed()) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (pstmt != null && !pstmt.isClosed()) {
                            pstmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            } else if (cmd.equals("article list")) {
                System.out.println("== 게시글 목록 ==");
                Connection conn = null;
                PreparedStatement pstmt = null; // Insert Quary를 하려면 필요하다.
                ResultSet rs = null; // Select Quary를 하려면 필요하다.

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://127.0.0.1:3306/JDBC?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
                    conn = DriverManager.getConnection(url, "root", "");

                    String sql = "SELECT * FROM article ORDER BY id DESC;";
                    pstmt = conn.prepareStatement(sql);
                    rs = pstmt.executeQuery();

                    if (!rs.isBeforeFirst()) {
                        System.out.println("작성된 게시글이 없습니다.");
                        continue;
                    }

                    System.out.println("  번호  /     작성일     /       제목       /      내용    ");
                    System.out.println("=".repeat(60));
                    while (rs.next()) {
                        if (rs.getString(2).split(" ")[0].equals(Util.getNow().split(" ")[0])) {
                            System.out.printf("   %d   /   %s   /    %s    /   %s      \n", (rs.getInt(1)), rs.getString(2).split(" ")[1], rs.getString(4), rs.getString(5));
                        } else
                            System.out.printf("   %d   /   %s   /    %s    /   %s      \n", (rs.getInt(1)), rs.getString(2).split(" ")[0], rs.getString(4), rs.getString(5));
                    }

                } catch (ClassNotFoundException e) {
                    System.out.println("드라이버 로딩 실패" + e);
                } catch (SQLException e) {
                    System.out.println("에러 : " + e);
                } finally {
                    try {
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (pstmt != null && !pstmt.isClosed()) {
                            pstmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (conn != null && !conn.isClosed()) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    // 실행했던거 역순으로 꺼주는게 좋다.
                }
            } else if (cmd.startsWith("article modify")) {
                System.out.println("== 게시글 수정 ==");

                Connection conn = null;
                PreparedStatement pstmt = null;// Insert Quary를 하려면 필요하다.
                ResultSet rs = null;
                int foundArticleId = 0;

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://127.0.0.1:3306/JDBC?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
                    conn = DriverManager.getConnection(url, "root", "");

                    String[] cmdBits = cmd.split(" ");

                    int id;
                    try {
                        id = Integer.parseInt(cmdBits[2]);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("숫자를 입력하세요.");
                        continue;
                    }

                    String sql = "SELECT * FROM article WHERE id = " + id + ";";
                    pstmt = conn.prepareStatement(sql);
                    rs = pstmt.executeQuery(sql);

                    while (rs.next()) {
                        if (rs.getInt("id") == id) {
                            foundArticleId = id;
                        }
                    }
                    if (foundArticleId == 0) {
                        System.out.printf("%d번 게시글은 없습니다.\n", id);
                        continue;
                    }

                    System.out.print("새 제목 : ");
                    String newTitle = sc.nextLine();
                    System.out.print("새 내용 : ");
                    String newBody = sc.nextLine();

                    sql = "UPDATE article ";
                    sql += "SET updateDate = NOW()";
                    if (newTitle.length() > 0) {
                        sql += ", title = '" + newTitle + "'";
                    }
                    if (newBody.length() > 0) {
                        sql += ", body = '" + newBody + "'";
                    }
                    sql += "WHERE id = " + id + ";";

                    pstmt = conn.prepareStatement(sql);
                    pstmt.executeUpdate();

                    System.out.printf("%d번 글이 수정되었습니다.\n", id);

                } catch (ClassNotFoundException e) {
                    System.out.println("드라이버 로딩 실패" + e);
                } catch (SQLException e) {
                    System.out.println("에러 : " + e);
                } finally {
                    try {
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (pstmt != null && !pstmt.isClosed()) {
                            pstmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (conn != null && !conn.isClosed()) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else if (cmd.startsWith("article delete")) {
                Connection conn = null;
                PreparedStatement pstmt = null;// Insert Quary를 하려면 필요하다.
                ResultSet rs = null;
                int foundArticleId = 0;

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    String url = "jdbc:mariadb://127.0.0.1:3306/JDBC?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
                    conn = DriverManager.getConnection(url, "root", "");

                    String[] cmdBits = cmd.split(" ");

                    int id;
                    try {
                        id = Integer.parseInt(cmdBits[2]);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.out.println("숫자를 입력하세요.");
                        continue;
                    }

                    String sql = "SELECT * FROM article WHERE id = " + id + ";";
                    pstmt = conn.prepareStatement(sql);
                    rs = pstmt.executeQuery(sql);

                    while (rs.next()) {
                        if (rs.getInt("id") == id) {
                            foundArticleId = id;
                        }
                    }
                    if (foundArticleId == 0) {
                        System.out.printf("%d번 게시글은 없습니다.\n", id);
                        continue;
                    }

                    sql = "DELETE FROM article WHERE id = " + id + ";";

                    pstmt = conn.prepareStatement(sql);

                    int affectedRows = pstmt.executeUpdate(); // 몇 열이 적용됐는지

                    System.out.println("affected rows: " + affectedRows);

                } catch (ClassNotFoundException e) {
                    System.out.println("드라이버 로딩 실패" + e);
                } catch (SQLException e) {
                    System.out.println("에러 : " + e);
                } finally {
                    try {
                        if (rs != null && !rs.isClosed()) {
                            rs.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (pstmt != null && !pstmt.isClosed()) {
                            pstmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
        System.out.println("==프로그램 종료==");
    }
}