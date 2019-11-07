package cources.example.com.couresmanger.Models;

/**
 * Created by Google       Company on 05/03/2018.
 */

public class DetailItems {
    String name_course;
    String instractor_course;
    String price;
    String image_icon;
    String id;
    String id_user;

    public DetailItems(String id, String name_course, String instractor_course, String price, String image_icon, String id_user) {
        this.image_icon = image_icon;
        this.instractor_course = instractor_course;
        this.name_course = name_course;
        this.price = price;
        this.id = id;
        this.id_user = id_user;
    }

    public String getImage_icon() {
        return image_icon;
    }

    public String getInstractor_course() {
        return instractor_course;
    }

    public String getName_course() {
        return name_course;
    }

    public String getPrice() {
        return price;
    }

    public String getid() {
        return id;
    }

    public String getiduser() {
        return id_user;
    }


}

