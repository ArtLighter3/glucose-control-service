package com.artlighter.glucosecontrolservice.authgateway.util.mapper;

import com.artlighter.glucosecontrolservice.user.dto.userinfo.UserRegistrationDTO;
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
        user.setEmail(externalDTO.email());

        user.setFirstName(externalDTO.firstName());
        user.setMiddleName(externalDTO.middleName());
        user.setLastName(externalDTO.lastName());
        user.setBirthDate(externalDTO.birthDate());

        user.setRoles(Set.of(Role.ROLE_PATIENT));

        return user;
    }
}
