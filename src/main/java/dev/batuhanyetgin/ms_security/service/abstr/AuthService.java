package dev.batuhanyetgin.ms_security.service.abstr;

import dev.batuhanyetgin.ms_security.dto.RegisterDto;
import dev.batuhanyetgin.ms_security.dto.UserDto;
import dev.batuhanyetgin.ms_security.exception.AuthException;

public interface AuthService {
    UserDto createUser(RegisterDto registerDto) throws AuthException;

    UserDto getUserByEmail(String email) throws AuthException;


}
