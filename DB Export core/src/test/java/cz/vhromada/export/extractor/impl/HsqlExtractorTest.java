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
 * A class represents test for class {@link HsqlExtractor}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:hsqlContext.xml")
public class HsqlExtractorTest {

    /**
     * Data source
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Test method for {@link HsqlExtractor#HsqlExtractor(Connection)} with null connection.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConnection() {
        new HsqlExtractor(null);
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
            final ExtractData actualExtractData = new HsqlExtractor(connection).extract();
            DeepAsserts.assertNotNull(actualExtractData);
            DeepAsserts.assertEquals(expectedExtractData, actualExtractData);
        }
    }

}
