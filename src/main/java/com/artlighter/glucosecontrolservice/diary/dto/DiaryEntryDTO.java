package com.artlighter.glucosecontrolservice.diary.dto;

import java.time.OffsetDateTime;

/**
 * Общий интерфейс для DTO-объектов записей дневников. Записи разных типов могут рассматриваться как единая
 * коллекция, поэтому это помогает избежать лишнего шаблонного кода для каждого отдельного типа записи дневника.
 * Из-за отсутствия наследования у record используется единый интерфейс,
 * а не абстрактный класс с инициализированными общими полями.
 */
public interface DiaryEntryDTO {
    Float value();
    OffsetDateTime commitedAt();
    String notes();
}
