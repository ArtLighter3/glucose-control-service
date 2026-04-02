package com.artlighter.glucosecontrolservice.user.util.mapper;

import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.user.dto.UserCreationDTO;
import com.artlighter.glucosecontrolservice.user.dto.UserDetailedInfoDTO;
import com.artlighter.glucosecontrolservice.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDetailedInfoMapper implements DTOMapper<User, UserDetailedInfoDTO> {
    @Override
    public UserDetailedInfoDTO mapToDTO(User internal) {
        return new UserDetailedInfoDTO(internal.getId(),
                internal.getUsername(),
                internal.getEmail(),
                internal.getFirstName(),
                internal.getMiddleName(),
                internal.getLastName(),
                internal.getBirthDate(),
                internal.getRoles());
    }

    @Override
    public User mapToInternal(UserDetailedInfoDTO externalDTO) {
        throw new UnsupportedOperationException();
    }
}
