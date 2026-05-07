package com.artlighter.glucosecontrolservice.diary.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

/**
 * Сервис для выборки и модификации записей дневника всех типов.
 */
@Service
@Transactional
public class DiaryEntryService {
    private CommonDiaryEntryDAO commonDiaryEntryDAO;
    @Value("${glucose-control-service.diary.default-period-for-fetch-in-days}")
    private final int defaultDatePeriodInDays = 7;

    @Autowired
    public DiaryEntryService(CommonDiaryEntryDAO commonDiaryEntryDAO) {
        this.commonDiaryEntryDAO = commonDiaryEntryDAO;
    }

    /**
     * Добавляет новую запись дневника для больного.
     * @param diaryEntry запись дневника определенного типа;
     * @param patientProfileId ID профиля больного, для которого добавляется запись;
     * @param commitedAt временная отметка совершения записи;
     * @return сохраненная запись DiaryEntry;
     * @throws ResourceAlreadyExistsException если запись этого типа для этого пользователя
     *                                        с этой отметкой уже существует;
     */
    public DiaryEntry addDiaryEntry(DiaryEntry diaryEntry, int patientProfileId, Instant commitedAt) {
        fill(diaryEntry, patientProfileId, commitedAt);

        if (commonDiaryEntryDAO.exists(diaryEntry))
            throw new ResourceAlreadyExistsException(diaryEntry,
                    "Diary entry for this user and this timestamp already exists");

        return commonDiaryEntryDAO.saveOrUpdate(diaryEntry);
    }

    /**
     * Добавляет список записей дневника для больного.
     * @param entries список записей дневника, могут быть разных типов; если null, то ничего не добавляется;
     * @param patientProfileId ID профиля больного, для которого добавляются записи;
     * @param updateIfExists флаг, устанавливающий, нужно ли обновлять запись из списка, если она уже существует; если
     *                       false, то существующая запись не обновляется, а возвращается в отклоненных записях;
     * @return список ОТКЛОНЕННЫХ записей (отклонены могут быть либо из-за того,
     *         что уже существуют, а флаг updateIfExists = false, либо по иным серверным причинам);
     */
    public List<DiaryEntry> addDiaryEntries(List<DiaryEntry> entries, int patientProfileId,
                                            boolean updateIfExists) {
        if (entries == null) return Collections.emptyList();

        List<DiaryEntry> savedEntries = new ArrayList<>();

        for (DiaryEntry entry : entries) {
            entry.setProfileId(patientProfileId);

            try {
                if (updateIfExists || !commonDiaryEntryDAO.exists(entry)) {
                    commonDiaryEntryDAO.saveOrUpdate(entry);
                    savedEntries.add(entry);
                }
            } catch (Exception ignored) {}
        }

        return savedEntries;
    }

    /**
     * Обновляет существующую запись дневника для больного.
     * @param diaryEntry запись дневника;
     * @param patientProfileId ID профиля больного, для которого обновляется запись;
     * @param commitedAt временная отметка совершения обновляемой записи;
     * @return обновленная запись DiaryEntry;
     * @throws ResourceNotFoundException если запись этого типа для этого пользователя
     *                                        с этой отметкой не найдена;
     */
    public DiaryEntry updateDiaryEntry(DiaryEntry diaryEntry, int patientProfileId, Instant commitedAt) {
        fill(diaryEntry, patientProfileId, commitedAt);

        if (!commonDiaryEntryDAO.exists(diaryEntry))
            throw new ResourceNotFoundException(DiaryEntry.class, "diary entry for patient profile '"
                    + patientProfileId + "' and timestamp '" + diaryEntry.getCommitedAt()  + "' not found");

        return commonDiaryEntryDAO.saveOrUpdate(diaryEntry);
    }

    /**
     * Удаляет существующую запись дневника для больного. Ничего не возвращает. Если записи и так нет, то действие
     * игнорируется.
     * @param entryType тип записи для удаления;
     * @param patientProfileId ID профиля больного, для которого удаляется запись;
     * @param commitedAt временная отметка совершения удаляемой записи;
     */
    public void deleteDiaryEntry(DiaryEntryType entryType, int patientProfileId, Instant commitedAt) {
        commonDiaryEntryDAO.deleteById(entryType, new DiaryEntry.DiaryEntryID(patientProfileId, commitedAt));
    }

