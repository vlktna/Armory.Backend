package blur.tech.armory.controller;

import blur.tech.armory.controller.models.request.LoginRequest;
import blur.tech.armory.controller.models.response.LoginResponse;
import blur.tech.armory.controller.models.request.RegisterRequest;
import blur.tech.armory.dto.entity.UserEntity;

import blur.tech.armory.dto.entity.UserRole;
import blur.tech.armory.dto.service.UserService;
import blur.tech.armory.security.Token;
import blur.tech.armory.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
public class LoginController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final Token tokenUtil;

    @PostMapping("/authorization")
    public ResponseEntity<LoginResponse> createAuthToken(@RequestBody LoginRequest user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>((LoginResponse) null, HttpStatus.BAD_REQUEST);
        }

        UserEntity newUser = userService.findByEmail(user.getEmail());
        final String jwt = getJwt(user.getEmail());

        return new ResponseEntity<>(new LoginResponse(
                newUser.getId(),
                newUser.getEmail(),
                newUser.getFirstName(),
                newUser.getLastName(),
                newUser.getRole(),
                jwt
        ), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/me")
    public ResponseEntity<LoginResponse> me(@RequestHeader Map<String, String> headers) {
        try {
            String jwt = headers.get("authorization");

            UserEntity newUser = userService.findByEmail(tokenUtil.extractUsername(jwt));

            return new ResponseEntity<>(new LoginResponse(
                    newUser.getId(),
                    newUser.getEmail(),
                    newUser.getFirstName(),
                    newUser.getLastName(),
                    newUser.getRole(),
                    jwt
            ), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>((LoginResponse) null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest userRequest) {
        if (userService.findByEmail(userRequest.getEmail()) == null) {
            UserEntity user = new UserEntity();
            user.setEmail(userRequest.getEmail());
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setPassword(userRequest.getPassword());
            user.setRole(UserRole.worker);
            userService.save(user);

            final String jwt = getJwt(userRequest.getEmail());

            return new ResponseEntity<>(new LoginResponse(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole(),
                    jwt
            ), HttpStatus.OK);
        }

        return new ResponseEntity<>((LoginResponse) null, HttpStatus.BAD_REQUEST);
    }

    private String getJwt(String email) {
        UserEntity newUser = userService.findByEmail(email);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return tokenUtil.generateToken(userDetails, newUser.getId());
    }
}

