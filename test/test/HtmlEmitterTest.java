package test;

import fscode.FSCode;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

/**
 *
 * @author cmiller
 */
public class HtmlEmitterTest {

    public HtmlEmitterTest() {
    }

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

	@Test
	public void outputTestText() {
		FSCode testCode = null;
		try {
			testCode = new FSCode(new File("FSCode Test Text"));
		} catch (SAXException ex) {
			Logger.getLogger(HtmlEmitterTest.class.getName()).log(Level.SEVERE,
					"There was an error parsing the XML", ex);
		}
		System.out.println(testCode.parse().emitHtml());
	}

}