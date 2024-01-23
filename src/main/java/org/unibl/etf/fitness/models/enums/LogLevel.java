package org.unibl.etf.fitness.models.enums;

public enum LogLevel {

    INFO("Info"),
    WARNING("Warning"),
    ERROR("Error");

    private final String level;

    LogLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public String toString()
    {
        return this.level;
    }
}
