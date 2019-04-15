package cm.studio.devbee.communitymarket.utilsForUserApp;

import cm.studio.devbee.communitymarket.utilsForNouveautes.PostId;

public class UserModel extends PostId{


    private String user_profil_image;
    private String user_prenom;
    private String id_utilisateur;
    private String status;
    private String search;


    public UserModel(){

    }

    public UserModel(String user_profil_image, String user_prenom, String id_utilisateur, String status, String search) {
        this.user_profil_image = user_profil_image;
        this.user_prenom = user_prenom;
        this.id_utilisateur = id_utilisateur;
        this.status = status;
        this.search = search;
    }

    public String getUser_profil_image() {
        return user_profil_image;
    }

    public void setUser_profil_image(String user_profil_image) {
        this.user_profil_image = user_profil_image;
    }

    public String getUser_prenom() {
        return user_prenom;
    }

    public void setUser_prenom(String user_prenom) {
        this.user_prenom = user_prenom;
    }

    public String getId_utilisateur() {
        return id_utilisateur;
    }

    public void setId_utilisateur(String id_utilisateur) {
        this.id_utilisateur = id_utilisateur;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
