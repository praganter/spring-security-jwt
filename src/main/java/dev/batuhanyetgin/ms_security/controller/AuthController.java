package dev.batuhanyetgin.ms_security.controller;

import dev.batuhanyetgin.ms_security.component.TokenManager;
import dev.batuhanyetgin.ms_security.dto.LoginDto;
import dev.batuhanyetgin.ms_security.dto.LoginResponseDto;
import dev.batuhanyetgin.ms_security.dto.RegisterDto;
import dev.batuhanyetgin.ms_security.dto.UserDto;
import dev.batuhanyetgin.ms_security.exception.AuthException;
import dev.batuhanyetgin.ms_security.service.abstr.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/v1/auth/")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager, TokenManager tokenManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto loginDto) throws AuthException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(), loginDto.getPassword()));
        UserDto userDto = authService.getUserByEmail(loginDto.getEmail());
        String token = tokenManager.generateToken(loginDto.getEmail(), String.valueOf(userDto.getId()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        log.info("Successfully login.");
        return ResponseEntity.ok(new LoginResponseDto(token, sdf.format(new Date()), userDto.getName()));
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDto registerDto) throws AuthException {
        authService.createUser(registerDto);
        log.info("Successfully registered.");
        return ResponseEntity.ok("Successfully registered.Now you can log-in.");
    }
}
