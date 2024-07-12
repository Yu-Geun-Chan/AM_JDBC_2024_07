package org.koreait.dao;

import org.koreait.util.DBUtil;
import org.koreait.util.SecSql;

import java.sql.Connection;

public class MemberDao {


    public boolean isLoginIdDup(Connection conn, String loginId) {
        SecSql sql = new SecSql();
        sql.append("SELECT COUNT(*) > 0 FROM `member` WHERE loginId = '" + loginId + "';");

        return DBUtil.selectRowBooleanValue(conn, sql);
    }
}
