package org.koreait.dao;

import org.koreait.dto.Member;
import org.koreait.util.DBUtil;
import org.koreait.util.SecSql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemberDao {
    private Connection conn;

    public MemberDao(Connection conn) {
        this.conn = conn;
    }

    public boolean isLoginIdDup(Connection conn, String loginId) {
        SecSql sql = new SecSql();
        sql.append("SELECT COUNT(*) > 0 FROM `member` WHERE loginId = '" + loginId + "';");

        return DBUtil.selectRowBooleanValue(conn, sql);
    }

    public int doJoin(String loginId, String loginPw, String name) {
        SecSql sql = new SecSql();

        sql.append("INSERT INTO `member`");
        sql.append("SET regDate = NOW(),");
        sql.append("loginId = '" + loginId + "',");
        sql.append("loginPw = '" + loginPw + "',");
        sql.append("name = '" + name + "';");

        return DBUtil.insert(conn, sql);
    }

    public Member getMemberLoginId(String enterLoginId) {
        SecSql sql = new SecSql();

        sql.append("SELECT * from `member`");
        sql.append("WHERE loginId = '" + enterLoginId + "';");

        Map<String, Object> memberMap = DBUtil.selectRow(conn, sql);

        if (memberMap == null) {
            return null;
        }

        return new Member(memberMap);
    }

    public void loginFailCount(String enterLoginId) {
        SecSql sql = new SecSql();

        sql.append("update `member`");
        sql.append("set loginFailCount = loginFailCount + 1");
        sql.append("where loginId = '" + enterLoginId + "';");

        DBUtil.update(conn, sql);
    }

    public int loginLimit() {
        SecSql sql = new SecSql();

        sql.append("update `member`");
        sql.append("set loginLimit = 1");
        sql.append("WHERE loginFailCount = 3;");

        return DBUtil.update(conn, sql);
    }

    public void loginFailCountReset() {
        SecSql sql = new SecSql();

        sql.append("update `member`");
        sql.append("set loginFailCount = 0,");
        sql.append("loginLimit = 0");
        sql.append("WHERE loginFailCount = 3;");

        DBUtil.update(conn, sql);
    }
}
