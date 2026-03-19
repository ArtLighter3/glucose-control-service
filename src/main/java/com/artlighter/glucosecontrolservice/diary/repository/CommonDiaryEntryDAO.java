package com.artlighter.glucosecontrolservice.diary.repository;

import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

/**
 * Общий интерфейс для слоя доступа к записям дневника DiaryEntry сразу ВСЕХ типов (то есть любого
 * наследника DiaryEntry)
 */

public interface CommonDiaryEntryDAO {
    /**
     * Функция находит все ресурсы DiaryEntry определенного типа из хранилища по PatientProfile
     * в диапазоне временных отметок from и to
     * @param entryType тип DiaryEntry; если null, то выбираются DiaryEntry ВСЕХ типов;
     * @param patientProfile профиль больного, для которого надо найти DiaryEntry;
     * @param from временная отметка, с которой выбираются ресурсы (включительно);
     * @param to временная отметка, до которой выбираются ресурсы (включительно);
     * @param sort тип сортировки итоговой коллекции;
     * @return список объектов DiaryEntry;
     */
    List<DiaryEntry> getAllOfTypeBetweenDates(DiaryEntryType entryType,
                                                        PatientProfile patientProfile, Instant from, Instant to,
                                                        Sort sort);

    /**
     * Функция находит последний ресурс DiaryEntry определенного типа по параметрам сортировки
     * @param entryType тип DiaryEntry;
     * @param patientProfile профиль больного, для которого надо найти DiaryEntry;
     * @param sort тип сортировки для поиска по ней последнего элемента;
     * @return Ресурс DiaryEntry; null, если не было найдено ни одного;
     */
    DiaryEntry findLastEntryOfType(DiaryEntryType entryType, PatientProfile patientProfile, Sort sort);
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
     * @param entryType тип записи для удаления;
     * @param id ID записи, содержащая ID профиля больного и временную отметку записи;
     * @throws IllegalArgumentException в случае, если entryType равен null, либо если id
     * равен null или не содержит внутри идентификационных данных;
     */
    void deleteById(DiaryEntryType entryType, DiaryEntry.DiaryEntryID id);

    /**
     * Функция определяет, существует ли переданный ресурс DiaryEntry
     * @param entry ресурс, который необходимо проверить
     * @return true, если ресурс существует; false в ином случае
     * @throws IllegalArgumentException в случае, если entry равен null, либо не содержит
     * внутри идентифицирующих его полей patientProfile и/или commitedAt
     */
    boolean exists(DiaryEntry entry);
}
