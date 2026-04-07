package com.artlighter.glucosecontrolservice.integration.util.mapper;

import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.integration.dto.IntegrationProfileDTO;
import com.artlighter.glucosecontrolservice.integration.entity.IntegrationProfile;
import org.springframework.stereotype.Component;

@Component
public class IntegrationProfileMapper implements DTOMapper<IntegrationProfile, IntegrationProfileDTO> {

    @Override
    public IntegrationProfileDTO mapToDTO(IntegrationProfile internal) {
        return new IntegrationProfileDTO(
                internal.isNightscoutEnabled(),
                internal.getNightscoutApiSecret());
    }

    @Override
    public IntegrationProfile mapToInternal(IntegrationProfileDTO externalDTO) {
        return new IntegrationProfile(0,
                externalDTO.isNightscoutEnabled(),
                externalDTO.nightscoutApiSecret());
    }

}
