package dev.batuhanyetgin.ms_security.service.impl;

import dev.batuhanyetgin.ms_security.dto.RegisterDto;
import dev.batuhanyetgin.ms_security.dto.UserDto;
import dev.batuhanyetgin.ms_security.entity.UserEntity;
import dev.batuhanyetgin.ms_security.exception.AuthException;
import dev.batuhanyetgin.ms_security.repository.UserRepository;
import dev.batuhanyetgin.ms_security.service.abstr.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    private boolean isUserExists(String email) {
        UserEntity user = userRepository.getByEmail(email);
        return user != null;
    }

    @Override
    public UserDto createUser(RegisterDto registerDto) throws AuthException {
        if (isUserExists(registerDto.getEmail())) {
            throw new AuthException("Email in use.");
        }
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        UserEntity newUser = userRepository.save(modelMapper.map(registerDto, UserEntity.class));
        log.info("User added -> " + newUser);
        return modelMapper.map(newUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) throws AuthException {
        if (isUserExists(email)) {
            return modelMapper.map(userRepository.getByEmail(email), UserDto.class);
        } else {
            throw new AuthException("There is no user with given credentials.");
        }
    }
}
