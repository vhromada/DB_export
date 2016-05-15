package cz.vhromada.export.extractor.impl;

import java.sql.Connection;

import cz.vhromada.export.extractor.Extractor;

import org.springframework.test.context.ContextConfiguration;

/**
 * A class represents test for class {@link H2Extractor}.
 *
 * @author Vladimir Hromada
 */
@ContextConfiguration(locations = "classpath:h2Context.xml")
public class H2ExtractorTest extends AbstractExtractorTest {

    @Override
    protected Extractor getExtractor(final Connection connection) {
        return new H2Extractor(connection);
    }

}
