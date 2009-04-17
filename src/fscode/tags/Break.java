package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Provides for the insertion of a manual break into the document.
 *
 * @author cmiller
 * @since 0.1
 */
public class Break extends Emitter implements HtmlEmitter {

	private boolean objectBreak = false;

	public Break(Emitter parent, Node contents) {
		super(parent, contents);
	}

	@Override
	public Emitter parse() {
		NamedNodeMap nnm = contents.getAttributes();
		Node n;
		for(int i=0;i!=nnm.getLength();++i) {
			n = nnm.item(i);
			if(n.getNodeName().equalsIgnoreCase("wrap"))
				if(n.getTextContent().equalsIgnoreCase("object"))
					objectBreak = true;
		}

		return super.parse();
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		emission.append("<br");
		if(objectBreak)
			emission.append(" clear=\"all\"");
		emission.append("/>");

		return emission;
	}

}
