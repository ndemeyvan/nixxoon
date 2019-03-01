package cm.studio.devbee.communitymarket.utilsForNouveautes;

import com.google.firebase.firestore.Exclude;

public class PostId {
    @Exclude
    public String PostId;
    public <T extends PostId> T withId(final String id ){
        this.PostId=id;
        return (T) this;
    }
}
