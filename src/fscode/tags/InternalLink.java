package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import fscode.exception.NonfatalException;
import java.util.Map;
import java.util.ResourceBundle;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Used to link between pages in a wiki engine, or another page on another
 * wiki.
 *
 * @author cmiller
 * @since 0.1
 */
public class InternalLink extends Emitter implements HtmlEmitter {

	private WikiProvider wiki;
	private String wikiPage;

	public InternalLink(Emitter parent, Node contents) {
		super(parent, contents);
	}

	@Override
	public Emitter parse() {
		NamedNodeMap nnm = contents.getAttributes();
		Node n;
		Map<String, WikiProvider> wikiProviders =
			(Map<String, WikiProvider>)getConfig().get("wikiProviders");
		n = nnm.getNamedItem("wiki");
		if(n!=null)
			wiki = wikiProviders.get(n.getTextContent());
		else
			wiki = wikiProviders.get("");
		if(wiki==null) {
			reportProblem("TAGS_INTERNAL_LINK_NO_SUCH_WIKI");
			return super.parse();
		}
		n = nnm.getNamedItem("page");
		if(n!=null) {
			wikiPage = n.getTextContent();
		} else {
			reportProblem("TAGS_INTERNAL_LINK_NO_PAGE_PROVIDED");
		}
		return super.parse();
	}

	public StringBuilder emitHtml() {
		if(wiki==null||wikiPage==null||((String)getConfig()
				.get("isWiki")).matches("(NO)|(FALSE)"))
			return new StringBuilder();

		StringBuilder emission = new StringBuilder();

		emission.append("<a href=\""+wiki.getUrlForPage(wikiPage)+"\">");

		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}

		emission.append("</a>");

		return emission;
	}


}
