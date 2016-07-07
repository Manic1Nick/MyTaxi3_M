package ua.artcode.taxi.model;





import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
public class User implements PassengerActive, DriverActive {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private int id;
    @Enumerated(EnumType.ORDINAL)
    private UserIdentifier identifier;
    @Column(nullable = false)
    private String phone;
    @Column
    private String pass;
    @Column(nullable = false)
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (referencedColumnName = "id")
    private Address homeAddress;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn (referencedColumnName = "id")
    private Car car;
    @Transient
    private List<Long> orderIds = new ArrayList<>();

    @OneToMany (mappedBy = "driver", cascade = CascadeType.ALL)
    private List<Order> drivers = new ArrayList<>(); //Order mapping
    @OneToMany (mappedBy = "passenger", cascade=CascadeType.ALL)
    private List<Order> passengers = new ArrayList<>(); //Order mapping

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


    public List<Order> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Order> drivers) {
        this.drivers = drivers;
    }

    public List<Order> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Order> passengers) {
        this.passengers = passengers;
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
    public List<Long> getOrderIds() {
        return orderIds;
    }

    @Override
    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    @Override
    public Car getCar() {
        return car;
    }

    @Override
    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identifier=" + identifier +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", homeAddress=" + homeAddress +
                ", car=" + car +
                ", orderIds=" + orderIds +
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

        //todo equals list ids
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + identifier.hashCode();
        result = 31 * result + phone.hashCode();
        return result;
    }
}
