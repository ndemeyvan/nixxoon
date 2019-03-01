package cm.studio.devbee.communitymarket.utilForPost;

public class CategoriesModelPost {
    private String post_titre_categories;
    private int post_image_categories;

    public CategoriesModelPost(String post_titre_categories, int post_image_categories) {
        this.post_titre_categories = post_titre_categories;
        this.post_image_categories = post_image_categories;
    }

    public String getPost_titre_categories() {
        return post_titre_categories;
    }

    public void setPost_titre_categories(String post_titre_categories) {
        this.post_titre_categories = post_titre_categories;
    }

    public int getPost_image_categories() {
        return post_image_categories;
    }

    public void setPost_image_categories(int post_image_categories) {
        this.post_image_categories = post_image_categories;
    }
}
