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

	private Emitter parent;

	private LinkedList<Emitter> children;

	private Node contents;

	private static TreeMap<String, Emitter> emitters;

	private Emitter() {
		children = new LinkedList<Emitter>();
	}

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

	public Node getContents() {
		return contents;
	}

	public void setContents(Node contents) {
		this.contents = contents;
	}

	public Emitter parse() {
		NodeList nl = contents.getChildNodes();

		for(int i = 0; i!=nl.getLength(); i++) {
			appendChild(Emitter.parse(this, nl.item(i)));
		}

		return this;
	}

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

	private static TreeMap<String, Emitter> getEmitters() {
		if(emitters==null) {
			emitters = new TreeMap<String, Emitter>();
			// list of all default emitters
		}
 		return emitters;
	}

}
