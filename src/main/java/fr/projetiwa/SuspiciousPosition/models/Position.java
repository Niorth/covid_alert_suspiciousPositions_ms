package fr.projetiwa.SuspiciousPosition.models;


import java.sql.Timestamp;

public class Position extends SuperClassPosition {

    private String userId;

    public Position(Float longitude, Timestamp positionDate, Float latitude, String userId) {
        super(longitude, positionDate, latitude);
        this.userId = userId;
    }

    public Position() { }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}