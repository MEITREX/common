package de.unistuttgart.iste.gits.common.user_handling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class User {
    private UUID id;
    private String userName;
    private String firstName;
    private String lastName;
}
