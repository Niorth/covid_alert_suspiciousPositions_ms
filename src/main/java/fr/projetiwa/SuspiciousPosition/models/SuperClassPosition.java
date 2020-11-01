package fr.projetiwa.SuspiciousPosition.models;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class SuperClassPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long position_id;
    protected Float longitude;
    protected Date position_date;
    protected Float latitude;

    public long getPosition_id() {
        return position_id;
    }

    public void setPosition_id(long position_id) {
        this.position_id = position_id;
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

    public Date getPosition_date() {
        return position_date;
    }

    public void setPosition_date(Date position_date) {
        this.position_date = position_date;
    }
}