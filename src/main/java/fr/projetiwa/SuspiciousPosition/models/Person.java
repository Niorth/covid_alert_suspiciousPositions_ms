package fr.projetiwa.SuspiciousPosition.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity(name="person")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long person_id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private String password;

    @ManyToMany
    @JoinTable(name="person_position",
            joinColumns = @JoinColumn(name="person_id"),
            inverseJoinColumns = @JoinColumn(name="position_id"))
    private List<Position> position;

    @OneToMany(mappedBy = "person")
    private List<Person_state> person_states;

    public long getPerson_id() {
        return person_id;
    }

    public void setPerson_id(long person_id) {
        this.person_id = person_id;
    }

    public List<Position> getPosition() {
        return position;
    }

    public void setPosition(List<Position> position) {
        this.position = position;
    }

    public List<Person_state> getPerson_states() {
        return person_states;
    }

    public void setPerson_states(List<Person_state> person_states) {
        this.person_states = person_states;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
