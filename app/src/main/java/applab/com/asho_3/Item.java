package applab.com.asho_3;


/** * Created by amit rawat on 11/27/2017. */public class Item {
    private String name;
    private int photo;
    public Item(String name, int photo) {
        this.name = name;
        this.photo = photo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPhoto() {
        return photo;
    }
    public void setPhoto(int photo) {
        this.photo = photo;
    }
}