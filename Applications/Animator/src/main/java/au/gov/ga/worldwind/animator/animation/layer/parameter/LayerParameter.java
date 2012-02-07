package au.gov.ga.worldwind.animator.animation.layer.parameter;

import gov.nasa.worldwind.layers.Layer;
import au.gov.ga.worldwind.animator.animation.parameter.Parameter;

/**
 * An interface for parameters that control properties of an {@link Layer}.
 * 
 * @author James Navin (james.navin@ga.gov.au)
 * 
 */
public interface LayerParameter extends Parameter
{
	/**
	 * @return The layer this parameter is associated with
	 */
	Layer getLayer();

	/**
	 * @return The type of this layer parameter
	 */
	Type getType();

	/**
	 * Apply this parameter's state to it's associated {@link Layer} for the
	 * current frame
	 */
	void apply();

	/**
	 * An enumeration of the valid types of layer parameters.
	 * <p/>
	 * Used to help identify which attribute of the associated {@link Layer} is
	 * being controlled by the {@link LayerParameter}.
	 * 
	 */
	public static enum Type
	{
		OPACITY, NEAR, FAR, OUTLINE_OPACITY;
	}
}