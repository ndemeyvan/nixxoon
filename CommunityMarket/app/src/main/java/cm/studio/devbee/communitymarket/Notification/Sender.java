package cm.studio.devbee.communitymarket.Notification;

public class Sender {
    public Data data;
    private String to;

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}
