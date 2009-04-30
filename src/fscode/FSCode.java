package fscode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.TreeMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This object generates output from FSCode.
 *
 * A number of settings may be sent to the parser via a Map.  The keys are
 * case sensitive, and the values are Java Objects.  Valid options:
 *
 * <table border="1">
 *	<tr>
 *		<td><center><i>key</i></center></td>
 *		<td><center><i>value type</i></center></td>
 *		<td><center><i>value</i></center></td>
 *		<td><center><i>doc</i></center></td>
 *	</tr>
 *  <tr>
 *		<td><code>lang</code></td>
 *		<td><code>java.lang.String</code></td>
 *		<td><i>path to a valid resource bundle</i></td>
 *		<td>The location of a set of strings in a resource bundle that will
 *			be used to emit error messages to the user.  Use this to relocalize
 *			to another language of your choice.  Defaults to
 *			<code>en_us</code></td>
 *  </tr>
 *	<tr>
 *		<td><code>tagset</code></td>
 *		<td><code>java.lang.String</code></td>
 *		<td><i>path to a valid resource bundle</i></td>
 *		<td>The location of a set of strings in a resource bundle that will
 *			be used to generate HTML.  This allows you to alter the HTML
 *			output on the fly.  Defaults to <code>default</code>.</td>
 *	</tr>
 *	<tr>
 *		<td colspan="4"><center><i>wiki-related options</i></center></td>
 *	</tr>
 *	<tr>
 *		<td><code>isWiki</code></td>
 *		<td><code>java.lang.String</code></td>
 *		<td><code>{YES|NO|TRUE|FALSE}</code></td>
 *		<td>Defines whether or not the parsed FSCode is part of a wiki
 *			on an Internet site.  This affects some link tags.  Defaults to
 *			<code>NO</code>.  This is case-sensitive.</td>
 *	</tr>
 *	<tr>
 *		<td><code>wikiProviders</code></td>
 *		<td><code>java.util.Map&lt;String, fscode.tag.WikiProvider&gt;</code></td>
 *		<td><i><center>list of wiki providers, mapped by the given name of the wiki</center></i></td>
 *		<td>A list of wikis that the FSCode parser will be aware of.  This
 *			affects the behavior of some tags.  The <code>WikiProvider</code>
 *			that represents the current wiki will be at "" in the map.</td>
 *	</tr>
 * </table>
 *
 * <br/>
 *
 * <b>TROUBLESHOOTING</b>:
 *
 * If you keep getting <code>SAXException</code>s then you may been to wrap the
 * input in <code>&lt;fscode&gt;&lt;/fscode&gt;</code> tags to make it
 * XML-compliant.  This kind of thing is more or less required for websites
 * where the average user can be more or less guaranteed to not care about
 * those tags.
 *
 * @author cmiller
 * @since 0.1
 */
public class FSCode extends Emitter implements HtmlEmitter {

	/**
	 * Static document builder so that we don't have massive garbage collection
	 * problems associated with tons of document builders lying around.
	 */
	private static DocumentBuilder docBuilder;

	/**
	 * Precompiled XPath query for accessing the child nodes of the root
	 * <code>fscode</code> tag.
	 */
	private static XPathExpression rootXPath;

	/**
	 * Default constructor nulls out or empty-initializes everything.
	 *
	 * @since 0.1
	 */
	private FSCode() {
		super(null, null);
		config = new TreeMap<String, Object>();
			config.put("isWiki", "NO");
			config.put("lang", "en_us");
			config.put("tagset", "default_tagset");
	}

	/**
	 * Creates a new FSCode parser from a <code>String</code>, but does
	 * <i>not</i> parse it yet.  Will throw various exceptions if the XML
	 * is malformed.
	 *
	 * @throws org.xml.sax.SAXException Thrown if the XML is malformed.
	 * @since 0.1
	 */
	public FSCode(String code) throws SAXException {
		this();
		try {
			contents = getDocBuilder().parse(new InputSource(
					new StringReader(code.replaceAll("&", "&amp;"))));
		} catch (IOException ex) {
			Logger.getLogger(FSCode.class.getName()).log(Level.SEVERE,
					"Since it's just a stupid iterator wrapper around" +
					"a String, this really shouldn't happen!", ex);
		}
	}

