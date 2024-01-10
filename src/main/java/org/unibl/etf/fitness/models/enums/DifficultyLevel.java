package org.unibl.etf.fitness.models.enums;

public enum DifficultyLevel {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    EXPERT("Expert");

    private final String level;

    DifficultyLevel(String level) {
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

    public static DifficultyLevel getByLevel(String level)
    {
        for(var el : DifficultyLevel.values())
            if(el.level.equals(level))
                return el;
        throw new IllegalArgumentException();
    }
}
