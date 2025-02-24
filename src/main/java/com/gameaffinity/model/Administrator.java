package com.gameaffinity.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMINISTRATOR")
public class Administrator extends UserBase {
    public Administrator() {
    }

    public Administrator(int id, String name, String email) {
        super(id, name, email, UserRole.ADMINISTRATOR);
    }
}
