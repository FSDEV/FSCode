package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Adds support for the addition of cells into tables.  Kind of important.
 *
 * @author cmiller
 * @since 0.1
 */
public class Cell extends Emitter implements HtmlEmitter {

	private int rowspan = 1;

	private int colspan = 1;

	public Cell(Emitter parent, Node contents) {
		super(parent, contents);
	}

	@Override
	public Emitter parse() {
		// 1: parse attributes
		NamedNodeMap nnm = contents.getAttributes();
		Node n = null;
		for(int i=0;i!=nnm.getLength();++i) {
			n = nnm.item(i);
			if(n.getNodeName().equalsIgnoreCase("rowspan"))
				rowspan = Integer.parseInt(n.getTextContent());
			else if(n.getNodeName().equalsIgnoreCase("colspan"))
				colspan = Integer.parseInt(n.getTextContent());
		}

		// 2: parse children
		return super.parse();

		//return this;
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		// opening tag
		emission.append("<td");
		if(rowspan!=1) {
			emission.append(" rowspan=\"");
			emission.append(rowspan);
			emission.append("\"");
		}
		if(colspan!=1) {
			emission.append(" colspan=\"");
			emission.append(colspan);
			emission.append("\"");
		}
		emission.append(">\n");

		// contents
		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}

		// closing tag
		emission.append("</td>\n");

		return emission;
	}

}
