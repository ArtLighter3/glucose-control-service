package com.artlighter.glucosecontrolservice.general;

public interface DTOMapper<INT, EXT> {
    EXT mapToDTO(INT internal);
    INT mapToInternal(EXT externalDTO);
}
