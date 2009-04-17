package fscode.tags;

import fscode.Const;
import fscode.Emitter;
import fscode.HtmlEmitter;
import fscode.exception.NonfatalException;
import java.util.ResourceBundle;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Support for the creation of tables using FSCode.
 *
 * @author cmiller
 * @since 0.1
 */
public class Table extends Emitter implements HtmlEmitter {

	private Const align = Const.ALIGN_LEFT;

	private int width = 0;

	private Const width_type = Const.WIDTH_PERCENT;

	private int border = 1;
	
	protected int rows = 0;

	protected int columns = 0;

	public Table(Emitter parent, Node contents) {
		super(parent, contents);
	}

	@Override
	public Emitter parse() {
		// 1: Parse table attributes
		NamedNodeMap attrs = contents.getAttributes();
		String attrStr;
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
		if(attrs.getNamedItem("border")!=null) {
			attrStr = attrs.getNamedItem("border").getTextContent();
			if(attrStr!=null) {
				border = Integer.parseInt(attrStr);

				if(border<0) {
					border = 1;
					getRootEmitter().appendProblem(
							new NonfatalException(this, ResourceBundle
							.getBundle((String)getConfig().get("lang"))
							.getString("TAGS_TABLE_INVALID_BORDER"))
							);
				}

			}
		}
		if(attrs.getNamedItem("width")!=null) {
			attrStr = attrs.getNamedItem("width").getTextContent();
			if(attrStr!=null) {
				StringBuilder num = new StringBuilder();
				StringBuilder type = new StringBuilder();
				for(char c:attrStr.toCharArray())
					if(Character.isDigit(c))
						num.append(c);
					else
						break;
				for(char c:new StringBuilder(attrStr).reverse().toString()
						.toCharArray())
					if(!Character.isDigit(c))
						type.insert(0, c);
					else
						break;
				if(num.length()==0) {
					getRootEmitter().appendProblem(
							new NonfatalException(this, ResourceBundle
							.getBundle((String)getConfig().get("lang"))
							.getString("TAGS_TABLE_INVALID_WIDTH"))
							);
					width = 100;
					width_type = Const.WIDTH_PERCENT;
				} else {
					width = Integer.parseInt(num.toString());
					if(type.toString().equalsIgnoreCase("px"))
						width_type = Const.WIDTH_PIXELS;
					else if(type.toString().equalsIgnoreCase("%"))
						width_type = Const.WIDTH_PERCENT;
					else {
						getRootEmitter().appendProblem(
								new NonfatalException(this, ResourceBundle
								.getBundle((String)getConfig().get("lang"))
								.getString("TAGS_TABLE_INVALID_WIDTH_TYPE"))
								);
						width_type = Const.WIDTH_PERCENT;
					}
				}
			}
			if(width_type==Const.WIDTH_PERCENT&&width>100)
				getRootEmitter().appendProblem(
						new NonfatalException(this, ResourceBundle
						.getBundle((String)getConfig().get("lang"))
						.getString("TAGS_TABLE_INVALID_WIDTH_PERCENTAGE"))
						);
		}
		// 2: Parse table children
		return super.parse();

		// 3: Inspect table children.  If there are any nodes that aren't table
		//    rows, send errors to the user.  Orphan those nodes
/*		for(Emitter em:getChildren()) {
			if(!em.getClass().isInstance(Row.class)) {
				getRootEmitter().appendProblem(
						new NonfatalException(this, ResourceBundle
						.getBundle((String)getConfig().get("lang"))
						.getString("TAGS_TABLE_INVALID_CHILD_NODE"))
						);
			}
		}*/

		//return this;
	}

	public StringBuilder emitHtml() {
		StringBuilder emission = new StringBuilder();

		// opening tag
		emission.append("<table");
		if(align == Const.ALIGN_CENTER)
			emission.append(" align=\"center\"");
		else if(align == Const.ALIGN_RIGHT)
			emission.append(" align=\"right\"");
		if(width_type == Const.WIDTH_PIXELS) {
			emission.append(" width=\"");
			emission.append(width);
			emission.append("px\"");
		} else {
			if(width != 0) {
				emission.append(" width=\"");
				emission.append(width);
				emission.append("%\"");
			}
		}
		if(border != 0) {
			emission.append(" border=\"");
			emission.append(border);
			emission.append("\"");
		}
		emission.append(">\n");

		// contents
		for(Emitter em:getChildren())
			if(em instanceof Row
					&&em instanceof HtmlEmitter)
				emission.append(((HtmlEmitter)em).emitHtml());

		// closing tag
		emission.append("</table>\n");

		return emission;
	}

	

}
