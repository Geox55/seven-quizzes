package it.sevenbits.courses.quizzes.web.controller.users;

import it.sevenbits.courses.quizzes.core.model.user.User;
import it.sevenbits.courses.quizzes.core.repository.user.UserRepository;
import it.sevenbits.courses.quizzes.web.controller.security.AuthRoleRequired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

/**
 * Controller to list users.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository usersRepository;

    /**
     * constructor
     *
     * @param userRepository - user repository
     */
    public UserController(final UserRepository userRepository) {
        this.usersRepository = userRepository;
    }
    @CrossOrigin("http://localhost:3000")
    @GetMapping
    @ResponseBody
    @AuthRoleRequired("ADMIN")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(usersRepository.findAll());
    }

    /**
     * get user info
     *
     * @param username - name
     * @return user
     */
    @CrossOrigin("http://localhost:3000")
    @GetMapping(value = "/{username}")
    @ResponseBody
    @AuthRoleRequired("ADMIN")
    public ResponseEntity<User> getUserInfo(@PathVariable("username") final String username) {
        return Optional
                .ofNullable(usersRepository.findByUserName(username))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


