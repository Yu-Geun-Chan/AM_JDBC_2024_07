package org.koreait.controller;

import org.koreait.dto.Member;
import org.koreait.service.MemberService;
import org.koreait.util.DBUtil;
import org.koreait.util.SecSql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class MemberController extends Controller {
    private Scanner sc;
    private Connection conn;

    private MemberService memberService;

    public MemberController(Scanner sc, Connection conn) {
        this.sc = sc;
        this.conn = conn;
        this.memberService = new MemberService();
    }

    public void doJoin() {
        System.out.println("== 회원가입 ==");
        String loginId = null;
        while (true) {
            System.out.print("아이디 : ");
            loginId = sc.nextLine().trim();

            if (loginId.isEmpty() || loginId.contains(" ")) {
                System.out.println("아이디를 입력해주세요.");
                continue;
            }

            boolean isLoginIdDup = memberService.isLoginIdDup(conn, loginId);

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
        sql.append("updateDate = NOW(),");
        sql.append("loginId = '" + loginId + "',");
        sql.append("loginPw = '" + loginPw + "',");
        sql.append("name = '" + name + "';");

        int id = DBUtil.insert(conn, sql);

        System.out.printf("[%s]님 환영합니다.\n", name);
    }

    public void doLogin() {
        if (loginedMember != null) {
            System.out.println("로그아웃 후 이용해주세요.");
            return;
        }

        System.out.println("== 로그인 ==");
        List<Member> members = new ArrayList<Member>();

        while (true) {
            System.out.print("아이디 입력 : ");
            String enterLoginId = sc.nextLine();
            System.out.print("비밀번호 입력 : ");
            String enterLoginPw = sc.nextLine();
            SecSql sql = new SecSql();
            sql.append("SELECT * FROM member;");

            List<Map<String, Object>> memberListMap = DBUtil.selectRows(conn, sql);

            for (Map<String, Object> memberMap : memberListMap) {
                members.add(new Member(memberMap));
            }

            Member member = null;

            for (Member m : members) {
                if (m.getLoginId().equals(enterLoginId)) {
                    member = m;
                }
                break;
            }

            if (member == null) {
                System.out.printf("[%s]은(는) 존재하지 않는 아이디 입니다.\n", enterLoginId);
                continue;
            }

            if (!member.getLoginPw().equals(enterLoginPw)) {
                System.out.println("비밀번호를 확인해주세요.");
                continue;
            }

            loginedMember = member;
            System.out.printf("[%s]님 환영합니다.\n", loginedMember.getName());
            break;
        }
    }

    public void doLogout() {
        if (loginedMember == null) {
            System.out.println("로그인 후 이용해주세요.");
            return;
        }

        loginedMember = null;
    }
}
