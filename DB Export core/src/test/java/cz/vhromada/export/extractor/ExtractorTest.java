package cz.vhromada.export.extractor;

import java.sql.Connection;

import cz.vhromada.export.api.entities.ColumnType;
import org.junit.Test;

/**
 * A class represents test for class {@link Extractor}.
 *
 * @author Vladimir Hromada
 */
public class ExtractorTest {

	/** Test method for {@link Extractor#Extractor(Connection)} with null connection. */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWithNullConnection() {
		new ExtractorImpl(null);
	}

	/** A class represents mock implementation of {@link Extractor}. */
	private class ExtractorImpl extends Extractor {

		/**
		 * Creates a new instance of ExtractorImpl.
		 *
		 * @param connection connection
		 * @throws IllegalArgumentException if connection is null
		 */
		protected ExtractorImpl(final Connection connection) {
			super(connection);
		}

		@Override
		protected String getDatabaseTableSQL() {
			throw new UnsupportedOperationException("Mock operation");
		}

		@Override
		protected String getDatabaseColumnSQL() {
			throw new UnsupportedOperationException("Mock operation");
		}

		@Override
		protected ColumnType convertColumn(final String column) {
			throw new UnsupportedOperationException("Mock operation");
		}

	}

}