	/**
	 * Creates a new FSCode parser from a <code>StringBuilder</code>.
	 *
	 * @see FSCode#FSCode(java.lang.String)
	 * @throws org.xml.sax.SAXException Thrown if the XML is malformed.
	 * @since 0.1
	 */
	public FSCode(StringBuilder code) throws SAXException {
		this(code.toString());
	}

	/**
	 * Yet another constructor that supports reading the FSCode from a file.
	 *
	 * @throws org.xml.sax.SAXException If there is an error parsing the XML.
	 * @since 0.1
	 */
	public FSCode(File f) throws SAXException {
		this();
		try {
			StringBuffer sb = new StringBuffer();
			int tmp;
			FileInputStream in = new FileInputStream(f);
			while(in.available()>0) {
				tmp = in.read();
				if(tmp=='&')
					sb.append("&amp;");
				else
					sb.append(((char)tmp));

			}
			contents = getDocBuilder().parse(new InputSource(
					new StringReader(sb.toString())));
		} catch (IOException ex) {
			Logger.getLogger(FSCode.class.getName()).log(Level.SEVERE,
					"Reading from file failed.", ex);
		}
	}

	/**
	 * Creates a new FSCode parser from a <code>String</code> with the given
	 * configuration settings.  May throw an exception if the XML is
	 * malformed.
	 *
	 * @throws org.xml.sax.SAXException Thrown if the XML is malformed.
	 * @since 0.1
	 */
	public FSCode(String code, Map<String, Object> config) throws SAXException {
		this(code);
		this.config = config;
	}

	/**
	 * Creates a new FSCode parser from a <code>StringBuilder</code> with the
	 * given configuration settings.  May throw an exception if the XML is
	 * malformed.
	 *
	 * @throws org.xml.sax.SAXException Thrown if the XML is malformed.
	 * @since 0.1
	 */
	public FSCode(StringBuilder code, Map<String, Object> config)
			throws SAXException {
		this(code.toString(), config);
	}

	public FSCode(File f, Map<String, Object> config) throws SAXException {
		this(f);
		this.config = config;
	}

	/**
	 * Overriden in order to break out of the <code>fscode</code> tag.
	 */
	@Override
	public FSCode parse() {
		Node fcontents = null;
		try {
			fcontents = (Node)getRootXPath()
					.evaluate(contents, XPathConstants.NODE);
		} catch (XPathExpressionException ex) {
			Logger.getLogger(FSCode.class.getName()).log(Level.SEVERE,
					"There is no fscode tag in the input", ex);
			return null;
		}
		NodeList nl = fcontents.getChildNodes();

		for(int i = 0; i!=nl.getLength(); i++) {
			appendChild(Emitter.parse(this, nl.item(i)));
		}

		return this;
	}

	/**
	 * Get the precompiled XPath expression to find the fscode tag and get
	 * it.
	 */
	private static XPathExpression getRootXPath() {
		if(rootXPath == null) {
			try {
				rootXPath = getQueryFactory().newXPath().compile("//fscode");
			} catch (XPathExpressionException ex) {
				Logger.getLogger(FSCode.class.getName()).log(Level.SEVERE,
						"Apparently my query is bogus", ex);
			}
		}
		return rootXPath;
	}

	/**
	 * @since 0.1
	 */
	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();
		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}
		return emission;
	}

	/**
	 * Static-singleton accessor to the document builder to prevent it from
	 * being <code>null</code> when accessed.
	 */
	private static DocumentBuilder getDocBuilder() {
		if(docBuilder == null) {
			try {
				docBuilder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
			} catch (ParserConfigurationException ex) {
				Logger.getLogger(FSCode.class.getName())
						.log(Level.SEVERE, "Unable to create a new document" +
						"builder, which is strange and not supposed to happen",
						ex);
			}
		}
		return docBuilder;
	}

}
