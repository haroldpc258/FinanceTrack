package edu.sena.finance.track.services;

import edu.sena.finance.track.entities.Company;
import edu.sena.finance.track.entities.User;
import edu.sena.finance.track.repositories.CompanyRepository;
import edu.sena.finance.track.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;


    @Autowired
    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public Company getById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));
    }

    public List<User> getUsersByCompanyId(Long id) {
        Company company = getById(id);
        return company.getEmployees();
    }

    /*public List<User> getUsersByCompanyId(Long id, User user) {
        List<User> users = getById(id).getEmployees();
        users.remove(user);
        return users;
    }*/

    public Company update(Company company, Long id) {

        Company companyUpdated = getById(id);
        updateUsersStatusByCompanyId(company, companyUpdated);

        companyUpdated.setAddress(company.getAddress());
        companyUpdated.setEmail(company.getEmail());
        companyUpdated.setNit(company.getNit());
        companyUpdated.setName(company.getName());
        companyUpdated.setPhoneNumber(company.getPhoneNumber());
        companyUpdated.setStatus(company.getStatus());

        return save(companyUpdated);
    }

    private void updateUsersStatusByCompanyId(Company updatedCompany, Company companyToUpdate) {
        if (companyToUpdate.getStatus() != updatedCompany.getStatus()) {
            List<User> companyUsers = getUsersByCompanyId(companyToUpdate.getId());
            companyUsers.forEach(user -> user.setStatus(updatedCompany.getStatus()));
            userRepository.saveAll(companyUsers);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }

}

