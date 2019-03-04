package cm.studio.devbee.communitymarket.gridView_post;

public class ModelGridView {
    private String nom_du_produit;
    private  String image_du_produit;
    private String prix_du_produit;

    public ModelGridView() {
    }

    public ModelGridView(String nom_du_produit, String image_du_produit, String prix_du_produit) {
        this.nom_du_produit = nom_du_produit;
        this.image_du_produit = image_du_produit;
        this.prix_du_produit = prix_du_produit;
    }

    public String getNom_du_produit() {
        return nom_du_produit;
    }

    public void setNom_du_produit(String nom_du_produit) {
        this.nom_du_produit = nom_du_produit;
    }

    public String getImage_du_produit() {
        return image_du_produit;
    }

    public void setImage_du_produit(String image_du_produit) {
        this.image_du_produit = image_du_produit;
    }

    public String getPrix_du_produit() {
        return prix_du_produit;
    }

    public void setPrix_du_produit(String prix_du_produit) {
        this.prix_du_produit = prix_du_produit;
    }
}
