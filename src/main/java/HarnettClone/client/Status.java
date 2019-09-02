package HarnettClone.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Status {

    public int checkConnection() throws Exception{
        String countRecordsUrl =
                "http://gis.harnett.org/arcgis/rest/services/Tax/Parcels/MapServer/0/query?where=1%" +
                        "3D1&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=" +
                        "esriSpatialRelIntersects&relationParam=&outFields=OBJECTID+&returnGeometry=false&returnTrueCurves=" +
                        "false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=true&" +
                        "orderByFields=OBJECTID+desc&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false" +
                        "&gdbVersion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&f=pjson";

        URL url = new URL(countRecordsUrl);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(url).get("count");

        return node.intValue();
    }
}
