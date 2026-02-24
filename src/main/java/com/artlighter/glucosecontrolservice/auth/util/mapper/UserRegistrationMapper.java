package com.artlighter.glucosecontrolservice.auth.util.mapper;

import com.artlighter.glucosecontrolservice.auth.dto.UserRegistrationDTO;
import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import com.artlighter.glucosecontrolservice.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserRegistrationMapper implements DTOMapper<User, UserRegistrationDTO> {

    @Override
    public UserRegistrationDTO mapToDTO(User internal) {
        throw new UnsupportedOperationException("no conversion to UserRegistrationDTO from User supported.");
    }

    @Override
    public User mapToInternal(UserRegistrationDTO externalDTO) {
        User user = new User();

        user.setUsername(externalDTO.username());
        user.setPassword(externalDTO.password());
        user.setRoles(Set.of(Role.ROLE_PATIENT));

        return user;
    }
}
