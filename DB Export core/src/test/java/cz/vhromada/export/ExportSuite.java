package cz.vhromada.export;

import cz.vhromada.export.extractor.ExtractorTest;
import cz.vhromada.export.extractor.impl.DerbyExtractorTest;
import cz.vhromada.export.extractor.impl.H2ExtractorTest;
import cz.vhromada.export.extractor.impl.HsqlExtractorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for all tests.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ AbstractExportTest.class, ExtractorTest.class, H2ExtractorTest.class, HsqlExtractorTest.class, DerbyExtractorTest.class })
public class ExportSuite {
}
