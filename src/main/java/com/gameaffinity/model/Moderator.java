package com.gameaffinity.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MODERATOR")
public class Moderator extends UserBase {
    public Moderator() {
    }

    public Moderator(int id, String name, String email) {
        super(id, name, email, UserRole.MODERATOR);
    }
}
