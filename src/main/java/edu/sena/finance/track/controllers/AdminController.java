package edu.sena.finance.track.controllers;

import edu.sena.finance.track.services.CompanyService;
import edu.sena.finance.track.services.DashboardService;
import edu.sena.finance.track.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final UserService userService;
    private final CompanyService companyService;

    @Autowired
    public AdminController(UserService userService, CompanyService companyService) {
        this.userService = userService;
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public String getCompanies(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (userService.isSuperUser(principal)) {
            model.addAttribute("sub", userService.get(principal));
            model.addAttribute("companies", companyService.getAll());
            return "admin/companies";
        }
        return "access-denied";
    }

    @GetMapping("/users")
    public String getUsers(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (userService.isSuperUser(principal)) {
            model.addAttribute("sub", userService.get(principal));
            model.addAttribute("users", userService.getAll());
            model.addAttribute("companies", companyService.getAll());
            return "admin/users";
        }
        return "access-denied";
    }

}
