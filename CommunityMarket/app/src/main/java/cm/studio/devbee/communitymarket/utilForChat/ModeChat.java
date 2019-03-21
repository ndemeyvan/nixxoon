package cm.studio.devbee.communitymarket.utilForChat;

import cm.studio.devbee.communitymarket.utilsForNouveautes.PostId;

public class ModeChat{

    String expediteur;
    String recepteur;
    String message;

    public ModeChat() {
    }

    public ModeChat(String message) {
        this.message = message;
    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }

    public String getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(String recepteur) {
        this.recepteur = recepteur;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
