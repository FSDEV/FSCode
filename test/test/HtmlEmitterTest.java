package test;

import fscode.FSCode;
import fscode.HtmlEmitter;
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
		System.out.println("REPLACEMENT TEST:::::::");
		System.out.println("foo\n\nbar\n".replaceAll("([\\r\\n][\\s]?){2,}", "<p/>"));
		System.out.println("\n\n\n\n\n");


		FSCode testCode = null;
		try {
			testCode = new FSCode(new File("fscode_test_text.fscode.xml"));
		} catch (SAXException ex) {
			Logger.getLogger(HtmlEmitterTest.class.getName()).log(Level.SEVERE,
					"There was an error parsing the XML", ex);
		}
		System.out.println("<html>\n" +
				"\t<head><title>test output</title>\n" +
				"\t</head>\n" +
				"\t<body>");
		System.out.println(((HtmlEmitter)testCode.parse()).emitHtml());
		System.out.println("\t</body>\n</html>");
	}

}