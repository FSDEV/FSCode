package fscode;

import fscode.macro.TOCElement;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;

/**
 * This object generates output from FSCode.
 *
 * A number of settings may be sent to the parser via a Map.  The keys are
 * case sensitive, and the values case-sensetivity varies.  Valid options:
 *
 * <table border="1">
 *	<tr>
 *		<td><center><i>key</i></center></td>
 *		<td><center><i>value</i></center></td>
 *		<td><center><i>doc</i></center></td>
 *	</tr>
 *	<tr>
 *		<td><code>isWiki</code></td>
 *		<td><code>{YES|NO|TRUE|FALSE}</code></td>
 *		<td>
 *			Defines whether or not the parsed FSCode is part of a wiki
 *			on an Internet site.  This affects some link tags.  Defaults to
 *			<code>NO</code>.  This is case-sensitive.
 *		</td>
 *	</tr>
 *	<tr>
 *		<td><code>wikiBaseUrl</code></td>
 *		<td><i>valid URL</i></td>
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
 *			<code>NULL</code>.
 *		</td>
 *	</tr>
 * </table>
 *
 * @author cmiller
 */
public class FSCode implements Emitter, HtmlEmitter {

	private Emitter parent;
	// thinking that the macro will pro-actively inspect the tree,
	// instead of caching it for later
	//private LinkedList<TOCElement> tableOfContents;
	private LinkedList<Emitter> children;
	private Map<String, String> config;

	private FSCode() {
		parent = null;
		//tableOfContents = new LinkedList<TOCElement>();
		children = new LinkedList<Emitter>();
		config = new TreeMap<String, String>();
			config.put("isWiki", "NO");
	}

	public StringBuilder emitHtml() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/**
	 * Although this is the root document, it can be grafted onto other parts
	 * of the object tree, and as such this method will default to return
	 * <code>null</code> but might not.
	 *
	 * @since 0.1
	 */
	public Emitter getParent() {
		return parent;
	}

	/**
	 * Used to graft a whole document into another document - a crazy feat,
	 * yes, but useful.  Think of MoinMoin wiki's ability to inclue pages
	 * or parts of pages into other pages via a macro...  Yes, I am insane
	 * with power!
	 */
	public void setParent(Emitter parent) {
		throw new UnsupportedOperationException("Inappropriate use of this " +
				"function on this class.");
	}

	public List<Emitter> getChildren() {
		return children;
	}

	public void setChildren(List<Emitter> children) {
		if(children instanceof LinkedList) {
			this.children = (LinkedList<Emitter>)children;
		} else {
			this.children = new LinkedList<Emitter>(children);
		}
	}

	public void appendChild(Emitter child) {
		children.add(child);
	}

}
