package edu.sena.finance.track.entities;

import edu.sena.finance.track.entities.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "COMPANIES")
public class Company {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NIT", unique = true)
    private String nit;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE_NUMBER", unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> employees;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movement> movements;


    public Company() {
        employees = new ArrayList<>();
        movements = new ArrayList<>();
        createdOn = LocalDateTime.now();
        status = Status.ACTIVE;
    }

    public String getCreatedOn() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return createdOn.format(formatter);
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = LocalDateTime.parse(createdOn);
    }

}
