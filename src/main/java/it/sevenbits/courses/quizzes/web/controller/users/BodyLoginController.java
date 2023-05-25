package it.sevenbits.courses.quizzes.web.controller.users;

import it.sevenbits.courses.quizzes.core.model.user.User;
import it.sevenbits.courses.quizzes.web.controller.model.Login;
import it.sevenbits.courses.quizzes.web.controller.model.Token;
import it.sevenbits.courses.quizzes.web.controller.security.JwtTokenService;
import it.sevenbits.courses.quizzes.web.service.login.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * class for login controller
 */
@RestController
@RequestMapping("/signin")
public class BodyLoginController {
    private final LoginService loginService;
    private final JwtTokenService tokenService;

    /**
     * constructor
     * @param loginService - login service
     * @param tokenService - token service
     */
    public BodyLoginController(final LoginService loginService, final JwtTokenService tokenService) {
        this.loginService = loginService;
        this.tokenService = tokenService;
    }

    /**
     * create token
     * @param login - login
     * @return token
     */
    @CrossOrigin("http://localhost:3000")
    @PostMapping
    @ResponseBody
    public ResponseEntity<Token> create(@RequestBody final Login login) {
        User user = loginService.login(login);
        String token = tokenService.createToken(user);
        return ResponseEntity.ok().body(new Token(token));
    }
}
