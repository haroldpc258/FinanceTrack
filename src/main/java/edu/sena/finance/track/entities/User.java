package edu.sena.finance.track.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.sena.finance.track.entities.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "AUTH0_ID", unique = true)
    private String auth0Id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PICTURE")
    private String picture;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "DNI", unique = true)
    private String dni;

    @Column(name = "PHONE_NUMBER", unique = true)
    private String phoneNumber;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    public User() {
        createdOn = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }

    public User(String auth0Id, String name, String email, String picture) {
        this.auth0Id = auth0Id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.status = Status.ACTIVE;
        createdOn = LocalDateTime.now();
    }

    public String getCreatedOn() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return createdOn.format(formatter);
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = LocalDateTime.parse(createdOn);
    }

    @Getter
    public enum Role {
        ADMINISTRATOR("administrator"),
        OPERATIVE("operative"),
        SUPER_USER("superUser");

        final String role;

        Role(String role) {
            this.role = role;
        }

    }


}
