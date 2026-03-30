package com.artlighter.glucosecontrolservice.user.util.mapper;

import com.artlighter.glucosecontrolservice.authgateway.ServiceUserDetails;
import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.user.dto.UserSessionDTO;
import com.artlighter.glucosecontrolservice.user.entity.Role;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserSessionMapper implements DTOMapper<ServiceUserDetails, UserSessionDTO> {
    @Override
    public UserSessionDTO mapToDTO(ServiceUserDetails internal) {
        return new UserSessionDTO(internal.getId(), internal.getUsername(),
                internal.getAuthorities().stream()
                        .filter((authority) ->
                                authority.getAuthority() != null && authority.getAuthority().startsWith("ROLE_"))
                        .map(authority -> Role.valueOf(authority.getAuthority()))
                        .collect(Collectors.toSet()));
    }

    @Override
    public ServiceUserDetails mapToInternal(UserSessionDTO externalDTO) {
        throw new UnsupportedOperationException();
    }
}
