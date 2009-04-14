package fscode.tags;

import fscode.Const;
import fscode.Emitter;
import fscode.HtmlEmitter;
import fscode.exception.NonfatalException;
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

	private int width = 100;

	private Const width_type = Const.WIDTH_PERCENT;

	private int border = 1;
	
	protected int rows = 0;

	protected int columns = 0;

	public Table(Emitter parent, Node contents) {
		super(parent, contents);
	}

	@Override
	public Table parse() {
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
							new NonfatalException(this, "You tried to set a n" +
							"egative border.  This doesn't work.  The border " +
							"has been reset to 1.  Please fix this.")
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
							new NonfatalException(this, "When in the course o" +
							"f determining the width to make the table, the n" +
							"umber could not be read!  Please fix this!  For " +
							"now using a default value of 100%")
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
								new NonfatalException(this, "Invalid width ty" +
								"pe given, it is not a percentage or in pixel" +
								"s.  Defaulting to percentage.  Please fix th" +
								"is!")
								);
						width_type = Const.WIDTH_PERCENT;
					}
				}
			}
			if(width_type==Const.WIDTH_PERCENT&&width>100)
				getRootEmitter().appendProblem(
						new NonfatalException(this, "Table width invalid, you" +
						" have selected a percentage-based width, yet the wid" +
						"th is greater than 100.  This doesn't make sense!  T" +
						"his will cause undefined behavior.  You should fix t" +
						"his.")
						);
		}
		// 2: Parse table children
		


		// 3: Inspect table children.  If there are any nodes that aren't table
		//    rows, send errors to the user.  Orphan those nodes
		
		return this;
	}

	public StringBuilder emitHtml() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	

}
