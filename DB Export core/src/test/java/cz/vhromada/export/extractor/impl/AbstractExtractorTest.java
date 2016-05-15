package cz.vhromada.export.extractor.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.export.extractor.Extractor;
import cz.vhromada.export.test.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * An abstract class represents parent for tests of implementations of {@link Extractor}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractExtractorTest {

    /**
     * Data source
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Test method for constructor with null connection.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullConnection() {
        getExtractor(null);
    }

    /**
     * Test method for implementation of {@link Extractor#extract()}.
     *
     * @throws SQLException if getting connection failed
     */
    @Test
    public void testExtract() throws SQLException {
        try (final Connection connection = dataSource.getConnection()) {
            final ExtractData expectedExtractData = TestUtils.getExtractDataForExtract();

            final ExtractData actualExtractData = getExtractor(connection).extract();

            TestUtils.assertExtractDataDeepEquals(expectedExtractData, actualExtractData);
        }
    }

    /**
     * Returns implementation of {@link Extractor}.
     *
     * @param connection connection
     * @return implementation of {@link Extractor}
     */
    protected abstract Extractor getExtractor(final Connection connection);

}
