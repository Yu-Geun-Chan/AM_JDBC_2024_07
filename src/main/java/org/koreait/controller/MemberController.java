package org.koreait.controller;

import org.koreait.articleManager.Container;
import org.koreait.dto.Member;
import org.koreait.service.MemberService;

import java.sql.Connection;

public class MemberController extends Controller {
    private Connection conn;

    private MemberService memberService;

    public MemberController(Connection conn) {
        this.conn = conn;
        this.memberService = new MemberService(conn);
    }

    public void doJoin() {
        System.out.println("== 회원가입 ==");
        String loginId = null;
        while (true) {
            System.out.print("아이디 : ");
            loginId = Container.getScanner().nextLine().trim();

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
            loginPw = Container.getScanner().nextLine().trim();

            if (loginPw.isEmpty() || loginPw.contains(" ")) {
                System.out.println("비밀번호를 입력해주세요.");
                continue;
            }

            boolean loginPwCheck = true;

            while (true) {
                System.out.print("비밀번호 확인 : ");
                String checkLoginPw = Container.getScanner().nextLine().trim();

                if (checkLoginPw.isEmpty() || checkLoginPw.contains(" ")) {
                    System.out.printf("비밀번호 확인을 입력해주세요.");
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
            name = Container.getScanner().nextLine();

            if (name.isEmpty() || name.contains(" ")) {
                System.out.println("이름을 입력해주세요.");
                continue;
            }
            break;
        }

        int id = memberService.doJoin(loginId, loginPw, name);

        System.out.printf("[%s]님 환영합니다.\n", name);
    }

    public void doLogin() {
        if (loginedMember != null) {
            System.out.println("로그아웃 후 이용해주세요.");
            return;
        }

        System.out.println("== 로그인 ==");

        String enterLoginId = null;
        Member member = null;

        while (true) {

            System.out.print("로그인 아이디 입력 : ");
            enterLoginId = Container.getScanner().nextLine();

            if (enterLoginId.startsWith("backspace")) {
                break;
            }

            if (enterLoginId.isEmpty() || enterLoginId.contains(" ")) {
                System.out.println("아이디를 입력해주세요.");
                continue;
            }

            boolean isLoginIdDup = memberService.isLoginIdDup(conn, enterLoginId);

            if (!isLoginIdDup) {
                System.out.printf("[%s]는(은) 존재하지 않는 아이디 입니다.\n", enterLoginId);
                continue;
            }

            member = memberService.getMemberLoginId(enterLoginId);

            break;
        }
        while (true) {
            if (memberService.loginLimit() == 1) {
                System.out.println("비밀번호 오류 3번");
                memberService.loginFailCountReset();
                return;
            }

            System.out.print("비밀번호 입력 : ");
            String enterLoginPw = Container.getScanner().nextLine();
            if (enterLoginPw.startsWith("backspace")) {
                break;
            }

            if (enterLoginPw.isEmpty() || enterLoginPw.contains(" ")) {
                memberService.loginFailCount(enterLoginId);
                System.out.println("비밀번호를 입력해주세요.");
                continue;
            }

            if (!member.getLoginPw().equals(enterLoginPw)) {
                memberService.loginFailCount(enterLoginId);
                System.out.println("비밀번호를 확인해주세요.");
                continue;
            }
            break;
        }
        loginedMember = member;
        System.out.printf("[%s]님 환영합니다.\n", loginedMember.getName());
    }

    public void doLogout() {
        if (loginedMember == null) {
            System.out.println("로그인 후 이용해주세요.");
            return;
        }

        loginedMember = null;

        System.out.println("로그아웃 되었습니다.");
    }

    public void showProfile() {
        if (loginedMember == null) {
            System.out.println("로그인 후 이용해주세요.");
            return;
        }

        System.out.println("== 회원정보 ==");
        System.out.printf("회원가입날짜: %s\n", loginedMember.getRegDate());
        System.out.printf("회원번호 : %d\n", loginedMember.getId());
        System.out.printf("아이디 : %s\n", loginedMember.getLoginId());
        System.out.printf("이름 : %s\n", loginedMember.getName());
    }
}