package org.koreait;

import java.sql.*;

public class JDBCSelectTest {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null; // Insert Quary를 하려면 필요하다.

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            String url = "jdbc:mariadb://127.0.0.1:3306/JDBC?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
            conn = DriverManager.getConnection(url, "root", "");
            System.out.println("연결 성공!");

            String sql = "SELECT id, regDate, title, body FROM article ";
            pstmt = conn.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();

            System.out.println("  번호  /    작성일     /    제목    /    내용    ");
            System.out.println("=".repeat(50));
            while (rs.next()) {
                if (rs.getString(2).split(" ")[0].equals(Util.getNow().split(" ")[0])) {
                    System.out.printf("   %d   /   %s   /   %s   /   %s      \n",(rs.getInt(1)), rs.getString(2).split(" ")[1], rs.getString(3), rs.getString(4));
                } else  System.out.printf("   %d   /   %s   /   %s   /   %s      \n",(rs.getInt(1)), rs.getString(2).split(" ")[0], rs.getString(3), rs.getString(4));
            }

            rs.close();
            pstmt.close();
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
    }
}
