package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import org.w3c.dom.Node;

/**
 * Allows users to superscript text.
 * 
 * @author cmiller
 * @since 0.1
 */
public class Super extends Emitter implements HtmlEmitter {

	public Super(Emitter parent, Node contents) {
		super(parent, contents);
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		emission.append("<super>");
		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}
		emission.append("</super>");

		return emission;
	}

}
