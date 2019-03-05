package cm.studio.devbee.communitymarket.utilsForUserApp;

import cm.studio.devbee.communitymarket.utilsForNouveautes.PostId;

public class CategoriesModelChaussure extends PostId{


    private String user_image;
    private String utilisateur;


    public CategoriesModelChaussure(){

    }

    public CategoriesModelChaussure(String user_image, String utilisateur) {
        this.user_image = user_image;
        this.utilisateur = utilisateur;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }
}
