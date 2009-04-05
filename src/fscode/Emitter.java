package fscode;

import fscode.tags.Text;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The nuts and bolts of parsing abstracted for the use of every tag.
 *
 * @author cmiller
 * @since 0.1
 */
public class Emitter {

	/**
	 * Parent node, used for tree walking up to higher nodes.
	 */
	protected Emitter parent;

	/**
	 * All child nodes.
	 */
	protected LinkedList<Emitter> children;

	/**
	 * All nodes need to know about their contents from the XML markup.
	 */
	protected Node contents;

	/**
	 * A complete list of all registered Emitters that can then be
	 * automagically added to the generic parsing list.
	 */
	private static TreeMap<String, Class<? extends Emitter>> emitters;

	/**
	 * Creates a new Emitter with an empty child list.
	 *
	 * @since 0.1
	 */
	private Emitter() {
		children = new LinkedList<Emitter>();
	}

	/**
	 * Creates a new Emitter with a parent node and XML contents.  Does
	 * <i>not</i> parse it.
	 *
	 * @since 0.1
	 */
	public Emitter(Emitter parent, Node contents) {
		this();
		this.contents = contents;
	}

	/**
	 * Reference back to the Emitter's parent node.
	 *
	 * @since 0.1
	 */
	public Emitter getParent() {
		return parent;
	}
	/**
	 * Sets the Emitter's parent node.
	 *
	 * @since 0.1
	 */
	public void setParent(Emitter parent) {
		this.parent = parent;
	}

	/**
	 * A list of all the Emitter's child nodes.
	 *
	 * @since 0.1
	 */
	public List<Emitter> getChildren() {
		return children;
	}
	/**
	 * Set all the Emitter's child nodes all at once.  This operation is
	 * generally discouraged.
	 *
	 * @since 0.1
	 */
	public void setChildren(List<Emitter> children) {
		if(children instanceof LinkedList) {
			this.children = (LinkedList<Emitter>)children;
		} else {
			this.children = new LinkedList<Emitter>(children);
		}
	}

	/**
	 * Used to append a child node to an Emitter.
	 *
	 * @since 0.1
	 */
	public void appendChild(Emitter child) {
		children.add(child);
	}

	/**
	 * Get the XML contents of this node.
	 *
	 * @since 0.1
	 */
	public Node getContents() {
		return contents;
	}

	/**
	 * Set the XML contents of this node.  Generally discouraged.
	 * @since 0.1
	 */
	public void setContents(Node contents) {
		this.contents = contents;
	}

	/**
	 * Generic parsing routine for this Emitter.  It is suggested that if you
	 * do overload this method and if you do call the super method in that
	 * code that you do it <i>last</i> to prevent child nodes from becoming
	 * confused if your node has not yet treated its attributes.
	 *
	 * @since 0.1
	 */
	public Emitter parse() {
		NodeList nl = contents.getChildNodes();

		for(int i = 0; i!=nl.getLength(); i++) {
			appendChild(Emitter.parse(this, nl.item(i)));
		}

		return this;
	}

	/**
	 * The default parser.  Differentiates between all known nodes specified by
	 * <code>getEmitters</code>.
	 *
	 * @since 0.1
	 */
	public static Emitter parse(Emitter parent, Node n) {
		int type = n.getNodeType();

        switch (type) {
        case Node.ELEMENT_NODE:
			// run through the supported tags and macros
			Class c = null;
			try {
				c = Class.forName(n.getNodeName());
			} catch (ClassNotFoundException ex) {
				// return a dummy node
				return new Emitter(parent, n).parse();
			}

			if(!c.isInstance(Emitter.class)) {
				// return a dummy node
				return new Emitter(parent, n).parse();
			}

            break;
        case Node.TEXT_NODE:
            return new Text(parent, n);
        default:
            // it gets ignored
            break;
        }

		return null;
	}

	/**
	 * Generally you should not deviate from the default Emitter list, however,
	 * this is included so that you can use <code>getEmitters</code> to take
	 * a snapshot of the Emitter list before you reset it.  In that way you
	 * could toggle between different parsing modes for different modules, such
	 * as a forum and a wiki.
	 *
	 * @since 0.1
	 */
	public static void setEmitters(
			TreeMap<String, Class<? extends Emitter>> emitters) {
		Emitter.emitters = emitters;
	}

	/**
	 * A map of all emitters and what tags they are bound to.
	 *
	 * @since 0.1
	 */
	public static TreeMap<String, Class<? extends Emitter>> getEmitters() {
		if(emitters==null) {
			emitters = new TreeMap<String, Class<? extends Emitter>>();
			// list of all default emitters
		}
 		return emitters;
	}

	/**
	 * Add your own custom emitter to the default parser.
	 *
	 * @throws fscode.EmitterAlreadyRegisteredForTagNameException if you attempt
	 *		to bind an Emitter to a tag that is already in use.
	 * @since 0.1
	 */
	public static void addEmitter(
			String tagName,
			Class<? extends Emitter> emitter)
		throws
			EmitterAlreadyRegisteredForTagNameException {
		if(getEmitters().containsKey(tagName))
			throw new EmitterAlreadyRegisteredForTagNameException();
		getEmitters().put(tagName, emitter);
	}

	/**
	 * If you have tampered with the Emitter list you may wish to reset it.
	 *
	 * @since 0.1
	 */
	public static void resetEmitterList() {
		emitters=null;
	}

}
