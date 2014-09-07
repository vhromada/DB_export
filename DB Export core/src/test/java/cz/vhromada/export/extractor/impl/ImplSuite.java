package cz.vhromada.export.extractor.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.export.extractor.impl.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ H2ExtractorTest.class, HsqlExtractorTest.class, DerbyExtractorTest.class })
public class ImplSuite {
}
