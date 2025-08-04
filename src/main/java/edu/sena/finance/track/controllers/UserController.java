package edu.sena.finance.track.controllers;

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
@RequestMapping("/companies/{companyId}/users")
public class UserController {

    private final UserService userService;
    private final CompanyService companyService;

    @Autowired
    public UserController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    @GetMapping()
    public String getUsers(@AuthenticationPrincipal OidcUser principal, @PathVariable Long companyId, Model model) {
        User sub = userService.get(principal);
        model.addAttribute("sub", sub);
        model.addAttribute("company", companyService.getById(companyId));
        model.addAttribute("users", companyService.getUsersByCompanyId(companyId));
        return "admin/users";
    }

    @GetMapping("/new")
    public String createUser(Model model, @PathVariable Long companyId, @AuthenticationPrincipal OidcUser principal) {
        if (!userService.isOperative(principal)) {
            model.addAttribute("sub", userService.get(principal));
            model.addAttribute("companyId", companyId);
            model.addAttribute("user",  new User());
            return "admin/user";
        }

        return "access-denied";
    }

    @GetMapping("/{userId}")
    public String updateUser(Model model, @AuthenticationPrincipal OidcUser principal,
                                    @PathVariable Long companyId, @PathVariable Long userId) {
        if (!userService.isOperative(principal)) {
            model.addAttribute("sub", userService.get(principal));
            model.addAttribute("companyId", companyId);
            model.addAttribute("user",  userService.getById(userId));
            return "admin/user";
        }
        return "access-denied";
    }

    @PostMapping()
    public RedirectView createUser(Model model, @PathVariable Long companyId,
                                          @AuthenticationPrincipal OidcUser principal, @ModelAttribute User user) {
        model.addAttribute("sub", userService.get(principal));
        userService.create(user, companyId);
        return new RedirectView("/companies/" + companyId + "/users");
    }

    @PutMapping("/{userId}")
    public RedirectView updateUser(Model model,
                                   @PathVariable Long companyId,
                                   @AuthenticationPrincipal OidcUser principal,
                                   @ModelAttribute User user,
                                   @PathVariable Long userId) {
        model.addAttribute("sub", userService.get(principal));
        userService.update(user, userId);
        return new RedirectView("/companies/" + companyId + "/users");
    }

    @DeleteMapping("/{userId}")
    public RedirectView deleteUser(@AuthenticationPrincipal OidcUser principal,
                                   @PathVariable Long companyId,
                                   @PathVariable Long userId,
                                   Model model) {

        model.addAttribute("sub", userService.get(principal));
        userService.deleteById(userId, companyId);
        return new RedirectView("/companies/" + companyId + "/users");
    }

}
