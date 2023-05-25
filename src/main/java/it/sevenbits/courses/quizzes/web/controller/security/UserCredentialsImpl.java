package it.sevenbits.courses.quizzes.web.controller.security;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;
import java.util.Collections;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * class user credentials
 */
public class UserCredentialsImpl implements UserCredentials {

    @JsonProperty("playerId")
    private final String playerId;
    @JsonProperty("name")
    private final String name;

    @JsonProperty("roles")
    private final Set<String> roles;

    /**
     * constructor
     * @param name - name
     * @param roles - roles
     * @param playerId - player id
     */
    @JsonCreator
    public UserCredentialsImpl(final String name, final Collection<String> roles, final String playerId) {
        this.playerId = playerId;
        this.name = name;
        this.roles = Collections.unmodifiableSet(new LinkedHashSet<>(roles));
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public Set<String> getRoles() {
        return roles;
    }

}
