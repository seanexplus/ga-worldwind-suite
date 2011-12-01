package au.gov.ga.worldwind.animator.application.effects;

import gov.nasa.worldwind.render.DrawContext;

import java.awt.Dimension;

import au.gov.ga.worldwind.animator.animation.Animatable;
import au.gov.ga.worldwind.animator.animation.Animation;

public interface Effect extends Animatable
{
	/**
	 * Add the provided parameter to this effect
	 * 
	 * @param parameter
	 *            The parameter to add to the effect
	 */
	void addParameter(EffectParameter parameter);

	/**
	 * Called before the scene is rendered. The effect should setup it's
	 * framebuffer here.
	 * 
	 * @param dc
	 *            Draw context
	 * @param dimensions
	 *            Dimensions of the viewport (includes render scale during
	 *            rendering)
	 */
	void preDraw(DrawContext dc, Dimension dimensions);

	/**
	 * Called after the scene is rendered. The effect should render it's
	 * framebuffer using it's effect shader here.
	 * 
	 * @param dc
	 *            Draw context
	 * @param dimensions
	 *            Dimensions of the viewport (includes render scale during
	 *            rendering)
	 */
	void postDraw(DrawContext dc, Dimension dimensions);

	/**
	 * Create a new instance of this {@link Effect}, associated with the
	 * provided {@link Animation}.
	 * 
	 * @param animation
	 *            Animation to associated new Effect with
	 * @return New instance of this Effect
	 */
	Effect createWithAnimation(Animation animation);
}
