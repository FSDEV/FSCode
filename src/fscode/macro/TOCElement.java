package fscode.macro;

/**
 * Objects which can appear in the table of contents implement this, which
 * is used to communicate to the TOC macro how to generate the table of
 * contents.
 * 
 * @author cmiller
 * @since 0.1
 */
public interface TOCElement {

	/**
	 * Printed name for how the element shall appear.
	 *
	 * @since 0.1
	 */
	public String getName();

	/**
	 * For <code>HTMLEmitter</code>s, this is the anchor provided to link to.
	 *
	 * @see fscode.HtmlEmitter
	 * @since 0.1
	 */
	public String getHtmlAnchor();

	/**
	 * Indentation isn't performed in an XML-like nested manner, so this is
	 * the way of easily differentiating between levels of nesting.
	 *
	 * @since 0.1
	 */
	public int getIndentLevel();

}
