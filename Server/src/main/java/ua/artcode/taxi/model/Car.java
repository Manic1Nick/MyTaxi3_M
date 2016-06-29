package ua.artcode.taxi.model;

public class Car {

    private long id;
    String type;
    String model;
    String number;

    public Car() {

    }

    public Car(String type, String model, String number) {
        this.number = number;
        this.model = model;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
