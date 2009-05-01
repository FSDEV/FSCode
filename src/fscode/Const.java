package fscode;

/**
 * Some integer-valued constants that are shared between different classes.
 *
 * @author cmiller
 * @since 0.1
 */
public enum Const {

	/**
	 * Align objects.
	 * 
	 * @see fscode.macro.TOCMacro
	 * @see fscode.tags.Table
	 * @since 0.1
	 */
	ALIGN_RIGHT,
	ALIGN_LEFT,
	ALIGN_CENTER,

	/**
	 * Vertical alignment of objects.
	 */
	ALIGN_MIDDLE,
	ALIGN_TOP,
	ALIGN_BOTTOM,
	
	WIDTH_PIXELS,
	WIDTH_PERCENT,

	HEIGHT_PIXELS,
	HEIGHT_PERCENT
}
