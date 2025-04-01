package views;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class IndexImageBean implements Serializable {
    private List<String> images;

    @PostConstruct
    public void init() {
        images = new ArrayList<>();
        images.add("image/hostelimg1.jpg");
        images.add("image/hostelimg2.jpg");
        images.add("image/hostelimg3.jpg");
    }

    public List<String> getImages() {
        return images;
    }
}
