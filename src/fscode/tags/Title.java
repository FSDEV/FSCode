package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import fscode.exception.NonfatalException;
import java.util.LinkedList;
import org.w3c.dom.Node;

/**
 * The title tag supports a document-level title.
 *
 * @author cmiller
 * @since 0.1
 */
public class Title extends Emitter implements HtmlEmitter {

	public Title(Emitter parent, Node contents) {
		super(parent, contents);
	}

	/**
	 * This also ensures that there are not two title tags in the same document,
	 * which would confuse things.
	 */
	@Override
	public Emitter parse() {
		LinkedList<Emitter> allEmitters =
				getRootEmitter().getAllChildEmitters();

		for(Emitter em:allEmitters)
			if(em instanceof Title) {
				getRootEmitter().appendProblem(
						new NonfatalException(null,"You may not have more tha" +
						"n one title per document.  Please remove one.  The s" +
						"econd title has been ignored and the program should " +
						"continue normally.")
						);
				return null;
			}

		return super.parse();
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		emission.append("<h1 id=\"top\">");
		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}
		emission.append("</h1>");

		return emission;
	}

}
