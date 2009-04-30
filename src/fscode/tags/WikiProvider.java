package fscode.tags;

/**
 * Implement this to provide a way for FSCode to detect wiki engines and the
 * pages in them.  This is used for differentiation between pages which do and
 * do not exist, to get valid links to them, and as a mechanism to provide for
 * other kinds of actions which makes the whole system more flexible.
 *
 * The architecture for this interface assumes that only one wiki engine will
 * be represented by this object.  You can pass multiple
 * <code>WikiProvider</code>s if you need to reference between multiple wikis
 * and support inter-wiki linking, etc.
 *
 * @author cmiller
 * @since 0.1
 */
public interface WikiProvider {

	/**
	 * A page will have some kind of identifier to access it.  This is to
	 * determine whether or not that page exists.
	 *
	 * @since 0.1
	 */
	public boolean hasPage(String page);

	/**
	 * Get a valid URL for the page.  This is used so that FSCode can emit
	 * the proper link in HTML to go to the correct page.  If your wiki view
	 * does not redirect to an edit page if the page does not exist, you will
	 * want to use <code>hasPage</code> to determine whether you should output
	 * a link to the edit URL or the view URL.
	 *
	 * @since 0.1
	 */
	public String getUrlForPage(String page);

}
