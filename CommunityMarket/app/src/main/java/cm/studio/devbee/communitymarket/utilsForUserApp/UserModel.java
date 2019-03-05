package cm.studio.devbee.communitymarket.utilsForUserApp;

import cm.studio.devbee.communitymarket.utilsForNouveautes.PostId;

public class UserModel extends PostId{


    private String user_profil_image;
    private String user_prenom;


    public UserModel(){

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
}
