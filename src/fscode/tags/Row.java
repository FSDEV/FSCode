package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import fscode.exception.NonfatalException;
import java.util.ResourceBundle;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Adds the ability to add rows to tables.  Kinda useful.
 * 
 * @author cmiller
 * @since 0.1
 */
public class Row extends Emitter implements HtmlEmitter {

	private int height = 0; // let HTML renderers size it

	public Row(Emitter parent, Node contents) {
		super(parent, contents);
	}

	@Override
	public Emitter parse() {
		// 1: parse row attributes
		NamedNodeMap nnm = contents.getAttributes();
		Node n = null;
		for(int i=0;i!=nnm.getLength();++i) {
			n = nnm.item(i);
			if(n.getNodeName().equalsIgnoreCase("height"))
				height = Integer.parseInt(n.getTextContent());
		}

		// 2: parse child cells
		return super.parse();
		
		// 3: emit error messages for misplaced tags
/*		for(Emitter em:getChildren()) {
			if(!(em instanceof Cell)) {
				getRootEmitter().appendProblem(
						new NonfatalException(this, ResourceBundle
						.getBundle((String)getConfig().get("lang"))
						.getString("TAGS_ROW_INVALID_CHILD_NODE"))
						);
			}
		}*/

		//return this;
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		// openining tag
		emission.append("<tr");
		if(height!=0) {
			emission.append(" height=\"");
			emission.append(height);
			emission.append("\"");
		}
		emission.append(">\n");

		// contents
		for(Emitter em:getChildren())
			if(em instanceof Cell
					&& em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());

		// closing tag
		emission.append("</tr>\n");

		return emission;
	}

}
