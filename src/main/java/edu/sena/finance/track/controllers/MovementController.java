package edu.sena.finance.track.controllers;

import edu.sena.finance.track.entities.Movement;
import edu.sena.finance.track.entities.User;
import edu.sena.finance.track.services.MovementService;
import edu.sena.finance.track.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/companies/{companyId}/movements")
public class MovementController {

    private MovementService movementService;
    private UserService userService;

    @Autowired
    public MovementController(MovementService movementService, UserService userService) {
        this.movementService = movementService;
        this.userService = userService;
    }

    @GetMapping()
    public String getMovements(Model model, @AuthenticationPrincipal OidcUser principal, @PathVariable Long companyId) {
        model.addAttribute("sub", userService.get(principal));
        model.addAttribute("movements", movementService.getAllByCompanyId(companyId));
        return "user/movements";
    }

    @GetMapping("/new")
    public String newMovement(Model model, @PathVariable Long companyId, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("sub", userService.get(principal));
        model.addAttribute("companyId", companyId);
        model.addAttribute("movement", new Movement());
        return "user/movement";
    }

    @GetMapping("/{movementId}")
    public String getMovement(Model model,
                              @PathVariable Long companyId,
                              @PathVariable Long movementId,
                              @AuthenticationPrincipal OidcUser principal) {

        User sub = userService.get(principal);
        Movement movement = movementService.getById(movementId);
        if (movement.getCreatedBy() == sub) {
            model.addAttribute("sub", sub);
            model.addAttribute("companyId", companyId);
            model.addAttribute("movement", movement);
            return "user/movement";
        }
        return "access-denied";
    }

    @PostMapping()
    public RedirectView createMovement(Model model,
                                       @AuthenticationPrincipal OidcUser principal,
                                       @PathVariable Long companyId,
                                       @ModelAttribute Movement movement) {
        User sub =  userService.get(principal);
        movementService.createByUser(sub, movement);
        model.addAttribute("sub",sub);
        model.addAttribute("companyId", companyId);
        return new RedirectView("/companies/" + companyId + "/movements");
    }

    @PutMapping("/{movementId}")
    public RedirectView updateMovement(Model model,
                                       @AuthenticationPrincipal OidcUser principal,
                                       @PathVariable Long companyId,
                                       @PathVariable Long movementId,
                                       @ModelAttribute Movement movement) {
        User sub =  userService.get(principal);
        movementService.updateById(movementId, movement);
        model.addAttribute("sub",sub);
        model.addAttribute("companyId", companyId);
        return new RedirectView("/companies/" + companyId + "/movements");
    }

    @DeleteMapping("/{movementId}")
    public RedirectView deleteMovement(Model model,
                                       @AuthenticationPrincipal OidcUser principal,
                                       @PathVariable Long companyId,
                                       @PathVariable Long movementId,
                                       @ModelAttribute Movement movement) {
        User sub =  userService.get(principal);
        movementService.deleteById(movementId, companyId);
        model.addAttribute("sub",sub);
        model.addAttribute("companyId", companyId);
        return new RedirectView("/companies/" + companyId + "/movements");
    }

}
