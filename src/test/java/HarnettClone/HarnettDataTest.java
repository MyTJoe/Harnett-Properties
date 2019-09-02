package HarnettClone;

import static org.junit.Assert.assertTrue;

import HarnettClone.client.HarnettData;
import HarnettClone.client.model.Attributes;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class HarnettDataTest {

    private Attributes attributes = new Attributes();

    @Before
    public void before() {
        attributes = new Attributes();
    }

    @Test
    public void testCleanZoning() {
        HarnettData harnettData = new HarnettData();

        attributes.setZoning("COMMERCIAL - 0.0 acres (0.0%), RA-20M - 0.27 acres (100.0%)");
        Assert.assertEquals("RA-20", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("COMMERCIAL - 0.3 acres (0.0%), RA-20M - 0.27 acres (100.0%)");
        Assert.assertEquals("COMMERCIAL", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("COMMERCIAL - 0.3 acres (0.0%), RA-20M - 0.57 acres (100.0%)");
        Assert.assertEquals("RA-20", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("Residential Single-Family - 0.42 acres (100.0%)");
        Assert.assertEquals("RESIDENTIAL SINGLE-FAMILY", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("COMMERCIAL - 0.0 acres (0.0%), RA-20M - 0.27 acres (100.0%)");
        Assert.assertEquals("RA-20", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("Single-Family - 0.1");
        Assert.assertEquals("SINGLE-FAMILY", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("RA-20 - 0.0");
        Assert.assertEquals("RA-20", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("Central Business - 0.04 acres (99.99%)");
        Assert.assertEquals("CENTRAL BUSINESS", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("Office/Service - 0.24 acres (100.0%)");
        Assert.assertEquals("OFFICE/SERVICE", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("Light Industrial - 0.65 acres (100.0%)");
        Assert.assertEquals("LIGHT INDUSTRIAL", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("INDUSTRIAL - 3.61 acres (100.0%)");
        Assert.assertEquals("INDUSTRIAL", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("Mixed Use - 0.1");
        Assert.assertEquals("MIXED USE", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("RA-30 - 0.0 acres (0.0%), RA-30 RESIDENTIAL AGRICULTURAL - 3.2 acres (100.0%)");
        Assert.assertEquals("RA-30 RESIDENTIAL AGRICULTURAL", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("OFFICE-INSTITUTIONAL - 0.0 acres (100.0%)");
        Assert.assertEquals("OFFICE-INSTITUTIONAL", harnettData.cleanZoning(attributes).getCleanZoning());

        attributes.setZoning("OPEN SPACE & RECREATION - 1.14 acres (100.0%)");
        Assert.assertEquals("OPEN SPACE & RECREATION", harnettData.cleanZoning(attributes).getCleanZoning());
    }

}
