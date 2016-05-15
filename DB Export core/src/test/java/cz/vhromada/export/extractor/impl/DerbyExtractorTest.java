package cz.vhromada.export.extractor.impl;

import java.sql.Connection;

import cz.vhromada.export.extractor.Extractor;

import org.springframework.test.context.ContextConfiguration;

/**
 * A class represents test for class {@link DerbyExtractor}.
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(locations = "classpath:derbyContext.xml")
public class DerbyExtractorTest extends AbstractExtractorTest {

    @Override
    protected Extractor getExtractor(final Connection connection) {
        return new DerbyExtractor(connection);
    }
}
