package cz.vhromada.export.extractor.impl;

import java.sql.Connection;

import cz.vhromada.export.extractor.Extractor;

import org.springframework.test.context.ContextConfiguration;

/**
 * A class represents test for class {@link HsqlExtractor}.
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(locations = "classpath:hsqlContext.xml")
public class HsqlExtractorTest extends AbstractExtractorTest {

    @Override
    protected Extractor getExtractor(final Connection connection) {
        return new HsqlExtractor(connection);
    }

}
