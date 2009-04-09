package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import org.w3c.dom.Node;

/**
 * Italicizes text.
 *
 * @author cmiller
 * @since 0.1
 */
public class Italic extends Emitter implements HtmlEmitter {

	public Italic(Emitter parent, Node contents) {
		super(parent, contents);
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		emission.append("<i>");
		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}
		emission.append("</i>");

		return emission;
	}

}
