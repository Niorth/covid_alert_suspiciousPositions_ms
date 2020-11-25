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

    /**
     * Override of equals : Comparison of two positions, location and temporality
     * Two positions are equal if:
     * - the latitude and longitude distance is less or equal than 10-4
     * - the position time is +30min -30min from the other position (same day)
     * @param obj
     * @return True if object are the same
     */
    @Override
    public boolean equals(Object obj) {
        SuspiciousPosition susposition = (SuspiciousPosition) obj;
        Float absLatitude = Math.abs(this.getLatitude() - susposition.getLatitude());
        Float absLongitude = Math.abs(this.getLongitude() - susposition.getLongitude());
        Double precision = 0.0001;

        int THIRTY_MINUTES = 30 * 60 * 1000;
        Long borneInf = susposition.getPositionDate().getTime() - THIRTY_MINUTES;
        Long borneSup = susposition.getPositionDate().getTime() + THIRTY_MINUTES;
        Long timeRef = this.getPositionDate().getTime();

        if(precision>=absLatitude && precision>=absLongitude){ //Check the position
            if((borneInf < timeRef) && (timeRef < borneSup)){ //Check the timestamp
                return true;
            }
        }
        return super.equals(obj);
    }
}