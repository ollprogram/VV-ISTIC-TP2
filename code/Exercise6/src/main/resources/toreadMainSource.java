public class Vehicle {

    private int color;
    private String name;

    public Vehicle(int color, String name) {
        this.color = color;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String setName(String name){
        this.name = name;
    }

}
