package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Childless text node used to emit simple textual elements.
 * 
 * @author cmiller
 */
public class Text implements Emitter, HtmlEmitter {

	private String contents;

	private Emitter parent;

	private static HashMap<String, String> htmlReplacementTable;

	private Text() { }

	public Text(Emitter parent) {
		
	}
	
	public StringBuilder emitHtml() {

		return null;
	}

	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}

	public Emitter getParent() {
		return parent;
	}
	public void setParent(Emitter parent) {
		this.parent = parent;
	}

	public List<Emitter> getChildren() {
		// the text tag is a filler - it can't support any kind of child
		// node
		return new LinkedList<Emitter>();
	}
	public void setChildren(List<Emitter> children) {
		// just do nothing
	}
	public void appendChild(Emitter child) {
		// just do nothing
	}

	private static HashMap<String, String> getHtmlReplacementTable() {
		if(htmlReplacementTable==null) {
			htmlReplacementTable = new HashMap<String, String>();
			htmlReplacementTable.put("  ", "&nbsp;");
		}
		return htmlReplacementTable;
	}

}
