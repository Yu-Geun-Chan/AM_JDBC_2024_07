package org.koreait.service;

import org.koreait.dao.MemberDao;
import org.koreait.dto.Member;

import java.sql.Connection;

public class MemberService {

    private MemberDao memberDao;

    public MemberService(Connection conn) {
        this.memberDao = new MemberDao(conn);
    }

    public int loginLimit() {
        return memberDao.loginLimit();
    }

    public boolean isLoginIdDup(Connection conn, String loginId) {
        return memberDao.isLoginIdDup(conn, loginId);
    }

    public int doJoin(String loginId, String loginPw, String name) {
        return memberDao.doJoin(loginId, loginPw, name);
    }

    public Member getMemberLoginId(String enterLoginId) {
        return memberDao.getMemberLoginId(enterLoginId);
    }

    public void loginFailCount(String enterLoginId) {
        memberDao.loginFailCount(enterLoginId);
    }

    public void loginFailCountReset() {
        memberDao.loginFailCountReset();
    }
}
