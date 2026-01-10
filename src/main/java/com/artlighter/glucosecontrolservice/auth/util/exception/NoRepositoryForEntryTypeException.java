package com.artlighter.glucosecontrolservice.auth.util.exception;

import com.artlighter.glucosecontrolservice.diary.util.DiaryEntryType;

public class NoRepositoryForEntryTypeException extends RuntimeException {
    private String entryType;

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
