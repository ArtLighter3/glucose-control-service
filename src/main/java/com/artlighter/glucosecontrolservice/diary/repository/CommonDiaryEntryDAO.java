package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

/**
 * Общий интерфейс для слоя доступа к записям дневника DiaryEntry сразу ВСЕХ типов (то есть любого
 * наследника DiaryEntry). Необходим как единый DAO для любого типа.
 */

public interface CommonDiaryEntryDAO {
    /**
     * Функция находит все ресурсы DiaryEntry определенного типа из хранилища по PatientProfile
     * в диапазоне временных отметок from и to
     * @param entryType тип DiaryEntry; если null, то выбираются DiaryEntry ВСЕХ типов;
     * @param patientProfileId ID профиля больного, для которого надо найти DiaryEntry;
     * @param from временная отметка, с которой выбираются ресурсы (включительно);
     * @param to временная отметка, до которой выбираются ресурсы (включительно);
     * @return список объектов DiaryEntry;
     */
    List<DiaryEntry> getAllOfTypeBetweenDates(DiaryEntryType entryType,
                                               int patientProfileId, Instant from, Instant to);
    /**
     * Функция находит все ресурсы DiaryEntry определенного типа из хранилища по PatientProfile
     * в диапазоне временных отметок from и to (постранично)
     * @param entryType тип DiaryEntry; если null, то выбираются DiaryEntry ВСЕХ типов;
     * @param patientProfileId ID профиля больного, для которого надо найти DiaryEntry;
     * @param from временная отметка, с которой выбираются ресурсы (включительно);
     * @param to временная отметка, до которой выбираются ресурсы (включительно);
     * @param pageable информация о пагинации;
     * @return список объектов DiaryEntry;
     */
    Slice<DiaryEntry> getAllOfTypeBetweenDates(DiaryEntryType entryType,
                                               int patientProfileId, Instant from, Instant to,
                                               Pageable pageable);

    /**
     * Функция находит все ресурсы DiaryEntry определенного типа из хранилища по PatientProfile
     * @param entryType тип DiaryEntry; если null, то выбираются DiaryEntry ВСЕХ типов;
     * @param patientProfileId ID профиля больного, для которого надо найти DiaryEntry;
     * @param pageable информация о пагинации;
     * @return список объектов DiaryEntry;
     */
    Slice<DiaryEntry> getAllOfType(DiaryEntryType entryType, int patientProfileId, Pageable pageable);

    /**
     * Функция находит все ресурсы DiaryEntry определенного типа из хранилища по PatientProfile
     * до временной отметки before
     * @param entryType тип DiaryEntry; если null, то выбираются DiaryEntry ВСЕХ типов;
     * @param before временная отметка, до которой выбираются ресурсы (включительно);
     * @param patientProfileId ID профиля больного, для которого надо найти DiaryEntry;
     * @param pageable информация о пагинации;
     * @return список объектов DiaryEntry;
     */
    Slice<DiaryEntry> getAllOfTypeBefore(DiaryEntryType entryType, int patientProfileId,
                                         Instant before,
                                         Pageable pageable);

    /**
     * Функция находит последний ресурс DiaryEntry определенного типа по параметрам сортировки, исключая записи с
     * временной отметкой позже текущего момента;
     * @param entryType тип DiaryEntry;
     * @param patientProfileId ID профиля больного, для которого надо найти DiaryEntry;
     * @param sort тип сортировки для поиска по ней последнего элемента;
     * @return Ресурс DiaryEntry; null, если не было найдено ни одного;
     */
    DiaryEntry findFirstEntryOfType(DiaryEntryType entryType, int patientProfileId, Sort sort);
    /**
     * Функция сохраняет новый ресурс DiaryEntry в хранилище, либо обновляет, если тот уже существует
     * @param entry ресурс DiaryEntry, который необходимо сохранить или обновить
     * @return сохраненный или обновленный DiaryEntry
     * @throws IllegalArgumentException в случае, если DiaryEntry равен null, либо не содержит
     * внутри идентифицирующих его полей patientProfile и/или commitedAt
     */
    DiaryEntry saveOrUpdate(DiaryEntry entry);

    /**
     * Функция удаляет ресурс DiaryEntry из хранилища для пользователя по его типу и временной отметке.
     * Если ресурс уже существует, он игнорируется.
     * @param id ID записи, содержащая ID профиля больного и временную отметку записи;
     * @throws IllegalArgumentException в случае, если entryType равен null, либо если id
     * равен null или не содержит внутри идентификационных данных;
     */
    void deleteById(DiaryEntry.DiaryEntryID id);

    /**
     * Функция определяет, существует ли переданный ресурс DiaryEntry
     * @param entry ресурс, который необходимо проверить
     * @return true, если ресурс существует; false в ином случае
     * @throws IllegalArgumentException в случае, если entry равен null, либо не содержит
     * внутри идентифицирующих его полей patientProfile и/или commitedAt
     */
    boolean exists(DiaryEntry entry);
}
