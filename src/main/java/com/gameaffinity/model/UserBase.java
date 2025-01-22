package com.gameaffinity.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegularUser.class, name = "regular_user"),
        @JsonSubTypes.Type(value = Moderator.class, name = "moderator"),
        @JsonSubTypes.Type(value = Administrator.class, name = "administrator")
})

public abstract class UserBase {
    private final int id;
    private String name;
    private String email;
    private String password;
    private String role;

    /**
     * Constructor for the UserBase class.
     *
     * @param id    The unique identifier for the user.
     * @param name  The name of the user.
     * @param email The email address of the user.
     * @param role  The role assigned to the user (e.g., "Administrator",
     *              "Moderator").
     */
    public UserBase(int id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets the unique identifier for the user.
     *
     * @return The user's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the user.
     *
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The new username.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     *
     * @param password The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role assigned to the user.
     *
     * @return The user's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role for the user.
     *
     * @param role The new role.
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name='" + name + ", email='" + email + ", password='" + password + ", role='" + role;
    }
}
