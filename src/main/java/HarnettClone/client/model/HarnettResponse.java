package HarnettClone.client.model;

import java.util.Arrays;

public class HarnettResponse {
    private Attributes.AttributesWrapper[] features;

    public Attributes.AttributesWrapper[] getFeatures() {
        return features;
    }

    public void setFeatures(Attributes.AttributesWrapper[] features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "HarnettResponse{" +
                "features=" + Arrays.toString(features) +
                '}';
    }
}
