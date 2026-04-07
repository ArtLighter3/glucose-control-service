package com.artlighter.glucosecontrolservice.user.util.mapper;

import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.user.dto.userinfo.UserCreationDTO;
import com.artlighter.glucosecontrolservice.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserCreationMapper implements DTOMapper<User, UserCreationDTO> {
    @Override
    public UserCreationDTO mapToDTO(User internal) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User mapToInternal(UserCreationDTO externalDTO) {
        User user = new User();

        user.setUsername(externalDTO.username());
        user.setPassword(externalDTO.password());
        user.setEmail(externalDTO.email());
        user.setFirstName(externalDTO.firstName());
        user.setMiddleName(externalDTO.middleName());
        user.setLastName(externalDTO.lastName());
        user.setBirthDate(externalDTO.birthDate());
        user.setRoles(externalDTO.roles());

        return user;
    }
}
