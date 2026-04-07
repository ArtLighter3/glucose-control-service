package com.artlighter.glucosecontrolservice.user.util.mapper;

import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.user.dto.userinfo.UserUpdatableInfoDTO;
import com.artlighter.glucosecontrolservice.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserUpdatableInfoMapper implements DTOMapper<User, UserUpdatableInfoDTO> {

    @Override
    public UserUpdatableInfoDTO mapToDTO(User internal) {
        return new UserUpdatableInfoDTO(
                internal.getEmail(),
                internal.getFirstName(),
                internal.getMiddleName(),
                internal.getLastName(),
                internal.getBirthDate());
    }

    @Override
    public User mapToInternal(UserUpdatableInfoDTO externalDTO) {
        User user = new User();

        user.setEmail(externalDTO.email());
        user.setFirstName(externalDTO.firstName());
        user.setMiddleName(externalDTO.middleName());
        user.setLastName(externalDTO.lastName());
        user.setBirthDate(externalDTO.birthDate());

        return user;
    }
}
