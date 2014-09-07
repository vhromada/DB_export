package cz.vhromada.export;

import cz.vhromada.export.extractor.ExtractorSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for all tests.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ExtractorSuite.class, AbstractExportTest.class })
public class ExportSuite {
}
