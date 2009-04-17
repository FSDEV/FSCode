package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import org.w3c.dom.Node;

/**
 * Adds support for code-style text.
 *
 * @author cmiller
 * @since 0.1
 */
public class Code extends Emitter implements HtmlEmitter {

	public Code(Emitter parent, Node contents) {
		super(parent, contents);
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		emission.append("<code>");
		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}
		emission.append("</code>");

		return emission;
	}
	
	

}
