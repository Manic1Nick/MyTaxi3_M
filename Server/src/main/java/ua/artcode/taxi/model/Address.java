package ua.artcode.taxi.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
public class Address {

    /*@OneToMany(mappedBy = "homeAddress", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "from", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    List<Order> ordersFrom = new ArrayList<>();

    @OneToMany(mappedBy = "to", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    List<Order> ordersTo = new ArrayList<>();*/

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "houseNum", nullable = false)
    private String houseNum;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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


    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Address) {
            return country.equals(((Address) obj).country) &&
                    city.equals(((Address) obj).city) &&
                    street.equals(((Address) obj).street) &&
                    houseNum.equals(((Address) obj).houseNum);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = country.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + street.hashCode();
        result = 31 * result + houseNum.hashCode();
        return result;
    }
}
