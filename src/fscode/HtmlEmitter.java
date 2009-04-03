package fscode;

/**
 * Contract for emitting HTML code from the provided FSCode.
 *
 * @author cmiller
 * @since 0.1
 */
public interface HtmlEmitter {

	/**
	 * Emit the HTML code for this object and all of its children.  This
	 * requires that you differentiate between <code>Emitter</code>s that aren't
	 * also <code>HtmlEmitter</code>s.
	 *
	 * @since 0.1
	 */
	public StringBuilder emitHtml();

}
