package pixsor.app.huzykamz.pixoradmin;

/**
 * Created by HUZY_KAMZ on 10/2/2016.
 */
public class Blog {

    private String title;
    private String description;
    private String imageUrl;

    public Blog() {
      // This is left out for firebase , by Huxaiphaer
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
