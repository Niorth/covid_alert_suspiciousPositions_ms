package fr.projetiwa.SuspiciousPosition.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;


@Entity(name="position")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Position extends SuperClassPosition {
    @ManyToMany(mappedBy = "position")
    @JsonIgnore // Pour ne pas produire des cycles
    private List<Person> persons;

    public List<Person> getPersons() {
        return persons;
    }
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

}