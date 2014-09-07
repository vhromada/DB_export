package cz.vhromada.export.extractor.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import cz.vhromada.export.api.entities.ExtractData;
import cz.vhromada.export.api.exceptions.ExportException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * A class represents test for class {@link H2Extractor}.
 *
 * @author Vladimir Hromada
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:h2Context.xml")
public class H2ExtractorTest {

	/** Data source */
	@Autowired
	private DataSource dataSource;

	/** Test method for {@link H2Extractor#H2Extractor(Connection)} with null connection. */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithNullConnection() {
		new H2Extractor(null);
	}

	/**
	 * Test method for {@link DerbyExtractor#extract()}.
	 *
	 * @throws SQLException    if getting connection failed
	 * @throws ExportException if extracting data failed
	 */
	@Test
	public void testExtract() throws SQLException, ExportException {
		try (final Connection connection = dataSource.getConnection()) {
			final ExtractData expectedExtractData = ExtractorTestUtils.getExpectedExtractData();
			final ExtractData actualExtractData = new H2Extractor(connection).extract();
			ExtractorTestUtils.assertExtractDataDeepEquals(expectedExtractData, actualExtractData);
		}
	}

}