    /**
     * Находит все записи дневника самоконтроля ВСЕХ типов в заданном периоде времени (постранично).
     * @param patientProfileId ID профиля больного;
     * @param from нижняя граница временного периода выборки по UTC+0;
     *             если null, выберутся все записи до верхней границы;
     * @param to верхняя граница временного периода выборки по UTC+0;
     *           если null, то выберутся все записи после нижней границы;
     * @param pageable информация о пагинации;
     * @return страница Slice со списком записей дневника всех типов; никогда не null;
     */
    @Transactional(readOnly = true)
    public Slice<DiaryEntry> getAllDiaryEntries(int patientProfileId, Instant from, Instant to, Pageable pageable) {
        return getDiaryEntriesOfType(null, patientProfileId, from, to, pageable);
    }

    /**
     * Находит все записи дневника самоконтроля определенного типа (постранично).
     * @param entryType тип записи дневника самоконтроля; если null, то выбираются записи ВСЕХ типов;
     * @param patientProfileId ID профиля больного;
     * @param from нижняя граница временного периода выборки по UTC+0;
     *             если null, выберутся все записи до верхней границы;
     * @param to верхняя граница временного периода выборки по UTC+0;
     *           если null, то выберутся все записи после нижней границы;
     * @param pageable информация о пагинации;
     * @return страница Slice со списком записей дневника соответствующего типа; никогда не null;
     */
    @Transactional(readOnly = true)
    public Slice<DiaryEntry> getDiaryEntriesOfType(DiaryEntryType entryType, int patientProfileId,
                                                   Instant from, Instant to, Pageable pageable) {
        Slice<DiaryEntry> entries = null;

        if (to == null && from == null)
            entries = commonDiaryEntryDAO.getAllOfType(entryType, patientProfileId, pageable);
        else if (to != null && from != null)
            entries = commonDiaryEntryDAO.getAllOfTypeBetweenDates(entryType, patientProfileId, from, to, pageable);
        else if (from == null)
            entries = commonDiaryEntryDAO.getAllOfTypeBefore(entryType, patientProfileId, to, pageable);
        else
            entries = commonDiaryEntryDAO
                    .getAllOfTypeBetweenDates(entryType, patientProfileId, from, Instant.now(), pageable);

        if (entries == null) return Page.empty();
        return entries;
    }

    /**
     * Находит все записи дневника самоконтроля определенного типа в заданном периоде времени.
     * @param entryType тип записи дневника самоконтроля; если null, то выбираются записи ВСЕХ типов;
     * @param patientProfileId ID профиля больного;
     * @param from нижняя граница временного периода выборки по UTC+0;
     *             если null, то нижней границей считается отметка за неделю до to;
     * @param to верхняя граница временного периода выборки по UTC+0;
     *           если null, то верхней границей считается текущий момент;
     * @return Список записей дневника соответствующего типа; никогда не null;
     */
    @Transactional(readOnly = true)
    public List<DiaryEntry> getDiaryEntriesOfType(DiaryEntryType entryType, int patientProfileId,
                                                  Instant from, Instant to) {
        List<DiaryEntry> entries = commonDiaryEntryDAO.getAllOfTypeBetweenDates(entryType, patientProfileId, from, to);

        if (entries == null) return Collections.emptyList();

        return entries;
    }

    /**
     * Находит последнюю по временной отметке запись дневника определенного типа.
     * @param entryType тип записи дневника самоконтроля; не null;
     * @param patientProfileId ID профиля больного;
     * @return Последнюю запись дневника этого типа; null, если не было найдено;
     */
    @Transactional(readOnly = true)
    public DiaryEntry findLastEntryOfType(DiaryEntryType entryType, int patientProfileId) {
        DiaryEntry entry = commonDiaryEntryDAO.findLastEntryOfType(entryType, patientProfileId,
                Sort.by("commitedAt").descending());

        return entry;
    }

    private void fill(DiaryEntry diaryEntry, int patientProfileId, Instant commitedAt) {
        if (diaryEntry != null) {
            diaryEntry.setProfileId(patientProfileId);
            diaryEntry.setCommitedAt(commitedAt);
        }
    }

//    public List<DiaryEntry> getUserMeasurementsFromPeriod(UserDTO user, Date from, Date to) {
//        List<DiaryEntry> measurements = getAllUserMeasurements(user);
//
//        return measurements.stream()
//                .filter((entry) -> entry.getDate().after(from) && entry.getDate().before(to))
//                .toList();
//    }

//    public List<DiaryEntry> getAllMeasurements() {
//        return diaryEntryRepository.getAllMeasurements();
//    }
}
