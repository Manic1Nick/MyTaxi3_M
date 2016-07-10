package ua.artcode.taxi.model;

public interface PassengerActive {

    int getId();
    void setId(int id);

    UserIdentifier getIdentifier();
    void setIdentifier(UserIdentifier identifier);

    String getPhone();
    void setPhone(String phone);

    String getPass();
    void setPass(String pass);

    String getName();
    void setName(String name);

    Address getHomeAddress();
    void setHomeAddress(Address homeAddress);

    String toString();
}
