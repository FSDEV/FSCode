package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import org.w3c.dom.Node;

/**
 * Simple bold text tag.
 *
 * @author cmiller
 * @since 0.1
 */
public class Bold extends Emitter implements HtmlEmitter {

	public Bold(Emitter parent, Node contents) {
		super(parent, contents);
	}

	/**
	 * @since 0.1
	 */
	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		emission.append("<b>");
		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}
		emission.append("</b>");

		return emission;
	}

}
