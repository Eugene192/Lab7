package org.eugene.common;

import java.io.Serializable;

// This class represents user credentials: password and username

public record UserCredentials(String username, String password) implements Serializable {
}
