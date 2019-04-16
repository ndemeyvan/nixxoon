package cm.studio.devbee.communitymarket.gridView_post;

import cm.studio.devbee.communitymarket.utilsForNouveautes.PostId;

public class ModelGridView extends PostId {
    private String nom_du_produit;
    private  String image_du_produit;
    private String prix_du_produit;
    private String user_profil_image;
    private String utilisateur;
    private String categories;

    public ModelGridView() {
    }

    public ModelGridView(String nom_du_produit, String image_du_produit, String prix_du_produit, String user_profil_image, String utilisateur, String categories) {
        this.nom_du_produit = nom_du_produit;
        this.image_du_produit = image_du_produit;
        this.prix_du_produit = prix_du_produit;
        this.user_profil_image = user_profil_image;
        this.utilisateur = utilisateur;
        this.categories = categories;
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

    public String getUser_profil_image() {
        return user_profil_image;
    }

    public void setUser_profil_image(String user_profil_image) {
        this.user_profil_image = user_profil_image;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }
}
