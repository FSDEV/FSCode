package fscode;

import fscode.exception.EmitterAlreadyRegisteredForTagNameException;
import fscode.exception.NonfatalException;
import fscode.macro.TOCMacro;
import fscode.tags.Bold;
import fscode.tags.Heading;
import fscode.tags.Italic;
import fscode.tags.Super;
import fscode.tags.Sub;
import fscode.tags.Text;
import fscode.tags.Title;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathFactory;
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
	 * Big list of all problems that have happened while parsing.
	 */
	protected LinkedList<NonfatalException> problems;

	/**
	 * Used in the creation of XPath queries, good for simple, clean code
	 * to manipulate and search through the DOM XML tree.
	 */
	protected static XPathFactory queryFactory;

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
		this.parent = parent;
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

			c = getEmitters().get(n.getNodeName());

			if(c == null) {
				return new Emitter(parent, n).parse();
			}

			try {
				try {
					return ((Emitter)c.getConstructor(
							Emitter.class,
							Node.class).newInstance(parent, n)).parse();
				} catch (InstantiationException ex) {
					Logger.getLogger(Emitter.class.getName()).log(
							Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					Logger.getLogger(Emitter.class.getName()).log(
							Level.SEVERE, null, ex);
				} catch (IllegalArgumentException ex) {
					Logger.getLogger(Emitter.class.getName()).log(
							Level.SEVERE, null, ex);
				} catch (InvocationTargetException ex) {
					Logger.getLogger(Emitter.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			} catch (NoSuchMethodException ex) {
				Logger.getLogger(Emitter.class.getName()).log(Level.SEVERE,
						"Apparently we lost a constructor in there " +
						"somewhere.", ex);
			} catch (SecurityException ex) {
				Logger.getLogger(Emitter.class.getName()).log(Level.SEVERE,
						"I haven't got a clue how to respond to this", ex);
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
	 * Anti-null accessor to the <code>XPathFactory</code>.
	 * 
	 * @since 0.1
	 */
	protected static XPathFactory getQueryFactory() {
		if(queryFactory==null)
			queryFactory = XPathFactory.newInstance();
		return queryFactory;
	}

	/**
	 * Finds the root emitter in the current Emitter tree.  Note that if the
	 * root node has itself as its parent and not <code>null</code> then this
	 * will infinitely loop.
	 * 
	 * @since 0.1
	 */
	public Emitter getRootEmitter() {
		if(getParent()!=null)
			return getParent().getRootEmitter();
		else
			return this;
	}

	/**
	 * Get an ordered list of all child emitters to this node.
	 *
	 * @since 0.1
	 */
	public LinkedList<Emitter> getAllChildEmitters() {
		LinkedList<Emitter> childEmitters = new LinkedList<Emitter>();

		for(Emitter em:getChildren()) {
			childEmitters.add(em);
			childEmitters.addAll(em.getAllChildEmitters());
		}

		return childEmitters;
	}

	/**
	 * Anti-null accessor to our problems.
	 */
	private LinkedList<NonfatalException> getProblems_Private() {
		if(problems==null)
			problems = new LinkedList<NonfatalException>();
		return problems;
	}

	/**
	 * Returns all problems, if any, that were encountered while parsing this
	 * document.  Problems are stored with the root node, although if you do
	 * some treewalking and document-merging you may need to search for nodes
	 * to get all the problems.
	 *
	 * @since 0.1
	 */
	public LinkedList<NonfatalException> getProblems() {
		return problems;
	}

	/**
	 * Add a problem to the list of woes.
	 *
	 * @since 0.1
	 */
	public void appendProblem(NonfatalException e) {
		getProblems_Private().add(e);
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
			emitters.put("b", Bold.class);
			emitters.put("i", Italic.class);
			emitters.put("h", Heading.class);
			emitters.put("h1", Heading.class);
			emitters.put("h2", Heading.class);
			emitters.put("h3", Heading.class);
			emitters.put("h4", Heading.class);
			emitters.put("h5", Heading.class);
			emitters.put("h6", Heading.class);
			emitters.put("title", Title.class);
			emitters.put("super", Super.class);
			emitters.put("sub", Sub.class);
			emitters.put("macro:toc", TOCMacro.class);
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
