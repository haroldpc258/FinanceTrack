package edu.sena.finance.track.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Table(name = "MOVEMENTS")
public class Movement {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "AMOUNT")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User createdBy;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Column(name = "CONCEPT")
    private String concept;

    public Movement() {
        createdOn = LocalDateTime.now();
    }

    public Movement(Type type, Double amount, String concept) {
        this.type = type;
        this.amount = amount;
        this.concept = concept;
        createdOn = LocalDateTime.now();
    }

    public Movement(Type type, Double amount, User createdBy, LocalDateTime createdOn, String concept) {
        this.type = type;
        this.amount = amount;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.concept = concept;
    }

    public String getCreatedOn() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return createdOn.format(formatter);
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = LocalDateTime.parse(createdOn);
    }


    public enum Type {
        INCOME("income"),
        EXPENSE("expense");

        final String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
