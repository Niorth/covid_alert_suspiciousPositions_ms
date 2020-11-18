package fr.projetiwa.SuspiciousPosition.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


@Entity(name="suspiciousPosition")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class SuspiciousPosition extends SuperClassPosition {


    public SuspiciousPosition(Float longitude, Timestamp positionDate, Float latitude) {
        super(longitude, positionDate, latitude);

    }

    public SuspiciousPosition() {

    }

    @Override
    public String toString() {
        return "{" +
                "longitude:" + longitude +
                ", positionDate:" + positionDate +
                ", latitude:" + latitude +
                '}';
    }
}