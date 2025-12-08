package com.artlighter.glucosecontrolservice.diary;

import java.util.Date;

public record DiaryEntryDTO(double measurement, Date date, String notes, String username) {
}
