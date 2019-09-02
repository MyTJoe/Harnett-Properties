package HarnettClone.db;

import HarnettClone.client.model.Attributes;
import com.google.gson.Gson;

import java.sql.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class PropertyDb {

    public void insertProperties(List<Attributes> attributes) throws SQLException {
        Connection connection = getConnection();
        try {
            for (Attributes attribute : attributes) {
                insertAttributes(connection, attribute);
            }
        } catch (Exception e) {
            if (connection != null) {
                connection.close();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private class PropertyValueJson {
        int id;
        int assessedValue;
        int priceDiff;
        String city;

        int getPriceDiff()  {
            return priceDiff;
        }
    }

    public void createPropertyGroups() throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            String sql = "select json_agg(json_build_object('id', id, 'assessedValue', assessed_value , 'priceDiff', sale_price-assessed_value, 'city', city)) as ids from properties where sale_price > 0 group by owner_name, sale_price, sale_date having count(id) > 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                PropertyValueJson[] valuesForGroup = new Gson().fromJson(results.getString(1), PropertyValueJson[].class);
                PropertyValueJson primary = findPrimaryProperty(valuesForGroup);
                markGroup(valuesForGroup, primary, connection);
            }
            statement.executeBatch();
        } catch (Exception e) {
            if (connection != null) {
                connection.close();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void markGroup(PropertyValueJson[] allInGroup, PropertyValueJson primary, Connection connection) throws SQLException {
        String updateSql = "update properties set is_group_primary = ?, grouped_properties = ?, group_assessed_value = ? where id = ?";
        PreparedStatement statement = connection.prepareStatement(updateSql);
        int groupTotalAssessed = Stream.of(allInGroup).mapToInt(i -> i.assessedValue).sum();
        for (PropertyValueJson property: allInGroup) {
            boolean isPrimary = primary == null ? false : property.id == primary.id;
            statement.setBoolean(1, isPrimary);
            statement.setArray(2, connection.createArrayOf("Integer", Stream.of(allInGroup).map(p -> p.id).toArray()));
            statement.setInt(3, groupTotalAssessed);
            statement.setInt(4, property.id);
            statement.addBatch();
        }
        statement.executeBatch();
    }

    private PropertyValueJson findPrimaryProperty(PropertyValueJson[] propertyGroup) {
        return Stream.of(propertyGroup).filter(p -> p.priceDiff > 0).min(Comparator.comparing(PropertyValueJson::getPriceDiff)).orElse(null);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/county_reports","devuser","password");
    }

    private void insertAttributes(Connection connection, Attributes attributes) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO properties(" +
                        "id, " +
                        "object_id, " +
                        "parcel_id, " +
                        "owner_name, " +
                        "address, " +
                        "sale_price, " +
                        "assessed_value, " +
                        "sale_date, " +
                        "zip_code, " +
                        "city, " +
                        "building_value," +
                        "square_footage," +
                        "price_per_foot," +
                        "longitude," +
                        "latitude," +
                        "neighborhood," +
                        "zoning," +
                        "zoning_raw) " +
                        "values (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        handleNullInt(statement,1,attributes.getOBJECTID());
        handleNullInt(statement,2,attributes.getParcelID());
        handleNullString(statement,3,attributes.getOwners());
        handleNullString(statement,4,attributes.getPhysicalAddress());
        handleNullInt(statement,5,attributes.getSalePrice());
        handleNullInt(statement,6,attributes.getTotalAssessedValue());

        if (attributes.getSaleMonth() == null || attributes.getSaleYear() == null) {
            statement.setNull(7, Types.DATE);
        } else {
            String saleDate = attributes.getSaleYear() + "-" + attributes.getSaleMonth() + "-01";
            statement.setDate(7, java.sql.Date.valueOf(saleDate));
        }

        handleNullInt(statement,8,attributes.getParZipCode());
        handleNullString(statement,9,attributes.getParCity());
        handleNullInt(statement,10,Integer.valueOf(Math.round(attributes.getParcelBuildingValue())));
        handleNullInt(statement,11,Integer.valueOf(Math.round(attributes.getTotalAcutalAreaHeated())));

        if (Integer.valueOf(Math.round(attributes.getParcelBuildingValue())) != null ||
                Integer.valueOf(Math.round(attributes.getParcelBuildingValue())) != 0) {
            if (Integer.valueOf(Math.round(attributes.getTotalAcutalAreaHeated())) != null ||
                    Integer.valueOf(Math.round(attributes.getTotalAcutalAreaHeated())) != 0) {

                double pricePerFoot =
                        ((double) Math.round(attributes.getParcelBuildingValue()
                                / attributes.getTotalAcutalAreaHeated()) * 100000d)
                                / 100000d;
                statement.setDouble(12, pricePerFoot);
            }
        } else {
            statement.setDouble(12,-1);
        }

        handleNullDouble(statement,13,attributes.getLongitude());
        handleNullDouble(statement,14,attributes.getLatitude());
        handleNullInt(statement,15,attributes.getNeighborhood());
        handleNullString(statement,16,attributes.getCleanZoning());
        handleNullString(statement,17,attributes.getZoning());
        statement.executeUpdate();
        statement.close();
    }
    private void handleNullInt(PreparedStatement statement, Integer i, Integer integer) throws SQLException{
        if (integer == null) {
            statement.setNull(i, Types.INTEGER);
        } else {
            statement.setInt(i,integer);
        }
    }

    private void handleNullString(PreparedStatement statement, Integer i, String string) throws SQLException {
        if (string == null) {
            statement.setNull(i, Types.VARCHAR);
        } else {
            statement.setString(i,string);
        }
    }

    private void handleNullDouble(PreparedStatement statement, Integer i, Double d) throws SQLException {
        if (d == null) {
            statement.setNull(i, Types.DOUBLE);
        } else {
            statement.setDouble(i, d);
        }
    }
}
