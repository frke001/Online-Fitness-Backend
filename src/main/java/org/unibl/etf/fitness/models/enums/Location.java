package org.unibl.etf.fitness.models.enums;

public enum Location {

    ONLINE("Online"),
    GYM("Gym"),
    PARK("Park");

    private final String location;

    Location(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString()
    {
        return this.location;
    }

    public static Location getByLocation(String location)
    {
        for(var el : Location.values())
            if(el.location.equals(location))
                return el;
        throw new IllegalArgumentException();
    }
}
