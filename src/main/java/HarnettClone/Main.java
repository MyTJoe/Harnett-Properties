package HarnettClone;

import HarnettClone.client.HarnettData;
import HarnettClone.client.HarnettRange;
import HarnettClone.client.Status;
import HarnettClone.client.model.Attributes;
import HarnettClone.db.PropertyDb;

import java.util.List;

/**
 * Hello world!
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {
        fetchProperties();
        new PropertyDb().createPropertyGroups();
    }

    private static void fetchProperties() throws Exception {
        Integer startingObjectID = getInitialObjectId();
        System.out.println("First objectID = " + startingObjectID);
        Integer totalRecords = getStatus();

        System.out.println("The query is starting with ObjectID " + startingObjectID);
        totalRecords += startingObjectID - 1;
        System.out.println(" There are " + totalRecords + " to obtain");
        System.out.println("-------");
        System.out.println();

        if (totalRecords > 0) {

            HarnettData hc = new HarnettData();
            PropertyDb propertyDb = new PropertyDb();

            while (startingObjectID < totalRecords) {
                List<Attributes> allAttributes;
                try {
                    System.out.println("querying starting with " + startingObjectID);
                    allAttributes = hc.getRecords(startingObjectID);
                    propertyDb.insertProperties(allAttributes);
                    System.out.println("success starting with " + startingObjectID);
                } catch (Exception e) {
                    throw e;
                }

                startingObjectID = allAttributes.get(allAttributes.size() - 1).getOBJECTID() + 1;

                if ( startingObjectID < totalRecords)
                    System.out.println(" Next query starts with " + startingObjectID);
                System.out.println("-------");
                System.out.println();
                Thread.sleep(500); // so the county doesn't think we're doing a dos attack
            }
        }
    }

    private static Integer getStatus() {
        Status status = new Status();
        Integer totalRecords = 0;
        try {
            totalRecords = status.checkConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalRecords;
    }

    private static Integer getInitialObjectId() {
        HarnettRange hr = new HarnettRange();
        Integer start = 0;
        try {
            start = hr.getFirstObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return start;
    }
}
