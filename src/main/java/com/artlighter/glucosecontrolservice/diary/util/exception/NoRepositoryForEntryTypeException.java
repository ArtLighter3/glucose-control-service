package com.artlighter.glucosecontrolservice.diary.util.exception;

/**
 * Исключение выбрасывается в случае, если при поиске репозитория для определенного типа записи дневника DiaryEntry
 * в объекте DiaryEntryRepositoryCollection репозиторий не был найден.
 * @see com.artlighter.glucosecontrolservice.diary.util.DiaryEntryRepositoryCollection
 */

public class NoRepositoryForEntryTypeException extends RuntimeException {
    private String entryType;

    /**
     *
     * @param entryType Строковое имя переданного типа записи дневника, для которого не было найдено репозитория
     * @param message Описание исключения
     */
    public NoRepositoryForEntryTypeException(String entryType, String message) {
        super(message);
        this.entryType = entryType;
    }

  public String getEntryType() {
    return entryType;
  }

  public void setEntryType(String entryType) {
    this.entryType = entryType;
  }
}
