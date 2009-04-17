package fscode.macro;

import fscode.Const;
import fscode.Emitter;
import fscode.HtmlEmitter;
import fscode.exception.NonfatalException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * This is the grand showcasing of the flexibility of the Emitter system - a
 * tag which inspects the rest of the document and generates content dependent
 * upon that content.
 *
 * @author cmiller
 * @since 0.1
 */
public class TOCMacro extends Emitter implements HtmlEmitter {

	private int depth = Integer.MAX_VALUE;

	private Const align = Const.ALIGN_LEFT;

	private boolean inline = true;

	public TOCMacro(Emitter parent, Node contents) {
		super(parent, contents);
	}

	@Override
	public TOCMacro parse() {
		NamedNodeMap attrs = contents.getAttributes();
		String attrStr;
		if(attrs.getNamedItem("depth")!=null) {
			depth = Integer.parseInt(attrs.getNamedItem("depth")
					.getTextContent());
			if(depth<2)
				depth = Integer.MAX_VALUE;
		}
		if(attrs.getNamedItem("align")!=null) {
			attrStr = attrs.getNamedItem("align").getTextContent();
			if(attrStr!=null)
				if(attrStr.equalsIgnoreCase("RIGHT"))
					align = Const.ALIGN_RIGHT;
				else if(attrStr.equalsIgnoreCase("LEFT"))
					align = Const.ALIGN_LEFT;
				else if(attrStr.equalsIgnoreCase("CENTER"))
					align = Const.ALIGN_CENTER;
		}
		if(attrs.getNamedItem("inline")!=null) {
			attrStr = attrs.getNamedItem("align").getTextContent();
			if(attrStr!=null) {
				inline = false;
				inline = Boolean.parseBoolean(attrStr);
				if(attrStr.equalsIgnoreCase("YES"))
					inline = true;
			}
		}
		return this;
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		LinkedList<TOCElement> elements = getRelevantTocElements();

		for(TOCElement tc:elements)
			if(tc.getIndentLevel()>depth)
				elements.remove(tc);

		if(elements.size()==0) {
			getRootEmitter().appendProblem(
					new NonfatalException(this, ResourceBundle
					.getBundle((String)getConfig().get("lang"))
					.getString("MACRO_TOCMACRO_EMPTYTOC"))
					);
		}

		if(inline==false) {
			emission.append("<div style=\"float:");
			if(align==Const.ALIGN_CENTER)
				emission.append("center");
			else if(align==Const.ALIGN_LEFT)
				emission.append("left");
			else if(align==Const.ALIGN_RIGHT)
				emission.append("right");
			
			emission.append("\">\n");
		}
		emission.append("<b>Table of Contents</b>\n<ul>\n");
		
		// content
		TOCElement last = null; int unclosedLists = 0;
		for(TOCElement tc:elements) {
			if(last!=null) {
				if(tc.getIndentLevel()<last.getIndentLevel()&&unclosedLists>0) {
					emission.append("</ul>\n");
					unclosedLists--;
				}
				if(tc.getIndentLevel()>last.getIndentLevel()) {
					emission.append("<ul>\n");
					unclosedLists++;
				}
			}
			emission.append("<li><a href=\"");
			emission.append(tc.getHtmlAnchor());
			emission.append("\">");
			emission.append(tc.getName());
			emission.append("</a></li>\n");
			last = tc;
		}
		for(;unclosedLists!=0;unclosedLists--)
			emission.append("</ul>\n");

		emission.append("<a href=\"#top\">return to top</a>\n</ul>\n");

		if(inline==false)
			emission.append("</div>\n");

		return emission;
	}

	private LinkedList<TOCElement> getRelevantTocElements() {
		LinkedList<TOCElement> elements = null;

		// rewind to the last toc element
		LinkedList<Emitter> allElements = getRootEmitter()
				.getAllChildEmitters();
		int lastTocIdx = allElements.indexOf(this);
		while(lastTocIdx>0)
			if(allElements.get(lastTocIdx) instanceof TOCElement)
				break;
			else
				lastTocIdx--;
		if(allElements.get(lastTocIdx) instanceof TOCElement) {
			// keep adding toc elements until the indent level is equal to
			// the indent level of the last toc element
			elements = new LinkedList<TOCElement>();
			for(int i=lastTocIdx+1;lastTocIdx<allElements.size();++i)
				if(allElements.get(i) instanceof TOCElement) {
					if(((TOCElement)allElements.get(i)).getIndentLevel()
							<=((TOCElement)allElements.get(lastTocIdx))
							.getIndentLevel())
						break;
					else
						elements.add((TOCElement)allElements.get(i));
				}
		} else
			elements = getAllTocElements(getRootEmitter());

		return elements;
	}

	private LinkedList<TOCElement> getAllTocElements(Emitter em) {
		LinkedList<TOCElement> toReturn = new LinkedList<TOCElement>();

		if(em instanceof TOCElement)
			toReturn.add((TOCElement)em);

		for(Emitter e:em.getChildren())
			toReturn.addAll(getAllTocElements(e));

		return toReturn;
	}

}
