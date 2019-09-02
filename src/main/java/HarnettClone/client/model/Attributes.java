package HarnettClone.client.model;

public class Attributes {
    private Integer OBJECTID;
    private Integer ParcelID;
    private String Owners;
    private String PhysicalAddress;
    private Integer SaleMonth;
    private Integer SaleYear;
    private Integer SalePrice ;
    private Integer TotalAssessedValue;
    private Integer ParZipCode;
    private String ParCity;
    private float ParcelBuildingValue;
    private float TotalAcutalAreaHeated;
    private String Zoning;
    private String cleanZoning;
    private Double Longitude;
    private Double Latitude;
    private Integer Neighborhood;

    public Integer getOBJECTID() {
        return OBJECTID;
    }

    public Integer getParcelID() {
        return ParcelID;
    }

    public String getOwners() {
        return Owners;
    }

    public String getPhysicalAddress() {
        return PhysicalAddress;
    }

    public Integer getSaleMonth() {
        return SaleMonth;
    }

    public Integer getSaleYear() {
        return SaleYear;
    }

    public Integer getSalePrice() {
        return SalePrice;
    }

    public Integer getTotalAssessedValue() {
        return TotalAssessedValue;
    }

    public Integer getParZipCode() {
        return ParZipCode;
    }

    public String getParCity() {
        return ParCity;
    }

    public float getParcelBuildingValue() {
        return ParcelBuildingValue;
    }

    public float getTotalAcutalAreaHeated() {
        return TotalAcutalAreaHeated;
    }

    public String getZoning() {
        return Zoning;
    }

    public String getCleanZoning() {
        return cleanZoning;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Integer getNeighborhood() {
        return Neighborhood;
    }

    public void setCleanZoning(String cleanZoning) {
        this.cleanZoning = cleanZoning;
    }

    @Override
    public String toString() {
        return "Attributes{" +
                "OBJECTID=" + OBJECTID +
                ", ParcelID=" + ParcelID +
                ", Owners='" + Owners + '\'' +
                ", PhysicalAddress='" + PhysicalAddress + '\'' +
                ", SaleMonth=" + SaleMonth +
                ", SaleYear=" + SaleYear +
                ", SalePrice=" + SalePrice +
                ", TotalAssessedValue=" + TotalAssessedValue +
                ", ParZipCode=" + ParZipCode +
                ", ParCity='" + ParCity + '\'' +
                ", ParcelBuildingValue=" + ParcelBuildingValue +
                ", TotalAcutalAreaHeated=" + TotalAcutalAreaHeated +
                ", Zoning='" + Zoning + '\'' +
                ", cleanZoning='" + cleanZoning + '\'' +
                ", Longitude=" + Longitude +
                ", Latitude=" + Latitude +
                ", Neighborhood=" + Neighborhood +
                '}';
    }

    public class AttributesWrapper {

        private Attributes attributes;

        public Attributes getAttributes() {
            return attributes;
        }

        public void setAttributes(Attributes attributes) {
            this.attributes = attributes;
        }

        @Override
        public String toString() {
            return "AttributesWrapper{" +
                    "attributes=" + attributes +
                    '}';
        }
    }
}
