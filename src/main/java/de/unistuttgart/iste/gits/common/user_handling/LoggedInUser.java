package de.unistuttgart.iste.gits.common.user_handling;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

/**
 * This class represents user data for a logged-in user as provided by keycloak.
 */
@Getter
public class LoggedInUser {
    private final UUID id;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String authToken;

    public LoggedInUser(@JsonProperty("id") UUID id,
                        @JsonProperty("userName") String userName,
                        @JsonProperty("firstName") String firstName,
                        @JsonProperty("lastName") String lastName,
                        @JsonProperty("authToken") String authToken) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authToken = authToken;
    }
}
