package edu.sena.finance.track.controllers;

import edu.sena.finance.track.entities.Company;
import edu.sena.finance.track.entities.User;
import edu.sena.finance.track.services.CompanyService;
import edu.sena.finance.track.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;

    @Autowired
    public CompanyController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String newCompany(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (userService.isSuperUser(principal)) {
            model.addAttribute("sub", userService.get(principal));
            model.addAttribute("company",  new Company());
            return "admin/company";
        }
        return "access-denied";
    }

    @GetMapping("/{id}")
    public String updateCompany(@AuthenticationPrincipal OidcUser principal, @PathVariable Long id, Model model) {
        if (userService.isSuperUser(principal)) {
            model.addAttribute("sub", userService.get(principal));
            model.addAttribute("company", companyService.getById(id));
            return "admin/company";
        }
        return "access-denied";
    }

    @PostMapping()
    public RedirectView createCompany(Model model, @AuthenticationPrincipal OidcUser principal, @ModelAttribute Company company) {
        model.addAttribute("sub", userService.get(principal));
        companyService.save(company);
        return new RedirectView("/companies");
    }

    @PutMapping("/{id}")
    public RedirectView updateCompany(@AuthenticationPrincipal OidcUser principal,
                                      @ModelAttribute Company company, @PathVariable Long id, Model model) {
        model.addAttribute("sub", userService.get(principal));
        companyService.update(company, id);
        return new RedirectView("/companies");
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCompany(@AuthenticationPrincipal OidcUser principal, @PathVariable Long id, Model model) {
        model.addAttribute("sub", userService.get(principal));
        companyService.deleteById(id);
        return new RedirectView("/companies");
    }
}
