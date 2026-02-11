package com.artlighter.glucosecontrolservice.templates.util.mapper;

import com.artlighter.glucosecontrolservice.general.DTOMapper;
import com.artlighter.glucosecontrolservice.templates.dto.PatientTemplateEntityDTO;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;

public interface TemplateMapper<INT extends PatientTemplateEntity, EXT extends PatientTemplateEntityDTO>
        extends DTOMapper<INT, EXT> {

}
