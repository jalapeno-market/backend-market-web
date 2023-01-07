package hallapinyoMarket.hallapinyoMarketspring.admin;

import hallapinyoMarket.hallapinyoMarketspring.controller.login.LoginForm;
import hallapinyoMarket.hallapinyoMarketspring.controller.login.SessionConst;
import hallapinyoMarket.hallapinyoMarketspring.domain.Member;
import hallapinyoMarket.hallapinyoMarketspring.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin")
    public String home(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute(SessionConst.LOGIN_MEMBER) != null) {
            Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            if(adminService.checkAdmin(member.getUserId(), member.getPassword())) {
                return "AdminHome";
            }
        }
        model.addAttribute("loginForm", new LoginForm());
        return "AdminLogin";
    }

    @PostMapping("/admin/login")
    public String login(LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        checkAdmin(loginForm.getLoginId(), loginForm.getPassword(), response);

        Member loginMember = adminService.getAdminMember();
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "AdminHome";
    }

    private void validAuthorized(HttpSession session, HttpServletResponse response) throws IOException {
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            response.sendError(401, "잘못된 접근입니다.");
        } else {
            Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            checkAdmin(member.getUserId(), member.getUserId(), response);
        }
    }

    private void checkAdmin(String userId, String password, HttpServletResponse response) throws IOException {
        if(!adminService.checkAdmin(userId, password)) {
            response.sendError(403, "관리자가 아닙니다.");
        }
    }

    @GetMapping("/admin/members")
    public String memberList(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        validAuthorized(request.getSession(false), response);
        model.addAttribute("members", adminService.findAll());
        return "AdminMember";
    }

    @GetMapping("/admin/members/{id}/edit")
    public String findMember(@PathVariable Long id, Model model, HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        validAuthorized(request.getSession(false), response);
        model.addAttribute("member", adminService.find(id));
        return "AdminMemberEdit";
    }

    @PostMapping("/admin/members/{id}/edit")
    public String updateMember(@PathVariable Long id, Member member,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        validAuthorized(request.getSession(false), response);
        adminService.updateMember(id, member.getUserId(), member.getNickname());
        return "redirect:/admin/members";
    }

    @PostMapping("/admin/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:/admin";
    }
}
