package cm.studio.devbee.communitymarket.utilForChat;

public class DiplayAllChat {
    String temps;
    String id_expediteur;
    String id_recepteur;
    String dernier_message;
    String image_profil;
    String nom_utilisateur;
    String tempsMilli;
    String lu;




    public DiplayAllChat() {
    }

    public DiplayAllChat(String temps, String id_expediteur, String id_recepteur, String dernier_message, String image_profil, String nom_utilisateur, String tempsMilli, String lu) {
        this.temps = temps;
        this.id_expediteur = id_expediteur;
        this.id_recepteur = id_recepteur;
        this.dernier_message = dernier_message;
        this.image_profil = image_profil;
        this.nom_utilisateur = nom_utilisateur;
        this.tempsMilli = tempsMilli;
        this.lu = lu;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public String getId_expediteur() {
        return id_expediteur;
    }

    public void setId_expediteur(String id_expediteur) {
        this.id_expediteur = id_expediteur;
    }

    public String getId_recepteur() {
        return id_recepteur;
    }

    public void setId_recepteur(String id_recepteur) {
        this.id_recepteur = id_recepteur;
    }

    public String getDernier_message() {
        return dernier_message;
    }

    public void setDernier_message(String dernier_message) {
        this.dernier_message = dernier_message;
    }

    public String getImage_profil() {
        return image_profil;
    }

    public void setImage_profil(String image_profil) {
        this.image_profil = image_profil;
    }

    public String getNom_utilisateur() {
        return nom_utilisateur;
    }

    public void setNom_utilisateur(String nom_utilisateur) {
        this.nom_utilisateur = nom_utilisateur;
    }

    public String getTempsMilli() {
        return tempsMilli;
    }

    public void setTempsMilli(String tempsMilli) {
        this.tempsMilli = tempsMilli;
    }

    public String getLu() {
        return lu;
    }

    public void setLu(String lu) {
        this.lu = lu;
    }
}
