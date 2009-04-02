package fscode;

import java.util.List;

/**
 * The nuts and bolts of parsing abstracted for the use of every tag.
 *
 * @author cmiller
 * @since 0.1
 */
public interface Emitter {

	/**
	 * Reference back to the Emitter's parent node.
	 *
	 * @since 0.1
	 */
	public Emitter getParent();
	/**
	 * Sets the Emitter's parent node.
	 *
	 * @since 0.1
	 */
	public void setParent(Emitter parent);

	/**
	 * A list of all the Emitter's child nodes.
	 *
	 * @since 0.1
	 */
	public List<Emitter> getChildren();
	/**
	 * Set all the Emitter's child nodes all at once.  This operation is
	 * generally discouraged.
	 *
	 * @since 0.1
	 */
	public void setChildren(List<Emitter> children);

	/**
	 * Used to append a child node to an Emitter.
	 *
	 * @since 0.1
	 */
	public void appendChild(Emitter child);

}
