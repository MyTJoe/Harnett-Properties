package HarnettClone.client;

import HarnettClone.client.model.Attributes;
import HarnettClone.client.model.HarnettResponse;
import com.google.gson.Gson;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HarnettData {

    private static final String[] COMPOUND_ZONINGS;
    private static final String[] SHORT_ZONINGS;
    private static final String pattern;

    static {
        COMPOUND_ZONINGS = new String[]{ "OPEN SPACE & RECREATION", "OFFICE-INSTITUTIONAL", "RA-30 RESIDENTIAL AGRICULTURAL", "Highway Business", "Central Business", "Light Industrial", "Office/Service", "HIGHWAY COMMERCIAL", "OFFICE & INSTITUTION", "SINGLE FAMILY", "Residential Agriculture", "Residential Single-Family", "Single-Family", "Rural District", "SHOPPING CENTER", "Mixed Use" };
        SHORT_ZONINGS = new String[]{ "BUSINESS",  "COMMERCIAL", "PARK", "CONSERVATION", "INDUSTRIAL", "MULTIFAMILY", "RESIDENTIAL", "RA-30", "RA-20", "RA-40" };
        pattern = "(" + String.join("|", Arrays.asList(ArrayUtils.addAll(COMPOUND_ZONINGS, SHORT_ZONINGS)).stream().map(z -> z.toUpperCase()).collect(Collectors.toList())) + ")|\\s[0-9]+(\\.[0-9][0-9]?)?";
    }

    public List<Attributes> getRecords(int startingObjectID) throws Exception {

        String gisURL =
                "http://gis.harnett.org/arcgis/rest/services/Tax/Parcels/MapServer/0/query?" +
                        "where=OBJECTID+>=+" + startingObjectID +
                        "&text=&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&" +
                        "spatialRel=esriSpatialRelIntersects&relationParam=&outFields=" +

                        "OBJECTID," +
                        "ParcelID," +
                        "Owners," +
                        "PhysicalAddress," +
                        "SaleMonth," +
                        "SaleYear," +
                        "SalePrice," +
                        "TotalAssessedValue," +
                        "ParZipCode," +
                        "ParCity," +
                        "Zoning," +
                        "ParcelBuildingValue," +
                        "TotalAcutalAreaHeated," +
                        "Longitude," +
                        "Latitude," +
                        "Neighborhood" +

                        "&returnGeometry=false&returnTrueCurves=false&maxAllowableOffset=&geometryPrecision=&ou" +
                        "tSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=objectid&grou" +
                        "pByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVers" +
                        "ion=&returnDistinctValues=false&resultOffset=&resultRecordCount=&f=pjson";

        //String gisURL = "https://s3.amazonaws.com/franklin-ironyard/harnett.json";

        URL url = new URL(gisURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        HarnettResponse parsedData = null;
        if (responseCode == 200) {
            InputStreamReader reader = new InputStreamReader(url.openStream());
            parsedData = new Gson().fromJson(reader, HarnettResponse.class);
        }

        return Arrays.asList(parsedData.getFeatures()).stream().map(attributesWrapper
                -> cleanZoning(attributesWrapper.getAttributes())).collect(Collectors.toList());
    }

    public Attributes cleanZoning(Attributes attributes) {
        if (StringUtils.isEmpty(attributes.getZoning())) {
            return attributes;
        }

        Map<Double, String> zoningAndValues = new TreeMap<>();
        String[] rawZonings = attributes.getZoning().split(",");
        for (String rawZoning: rawZonings) {
            List<String> matches = new ArrayList();
            Matcher m = Pattern.compile(pattern).matcher(rawZoning.toUpperCase());
            int matchCount = 0;
            while (m.find()) {
                matches.add(m.group());
                matchCount++;
            }
            if (matchCount == 2 && StringUtils.isNumeric(matches.get(1).trim().replace(".", ""))) {
                zoningAndValues.put(Double.parseDouble(matches.get(1).trim()), matches.get(0));
            }
        }

        if (!zoningAndValues.isEmpty()) {
            List<String> values = zoningAndValues.values().stream().collect(Collectors.toList());
            attributes.setCleanZoning(values.get(values.size() - 1));
        }
        return attributes;
    }
}
