package fr.projetiwa.SuspiciousPosition.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@MappedSuperclass
public class SuperClassPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long positionId;
    protected Float longitude;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    protected Timestamp positionDate;
    protected Float latitude;

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Timestamp getPositionDate() {
        return positionDate;
    }

    public void setPosition_date(Timestamp position_date) {
        this.positionDate = position_date;
    }

    public SuperClassPosition(Float longitude, Timestamp positionDate, Float latitude) {
        this.longitude = longitude;
        this.positionDate = positionDate;
        this.latitude = latitude;
    }
    public SuperClassPosition(){}

    @Override
    public boolean equals(Object obj) {
        SuspiciousPosition position = (SuspiciousPosition) obj;
        Float absLatitude = Math.abs(this.getLatitude() - position.getLatitude());
        Float absLongitude = Math.abs(this.getLongitude() - position.getLongitude());
        Double precision = 0.0001;
        if(precision>=absLatitude && precision>=absLongitude){
            return true;
        }
        return super.equals(obj);
    }
}