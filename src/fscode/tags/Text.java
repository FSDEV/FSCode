package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.Node;

/**
 * Childless text node used to emit simple textual elements.
 * 
 * @author cmiller
 * @since 0.1
 */
public class Text extends Emitter implements HtmlEmitter {

	/**
	 * HTML character replacement table.  Used to replace potentially dangerous
	 * or annoying HTML characters/entities with the user-intended/safer
	 * versions of those entities.
	 *
	 * Do not access this directly, use <code>getHtmlReplacementTable()</code>
	 *
	 * @see #getHtmlReplacementTable() 
	 */
	private static HashMap<String, String> htmlReplacementTable;

	/**
	 * Creates a new text node with the supplied node and parent
	 * <code>Emitter</code>.
	 *
	 * @since 0.1
	 */
	public Text(Emitter parent, Node contents) {
		super(parent, contents);
	}
	
	public StringBuilder emitHtml() {
		String toReturn = contents.getNodeValue();

		for(String key:getHtmlReplacementTable().keySet()) {
			toReturn = toReturn.replaceAll(key, getHtmlReplacementTable().get(key));
		}

		return new StringBuilder(toReturn);
	}

	@Override
	public List<Emitter> getChildren() {
		// the text tag is a filler - it can't support any kind of child
		// node
		return new LinkedList<Emitter>();
	}
	@Override
	public void setChildren(List<Emitter> children) {
		// just do nothing
	}
	@Override
	public void appendChild(Emitter child) {
		// just do nothing
	}

	private static HashMap<String, String> getHtmlReplacementTable() {
		if(htmlReplacementTable==null) {
			htmlReplacementTable = new HashMap<String, String>();
			htmlReplacementTable.put("  ", "&nbsp;");
			htmlReplacementTable.put("([\\r\\n][\\s]?){2,}", "<p/>");
		}
		return htmlReplacementTable;
	}

}
