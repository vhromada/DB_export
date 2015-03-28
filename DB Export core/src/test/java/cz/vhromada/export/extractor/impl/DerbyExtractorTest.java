package cz.vhromada.export.extractor.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.test.DeepAsserts;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link DerbyExtractor}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:derbyContext.xml")
public class DerbyExtractorTest {

    /**
     * Data source
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Test method for {@link DerbyExtractor#DerbyExtractor(Connection)} with null connection.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConnection() {
        new DerbyExtractor(null);
    }

    /**
     * Test method for {@link DerbyExtractor#extract()}.
     *
     * @throws SQLException if getting connection failed
     */
    @Test
    public void testExtract() throws SQLException {
        try (final Connection connection = dataSource.getConnection()) {
            final ExtractData expectedExtractData = ExtractorTestUtils.getExpectedExtractData();
            final ExtractData actualExtractData = new DerbyExtractor(connection).extract();
            DeepAsserts.assertNotNull(actualExtractData);
            DeepAsserts.assertEquals(expectedExtractData, actualExtractData);
        }
    }

}
