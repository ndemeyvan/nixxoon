package cm.studio.devbee.communitymarket.utilForChat;

public class ModelChat {
    long temps;
    String expediteur;
    String recepteur;
    String message;
    private String status;

    public ModelChat() {
    }

    public ModelChat(long temps, String expediteur, String recepteur, String message, String status) {
        this.temps = temps;
        this.expediteur = expediteur;
        this.recepteur = recepteur;
        this.message = message;
        this.status = status;
    }

    public long getTemps() {
        return temps;
    }

    public void setTemps(long temps) {
        this.temps = temps;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
