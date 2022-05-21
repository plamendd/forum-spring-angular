package com.plamendd.forum.service;


import com.plamendd.forum.dto.AuthenticationResponce;
import com.plamendd.forum.dto.LoginRequest;
import com.plamendd.forum.dto.RefreshTokenRequest;
import com.plamendd.forum.dto.RegisterRequest;
import com.plamendd.forum.exeptions.ForumException;
import com.plamendd.forum.model.NotificationEmail;
import com.plamendd.forum.model.Refreshtoken;
import com.plamendd.forum.model.User;
import com.plamendd.forum.model.VerificationToken;
import com.plamendd.forum.repository.RefreshtokenRepository;
import com.plamendd.forum.repository.UserRepository;
import com.plamendd.forum.repository.VerificationTokenRepository;
import com.plamendd.forum.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshtokenService refreshtokenService;

    @Transactional
    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
                user.setUsername(registerRequest.getUsername());
                user.setEmail(registerRequest.getEmail());
                user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
                user.setCreated(Instant.now());
                user.setEnabled(false);

        userRepository.save(user);
        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Please activate your account",
                user.getEmail(), "Thank you for signing up. " +
                "Please click on the below url to activate your account: " +
                "http://localhost:8080/api/v1/auth/verifyaccount/" + token));

    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .build();

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String verificationToken) {
         VerificationToken token = verificationTokenRepository.findByToken(verificationToken)
                    .orElseThrow(() -> new ForumException("The verificationtoken is invalid."));

            enableUser(token);
    }

    @Transactional
    private void enableUser(VerificationToken token) {
        String name = token.getUser().getUsername();
        User user =userRepository.findByUsername(name)
                .orElseThrow(() -> new ForumException("No user found with " + name));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponce login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return AuthenticationResponce.builder()
                .authenticationToken(token)
                .refreshToken(refreshtokenService.generateToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }


    public AuthenticationResponce refreshToken(RefreshTokenRequest refreshTokenRequest) {
            refreshtokenService.validateToken(refreshTokenRequest.getRefreshtoken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponce.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshtoken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }


    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
    }
}
