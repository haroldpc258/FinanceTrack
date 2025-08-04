package edu.sena.finance.track.services;

import edu.sena.finance.track.entities.Company;
import edu.sena.finance.track.entities.Movement;
import edu.sena.finance.track.entities.User;
import edu.sena.finance.track.repositories.MovementRepository;
import edu.sena.finance.track.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final CompanyService companyService;
    private final InvitationService invitationService;
    private final MovementRepository movementRepository;
    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository, CompanyService companyService, MovementRepository movementRepository, InvitationService invitationService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.movementRepository = movementRepository;
        this.invitationService = invitationService;
    }


    public boolean save(OidcUser principal) {

        if (exists(principal)) {
            String picture = principal.getPicture();
            String auth0Id = principal.getSubject();
            User user = get(principal);
            user.setPicture(picture);
            user.setAuth0Id(auth0Id);
            save(user);
            return true;
        }
        return false;
    }

    public void save(User user, Long companyId) {
        Company company = companyService.getById(companyId);
        company.getEmployees().add(user);

        companyService.save(company);
        user.setCompany(company);
        save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void create(User user, Long companyId) {

        save(user, companyId);

        try {
            sendInvitationEmail(user);
        } catch (Exception e) {
            throw new RuntimeException("Error sending invitation email " + e.getMessage(), e);
        }
    }

    private void sendInvitationEmail(User user) {
        invitationService.sendInvitationEmail(user);
    }

    public void update(User user, Long id) {
        User userUpdated = getById(id);

        userUpdated.setName(user.getName());
        userUpdated.setEmail(user.getEmail());
        userUpdated.setPhoneNumber(user.getPhoneNumber());
        userUpdated.setDni(user.getDni());
        userUpdated.setRole(user.getRole());
        userUpdated.setStatus(user.getStatus());

        save(userUpdated);
    }

    public User get(OidcUser principal) {
        return userRepository.findByEmail(principal.getEmail());
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    private boolean exists(OidcUser principal) {
        return userRepository.existsByEmail(principal.getEmail());
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteById(Long userId, Long companyId) {
        Company company = companyService.getById(companyId);

        List<Movement> movements = movementRepository.findMovementsByCreatedBy_Id(userId);
        company.getMovements().removeAll(movements);
        company.getEmployees().remove(getById(userId));
        movementRepository.deleteMovementsByCreatedBy_Id(userId);
        companyService.save(company);
        userRepository.deleteById(userId);
    }

    public boolean isSuperUser(OidcUser user) {
        return exists(user) && get(user).getRole() == User.Role.SUPER_USER;
    }

    public boolean isOperative(OidcUser user) {
        return exists(user) && get(user).getRole() == User.Role.OPERATIVE;
    }

}
