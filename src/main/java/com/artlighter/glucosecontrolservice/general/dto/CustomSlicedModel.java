package com.artlighter.glucosecontrolservice.general.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

//Используется, как замена SlicedModel из Spring HATEOAS (чтобы не подключать целый модуль из-за одного класса)

@Schema(name = "Slice", description = "Страница с данными, не содержащая информации об общем количестве элементов")
public record CustomSlicedModel<T>(
        List<T> content,
        CustomSliceMetadata page
) {
}
