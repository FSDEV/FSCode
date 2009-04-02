package fscode.macro;

/**
 * Objects which can appear in the table of contents implement this, which
 * is used to communicate to the TOC macro how to generate the table of
 * contents.
 * 
 * @author cmiller
 */
public interface TOCElement {

	public String getName();

	public String getHtmlAnchor();

}
