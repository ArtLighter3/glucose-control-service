package com.artlighter.glucosecontrolservice.diary.service;

import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.entity.PatientProfile;
import com.artlighter.glucosecontrolservice.diary.entity.entry.DiaryEntry;
import com.artlighter.glucosecontrolservice.diary.repository.*;
import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
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
     * Находит все записи дневника самоконтроля ВСЕХ типов в заданном периоде времени.
     * @param patientProfile профиль больного, не null;
     * @param from нижняя граница временного периода выборки по UTC+0;
     *             если null, то выберутся записи в течение недели до верхней границы to;
     * @param to верхняя граница временного периода выборки по UTC+0; если null, то верхней границей считается
     *           текущий момент времени;
     * @return список записей дневника всех типов; никогда не null;
     */
    @Transactional(readOnly = true)
    public List<DiaryEntry> getAllDiaryEntries(PatientProfile patientProfile, Instant from, Instant to) {
        return getDiaryEntriesOfType(null, patientProfile, from, to);
    }

    /**
     * Находит все записи дневника самоконтроля определенного типа в заданном периоде времени.
     * @param entryType тип записи дневника самоконтроля; если null, то выбираются записи ВСЕХ типов;
     * @param patientProfile профиль больного, не null;
     * @param from нижняя граница временного периода выборки по UTC+0;
     *             если null, то выберутся записи в течение недели до верхней границы to;
     * @param to верхняя граница временного периода выборки по UTC+0; если null, то верхней границей считается
     *           текущий момент времени;
     * @return список записей дневника соответствующего типа; никогда не null;
     */
    @Transactional(readOnly = true)
    public List<DiaryEntry> getDiaryEntriesOfType(DiaryEntryType entryType, PatientProfile patientProfile,
                                                  Instant from, Instant to) {
        //if (patientProfile == null) throw new IllegalArgumentException("PatientProfile cannot be null");
        if (to == null) to = Instant.now();
        if (from == null) from = to.minus(Duration.ofDays(defaultDatePeriodInDays));

        List<DiaryEntry> entries = commonDiaryEntryDAO.getAllOfTypeBetweenDates(entryType,
                patientProfile, from, to, Sort.by("commitedAt").descending());

        if (entries == null) return Collections.emptyList();

        return entries;
//        return entries.stream().map((entry) -> (DiaryEntry) entry)
//                .collect(Collectors.toList());
    }

    /**
     * Находит последнюю по временной отметке запись дневника определенного типа.
     * @param entryType тип записи дневника самоконтроля; не null;
     * @param patientProfile профиль больного, не null;
     * @return Последнюю запись дневника этого типа; null, если не было найдено;
     */
    @Transactional(readOnly = true)
    public DiaryEntry findLastEntryOfType(DiaryEntryType entryType, PatientProfile patientProfile) {
        DiaryEntry entry = commonDiaryEntryDAO.findLastEntryOfType(entryType, patientProfile,
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
