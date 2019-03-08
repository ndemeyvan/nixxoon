package cm.studio.devbee.communitymarket.gridView_post;

import cm.studio.devbee.communitymarket.utilsForNouveautes.PostId;

public class ModelGridView extends cm.studio.devbee.communitymarket.utilsForNouveautes.PostId {
    private String nom_du_produit;
    private  String image_du_produit;
    private String prix_du_produit;
    private String image_profil;

    public ModelGridView() {
    }

    public ModelGridView(String nom_du_produit, String image_du_produit, String prix_du_produit, String image_profil) {
        this.nom_du_produit = nom_du_produit;
        this.image_du_produit = image_du_produit;
        this.prix_du_produit = prix_du_produit;
        this.image_profil = image_profil;
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

    public String getImage_profil() {
        return image_profil;
    }

    public void setImage_profil(String image_profil) {
        this.image_profil = image_profil;
    }
}
