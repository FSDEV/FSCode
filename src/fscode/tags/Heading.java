package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import fscode.macro.TOCElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Provides heading tag support for both named tags and the variable level
 * indentation tag.  This is a showcase of the flexibility of the tag
 * implementation system.
 *
 * @author cmiller
 * @since 0.1
 */
public class Heading extends Emitter implements HtmlEmitter, TOCElement {

	/**
	 * Used by the <code>HtmlEmitter</code> to format the output as well as
	 * by the TOC macro in order to determine the indentation level.
	 */
	private int level;

	/**
	 * The name used for printing and is also munged to create the anchor.
	 */
	private String name;

	public Heading(Emitter parent, Node contents) {
		super(parent, contents);
	}

	/**
	 * @since 0.1
	 */
	@Override
	public Heading parse() {
		if(contents.getNodeName().equalsIgnoreCase("h")) {
			// detect the level attribute
			Node n = contents.getAttributes().getNamedItem("level");
			if(n != null)
				level=Integer.parseInt(n.getTextContent());
			// treat the child nodes, which should be one text node
		} else
			// detect which tag level this is (h1, h2, h3, etc)
			if(contents.getNodeName().equalsIgnoreCase("h1"))
				level = 1;
			else if(contents.getNodeName().equalsIgnoreCase("h2"))
				level = 2;
			else if(contents.getNodeName().equalsIgnoreCase("h3"))
				level = 3;
			else if(contents.getNodeName().equalsIgnoreCase("h4"))
				level = 4;
			else if(contents.getNodeName().equalsIgnoreCase("h5"))
				level = 5;
			else if(contents.getNodeName().equalsIgnoreCase("h6"))
				level = 6;
			else
				level = -1;
		// treat the child nodes, which should be one text node
		NodeList nl = contents.getChildNodes();
		if(nl.getLength()>0) {
			Node n;
			for(int i=0;i<nl.getLength();++i) {
				n = nl.item(i);
				if(n.getNodeType()==Node.TEXT_NODE) {
					name = n.getNodeValue();
					break;
				}
			}
		} else
			name = "Untitled";
		return this;
	}

	/**
	 * @since 0.1
	 */
	public StringBuilder emitHtml() {
		String openingTag = null, closingTag = null;
		switch(level) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				openingTag = "<h" + level + " id=\"" + getHtmlAnchor() + "\">";
				closingTag = "</h" + level + ">";
				break;
			default:
				// nonstandard heading, just use a bold tag
				openingTag = "<b><a name=\"" + getHtmlAnchor() + "\">";
				closingTag = "</a></b>";
				break;
		}

		StringBuilder emission = new StringBuilder();

		emission.append(openingTag);
		emission.append(getName());
		emission.append(closingTag);

		return emission;
	}

	/**
	 * @since 0.1
	 */
	public String getName() {
		return name;
	}

	/**
	 * @since 0.1
	 */
	public String getHtmlAnchor() {
		return "#"+getName().replace(" ", "-");
	}

	/**
	 * @since 0.1
	 */
	public int getIndentLevel() {
		return level;
	}


}
