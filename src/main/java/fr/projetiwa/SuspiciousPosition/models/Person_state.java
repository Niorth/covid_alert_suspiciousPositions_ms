package fr.projetiwa.SuspiciousPosition.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity(name="person_state")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Person_state {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long person_state_id;

    @ManyToOne
    @JoinColumn(name = "person_id")
    @JsonIgnore
    private Person person;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private Covid_state covid_state;
    public long getPerson_state_id() {
        return person_state_id;
    }

    public void setPerson_state_id(long person_state_id) {
        this.person_state_id = person_state_id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Covid_state getCovid_state() {
        return covid_state;
    }

    public void setCovid_state(Covid_state covid_state) {
        this.covid_state = covid_state;
    }

}