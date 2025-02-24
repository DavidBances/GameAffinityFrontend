package com.gameaffinity.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("REGULAR_USER")
public class RegularUser extends UserBase {
    public RegularUser() {
    }

    public RegularUser(int id, String name, String email) {
        super(id, name, email, UserRole.REGULAR_USER);
    }
}
