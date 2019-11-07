package cources.example.com.couresmanger.Models;

/**
 * Created by Google       Company on 01/03/2018..
 */

public class ItemPoster {
    String images;
    String name;

    public ItemPoster(String images, String name) {
        this.images = images;
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public String getname() {
        return name;
    }
}