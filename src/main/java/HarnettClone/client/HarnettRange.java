package HarnettClone.client;

import HarnettClone.client.model.HarnettResponse;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.URL;

public class HarnettRange {

    private String urlFirstObjectId =
            "http://gis.harnett.org/arcgis/rest/services/Tax/Parcels/MapServer/0/query?where=" +
                    "1%3D1&text=&objectIds=&time=&geometry=&geometryType=esriGeometryE" +
                    "nvelope&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=objec" +
                    "tid&returnGeometry=false&returnTrueCurves=false&maxAllowableOffset=&geometryPrec" +
                    "ision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=objectid+a" +
                    "sc&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVer" +
                    "sion=&returnDistinctValues=false&resultOffset=&resultRecordCount=1&f=pjson";


    public int getFirstObjectId() throws Exception {
        URL url = new URL(urlFirstObjectId);
        HarnettResponse parsedData;
        InputStreamReader reader = new InputStreamReader(url.openStream());
        parsedData = new Gson().fromJson(reader, HarnettResponse.class);

        return parsedData.getFeatures()[0].getAttributes().getOBJECTID();
    }
}
