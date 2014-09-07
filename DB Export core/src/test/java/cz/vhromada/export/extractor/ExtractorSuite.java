package cz.vhromada.export.extractor;

import cz.vhromada.export.extractor.impl.ImplSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * A class represents test suite for package cz.vhromada.export.extractor.
 *
 * @author Vladimir Hromada
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ExtractorTest.class, ImplSuite.class })
public class ExtractorSuite {
}
