package ua.artcode.taxi.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Address {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private long id;

    @Transient
    @Expose(serialize = false, deserialize = false)
    @OneToMany (mappedBy = "homeAddress", cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>(); //User mapping

    @Transient
    @Expose(serialize = false, deserialize = false)
    @OneToMany (mappedBy = "from", cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Order> from = new ArrayList<>(); //Order mapping

    @Transient
    @Expose(serialize = false, deserialize = false)
    @OneToMany (mappedBy = "to", cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Order> to = new ArrayList<>(); //Order mapping

    @Column
    private String country;
    @Column
    private String city;
    @Column
    private String street;
    @Column
    private String houseNum;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    // google api
    @Transient
    private double lat;
    @Transient
    private double lon;

    public Address(String country, String city, String street, String houseNum) {
        this.city = city;
        this.street = street;
        this.houseNum = houseNum;
        this.country = country;
    }

    public List<Order> getFrom() {
        return from;
    }

    public void setFrom(List<Order> from) {
        this.from = from;
    }

    public List<Order> getTo() {
        return to;
    }

    public void setTo(List<Order> to) {
        this.to = to;
    }

    public Address(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Address() {
    }

    public Address(String line){

        String[] address = line.split(" ");

        if (address.length >= 4) {
            this.country = address[0];
            this.city = address[1];
            this.street = address[2];
            this.houseNum = address[3];
        } else {
            this.country = line;
            this.city = "";
            this.street = "";
            this.houseNum = "";
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", houseNum='" + houseNum + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

    public String toLine() {
        return country + " " + city + " " + street + " " + houseNum;
    }


}
