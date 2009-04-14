package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import org.w3c.dom.Node;

/**
 * Allows users to subscript text.
 *
 * @author cmiller
 * @since 0.1
 */
public class Sub extends Emitter implements HtmlEmitter {

	public Sub(Emitter parent, Node contents) {
		super(parent, contents);
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		emission.append("<sub>");
		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}
		emission.append("</sub>");

		return emission;
	}

}
