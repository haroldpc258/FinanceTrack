package edu.sena.finance.track.services;

import edu.sena.finance.track.entities.Company;
import edu.sena.finance.track.repositories.MovementRepository;
import edu.sena.finance.track.entities.Movement;
import edu.sena.finance.track.entities.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final CompanyService companyService;
    private final UserService userService;

    @Autowired
    public MovementService(MovementRepository movementRepository, CompanyService companyService, UserService userService) {
        this.movementRepository = movementRepository;
        this.companyService = companyService;
        this.userService = userService;
    }

    public List<Movement> getAllByCompanyId(Long companyId) {
        return companyService.getById(companyId).getMovements();
    }

    public Movement getById(Long id) {
        return movementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movement not found with id: " + id));
    }

    public Movement createByUser(User user, Movement movement) {
        movement.setCreatedBy(user);
        Company company = user.getCompany();
        company.getMovements().add(movement);
        companyService.save(company);
        return save(movement);
    }

    public Movement updateById(Long id, Movement movement) {

        Movement movementUpdated = movementRepository.getById(id);
        movementUpdated.setType(movement.getType());
        movementUpdated.setAmount(movement.getAmount());
        movementUpdated.setConcept(movement.getConcept());

        return save(movementUpdated);
    }

    @Transactional
    public void deleteById(Long movementId, Long companyId){
        Company company = companyService.getById(companyId);
        Movement movement = getById(movementId);

        company.getMovements().remove(movement);
        companyService.save(company);
        movementRepository.deleteById(movementId);
    }

    public Movement save(Movement movement) {
        return movementRepository.save(movement);
    }

}
