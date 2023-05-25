package it.sevenbits.courses.quizzes.web.controller.security;

import java.util.Set;

/**
 * interface
 */

public interface UserCredentials {
    /**
     * get name
     * @return name
     */
    String getName();

    /**
     * get roles
     * @return list roles
     */
    Set<String> getRoles();

    /**
     * get player
     * @return id
     */
    String getPlayerId();
}
