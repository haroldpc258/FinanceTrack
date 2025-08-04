package edu.sena.finance.track.entities.enums;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE("active"),
    SUSPENDED("suspended");

    private final String status;

    Status(String status) {
        this.status = status;
    }

}
