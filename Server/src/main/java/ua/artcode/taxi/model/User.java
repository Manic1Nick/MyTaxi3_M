package ua.artcode.taxi.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NamedQueries({@NamedQuery(name = "getAllUsers", query = "SELECT c FROM User c")})
public class User implements PassengerActive, DriverActive {

    @OneToMany(mappedBy = "passenger", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    List<Order> ordersPassenger = new ArrayList<>();

    @OneToMany(mappedBy = "driver", cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    List<Order> ordersDriver = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    private UserIdentifier identifier;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "pass", nullable = false)
    private String pass;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Address homeAddress;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Car car;

    /*@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Address homeAddress;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Car car;*/

    public User() {
    }

    //for passenger
    public User(UserIdentifier identifier, String phone, String pass, String name, Address homeAddress) {
        this.identifier = identifier;
        this.phone = phone;
        this.pass = pass;
        this.name = name;
        this.homeAddress = homeAddress;
    }

    //for driver
    public User(UserIdentifier identifier, String phone, String pass, String name, Car car) {
        this.identifier = identifier;
        this.phone = phone;
        this.pass = pass;
        this.name = name;
        this.car = car;
    }

    //for anonymous
    public User(UserIdentifier identifier, String phone, String name) {
        this.identifier = identifier;
        this.phone = phone;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public UserIdentifier getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(UserIdentifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getPass() {
        return pass;
    }

    @Override
    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Address getHomeAddress() {
        return homeAddress;
    }

    @Override
    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    @Override
    public Car getCar() {
        return car;
    }

    @Override
    public void setCar(Car car) {
        this.car = car;
    }

    public List<Order> getOrdersPassenger() {
        return ordersPassenger;
    }

    public void setOrdersPassenger(List<Order> ordersPassenger) {
        this.ordersPassenger = ordersPassenger;
    }

    public List<Order> getOrdersDriver() {
        return ordersDriver;
    }

    public void setOrdersDriver(List<Order> ordersDriver) {
        this.ordersDriver = ordersDriver;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identifier=" + identifier +
                ", phone='" + phone + '\'' +
                ", pass='" + pass + '\'' +
                ", name='" + name + '\'' +
                ", homeAddress=" + homeAddress +
                ", car=" + car +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof User) {

            if (((User)obj).identifier.equals(UserIdentifier.P)) {
                return  id == (((User)obj).id) &&
                        identifier.equals(((User)obj).identifier) &&
                        phone.equals(((User)obj).phone) &&
                        name.equals(((User)obj).name) &&
                        pass.equals(((User)obj).pass) &&
                        homeAddress.equals(((User)obj).homeAddress);

            } else if (((User)obj).identifier.equals(UserIdentifier.D)) {
                return  id == (((User)obj).id) &&
                        identifier.equals(((User)obj).identifier) &&
                        phone.equals(((User)obj).phone) &&
                        name.equals(((User)obj).name) &&
                        pass.equals(((User)obj).pass) &&
                        car.equals(((User)obj).car);
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = identifier.hashCode();
        result = 31 * result + phone.hashCode();
        result = 31 * result + pass.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + homeAddress.hashCode();
        result = 31 * result + car.hashCode();
        return result;
    }
}
