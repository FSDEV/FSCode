package fscode;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.TreeMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
 *	<tr>
 *		<td colspan="4"><center><i>wiki-related options</i></center></td>
 *	</tr>
 *	<tr>
 *		<td><code>isWiki</code></td>
 *		<td><code>java.lang.String</code></td>
 *		<td><code>{YES|NO|TRUE|FALSE}</code></td>
 *		<td>
 *			Defines whether or not the parsed FSCode is part of a wiki
 *			on an Internet site.  This affects some link tags.  Defaults to
 *			<code>NO</code>.  This is case-sensitive.</td>
 *	</tr>
 *	<tr>
 *		<td><code>wikiBaseUrl</code></td>
 *		<td><code>java.lang.String</code></td>
 *		<td><i><center>valid URL</center></i></td>
 *		<td>
 *			The base URL for a wiki, such as
 *			<code>http://en.wikipedia.org/wiki/</code>.  The URL should be
 *			whatever is required to append the name of a wiki page, so you
 *			should pay attention to trailing slashes.  This is not
 *			case-sensitive, and is not Java-parsed, so it can fail quietly.
 *			<p/>
 *			This setting is ignored if <code>isWiki</code> is
 *			<code>{NULL|NO|FALSE}</code>, and is generally ignored if the
 *			<code>HtmlEmitter</code> is not used.  Defaults to
 *			<code>NULL</code>.</td>
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
	 * Configuration mapping for this node.
	 */
	private Map<String, Object> config;

	/**
	 * Default constructor nulls out or empty-initializes everything.
	 *
	 * @since 0.1
	 */
	private FSCode() {
		super(null, null);
		config = new TreeMap<String, Object>();
			config.put("isWiki", "NO");
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
					new StringReader(code)));
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
			contents = getDocBuilder().parse(f);
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

	/**
	 * @since 0.1
	 */
	public StringBuilder emitHtml() {
		throw new UnsupportedOperationException("Not supported yet.");
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
