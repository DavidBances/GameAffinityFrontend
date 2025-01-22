package com.gameaffinity.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Friendship {
    private final int id;
    private final int requesterId;
    private final String requesterEmail;
    private final int receiverId;
    private String status;

    /**
     * Constructor for the Friendship class.
     *
     * @param id          The unique ID of the friendship.
     * @param requesterId The ID of the user who sent the friendship request.
     * @param receiverId  The ID of the user who received the friendship request.
     * @param status      The current status of the friendship.
     */
    @JsonCreator
    public Friendship(@JsonProperty("id") int id,
                      @JsonProperty("requesterId") int requesterId,
                      @JsonProperty("requesterEmail") String requesterEmail,
                      @JsonProperty("receiverId") int receiverId,
                      @JsonProperty("status") String status) {
        this.id = id;
        this.requesterId = requesterId;
        this.requesterEmail = requesterEmail;
        this.receiverId = receiverId;
        this.status = status;
    }

    /**
     * Retrieves the unique ID of the friendship.
     *
     * @return The friendship ID.
     */
    public int getId() {
        return id;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    /**
     * Updates the current status of the friendship.
     *
     * @param status The new status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Provides a string representation of the friendship.
     *
     * @return A formatted string containing the friendship details.
     */
    @Override
    public String toString() {
        return "Friendship ID: " + id + ", Requester ID: " + requesterId + ", Receiver ID: " + receiverId + ", Status: "
                + status;
    }
}
