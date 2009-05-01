package fscode.tags;

import fscode.Emitter;
import fscode.HtmlEmitter;
import fscode.exception.NonfatalException;
import java.util.Collection;
import java.util.ResourceBundle;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Supports linking to external pages via the <code>url</code> tag.
 *
 * @author cmiller
 * @since 0.1
 */
public class ExternalLink extends Emitter implements HtmlEmitter {

	private String location;

	public ExternalLink(Emitter parent, Node contents) {
		super(parent, contents);
	}

	@Override
	public Emitter parse() {
		NamedNodeMap nnm = contents.getAttributes();
		Node n = nnm.getNamedItem("location");
		if(n!=null) {
			location = n.getTextContent();
			Collection<String> illegalUrls = (Collection<String>)
					getConfig().get("forbiddenLinks");
			if(illegalUrls!=null)
				for(String rgx:illegalUrls) {
					if(location.matches(rgx)) {
						location = "";
						getRootEmitter().appendProblem(
								new NonfatalException(this, ResourceBundle
								.getBundle((String)getConfig().get("lang"))
								.getString("TAGS_EXTERNAL_LINK_ILLEGAL_URL")));
					}
				}
		} else {
			location = "";
			getRootEmitter().appendProblem(
							new NonfatalException(this, ResourceBundle
							.getBundle((String)getConfig().get("lang"))
							.getString("TAGS_EXTERNAL_LINK_NO_LINK_SUPPLIED"))
							);
		}
		return super.parse();
	}

	public StringBuilder emitHtml() {
		if(location.equals(""))
			return new StringBuilder();

		StringBuilder emission = new StringBuilder();

		emission.append("<a href=\"" + location + "\">");

		for(Emitter em:getChildren()) {
			if(em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());
		}

		emission.append("</a>");

		return emission;
	}

}
