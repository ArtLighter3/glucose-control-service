package com.artlighter.glucosecontrolservice.auth.controller.templates;

import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.auth.util.exception.ValidationIsFailedException;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.user.service.PatientProfileService;
import com.artlighter.glucosecontrolservice.templates.dto.PatientTemplateEntityDTO;
import com.artlighter.glucosecontrolservice.templates.dto.TemplateDeletionDTO;
import com.artlighter.glucosecontrolservice.templates.entity.PatientTemplateEntity;
import com.artlighter.glucosecontrolservice.templates.service.TemplateService;
import com.artlighter.glucosecontrolservice.templates.util.mapper.TemplateMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/patients/{userId}/templates/")
public abstract class AbstractPatientTemplateResourceController
        <INT extends PatientTemplateEntity, EXT extends PatientTemplateEntityDTO> {

    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_SHOW_OWN', " +
            "#userId, authentication)")
    @GetMapping("/default-by-name")
    public EXT getTemplate(@PathVariable int userId,
                            @RequestParam String name) {
        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        INT template = getTemplateService().getByName(patientProfile.getId(), name);

        return getTemplateMapper().mapToDTO(template);
    }

    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_SHOW_OWN', " +
            "#userId, authentication)")
    @GetMapping("/defaults")
    public Page<EXT> getTemplates(@PathVariable int userId,
                                  @PageableDefault(size = 10, page = 0, sort = "id.name") Pageable pageable) {
        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        Page<INT> templates = getTemplateService().getAllByPatientProfileId(patientProfile.getId(), pageable);

        return templates.map(getTemplateMapper()::mapToDTO);
    }

    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_SHOW_OWN', " +
            "#userId, authentication)")
    @GetMapping("/defaults/search")
    public Page<EXT> getTemplatesBySearchQuery(@PathVariable int userId,
                                               @RequestParam String query,
                                               @PageableDefault(size = 10, page = 0, sort = "id.name")
                                                   Pageable pageable) {
        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        Page<INT> templates = getTemplateService().searchByNameQuery(patientProfile.getId(), query, pageable);

        return templates.map(getTemplateMapper()::mapToDTO);
    }

    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_ADD_OWN', " +
            "#userId, authentication)")
    @PostMapping("/default")
    public EXT postTemplate(@PathVariable int userId, @RequestBody @Valid EXT template, BindingResult bindingResult) {
        INT added = post(userId, template, bindingResult);
        return getTemplateMapper().mapToDTO(added);
    }

    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_UPDATE_OWN', " +
            "#userId, authentication)")
    @PutMapping("/default")
    public EXT putTemplate(@PathVariable int userId, @RequestBody @Valid EXT template, BindingResult bindingResult) {
        INT updated = update(userId, template, bindingResult);
        return getTemplateMapper().mapToDTO(updated);
    }

    @PreAuthorize("@resourceAccessInspector.hasPermissionForResource(null, null, 'TEMPLATE_DELETE_OWN', " +
            "#userId, authentication)")
    @DeleteMapping("/default")
    public TemplateDeletionDTO deleteTemplate(@PathVariable int userId,
                                              @RequestBody @Valid TemplateDeletionDTO deletionDTO,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        getTemplateService().deleteFromPatient(patientProfile.getId(), deletionDTO.name());

        return deletionDTO;
    }

    protected INT post(int userId, EXT template, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        INT toAdd = getTemplateMapper().mapToInternal(template);
        return getTemplateService().addToPatient(toAdd, patientProfile.getId(), toAdd.getId().getName());
    }

    protected INT update(int userId, EXT template, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationIsFailedException(bindingResult, "request body is invalid");

        PatientProfile patientProfile = getPatientProfileOrThrowException(userId);

        INT toUpdate = getTemplateMapper().mapToInternal(template);
        return getTemplateService().update(toUpdate, patientProfile.getId(), toUpdate.getId().getName());
    }

    protected PatientProfile getPatientProfileOrThrowException(int userId) {
        PatientProfile patientProfile = getPatientProfileService().getByUserId(userId);
        if (patientProfile == null) throw new ResourceNotFoundException("patient not found");

        return patientProfile;
    }

   // protected abstract EXT mapToDTO(INT internal);
    protected abstract TemplateService<INT> getTemplateService();
    protected abstract PatientProfileService getPatientProfileService();
    protected abstract TemplateMapper<INT, EXT> getTemplateMapper();
}
