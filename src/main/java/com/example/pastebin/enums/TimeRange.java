package com.example.pastebin.enums;

public enum TimeRange {
    TEN_MINUTES(10L),
    ONE_HOUR(60L),
    THREE_HOUR(180L),
    ONE_DAY(1_440L),
    ONE_WEEK(10_080L),
    ONE_MONTH(44_640L),
    NO_TIME_LIMIT(null);
    private final Long time;

    TimeRange(Long time) {
        this.time = time;
    }

    public Long getTime() {
        return time;
    }
}
