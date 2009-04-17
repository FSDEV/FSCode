package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import org.w3c.dom.Node;

/**
 * Centers text.
 *
 * @author cmiller
 * @since 0.1
 */
public class Center extends Emitter implements HtmlEmitter {

	public Center(Emitter parent, Node contents) {
		super(parent, contents);
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		emission.append("<center>");
		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}
		emission.append("</center>");

		return emission;
	}

}
