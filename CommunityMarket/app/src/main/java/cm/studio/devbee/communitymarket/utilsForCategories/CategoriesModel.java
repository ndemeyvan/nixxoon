package cm.studio.devbee.communitymarket.utilsForCategories;

public class CategoriesModel {
    private String titre_categories;
    private int image_categories;

    public CategoriesModel(String titre_categories, int image_categories) {
        this.titre_categories = titre_categories;
        this.image_categories = image_categories;
    }

    public String getTitre_categories() {
        return titre_categories;
    }

    public void setTitre_categories(String titre_categories) {
        this.titre_categories = titre_categories;
    }

    public int getImage_categories() {
        return image_categories;
    }

    public void setImage_categories(int image_categories) {
        this.image_categories = image_categories;
    }
}
