package it.sevenbits.courses.quizzes.web.service.login;

import it.sevenbits.courses.quizzes.core.model.user.User;
import it.sevenbits.courses.quizzes.web.controller.model.Login;

/**
 * interface Login Service
 */
public interface ILoginService {
    /**
     * login user
     * @param login - login
     * @return user
     */
    User login(Login login);

    /**
     * add user
     * @param login - login user`s add
     * @return true if add user
     */
    boolean addUser(Login login);
}
