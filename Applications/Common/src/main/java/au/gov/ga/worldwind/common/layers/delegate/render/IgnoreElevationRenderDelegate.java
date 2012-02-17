package au.gov.ga.worldwind.common.layers.delegate.render;

import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.render.DrawContext;

import org.w3c.dom.Element;

import au.gov.ga.worldwind.common.layers.delegate.IDelegate;
import au.gov.ga.worldwind.common.layers.delegate.IRenderDelegate;
import au.gov.ga.worldwind.common.render.ExtendedDrawContext;

/**
 * Implementation of {@link IRenderDelegate} that uses the
 * {@link ExtendedDrawContext} to disable the elevation model for this layer,
 * causing the layer to be rendered on a flat surface.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class IgnoreElevationRenderDelegate implements IRenderDelegate
{
	protected final static String DEFINITION_STRING = "IgnoreElevation";
	protected boolean oldValue = false;

	public IgnoreElevationRenderDelegate()
	{
	}

	@Override
	public void preRender(DrawContext dc)
	{
		if (dc instanceof ExtendedDrawContext)
		{
			oldValue = ((ExtendedDrawContext) dc).getGeographicSurfaceTileRenderer().isIgnoreElevation();
			((ExtendedDrawContext) dc).getGeographicSurfaceTileRenderer().setIgnoreElevation(true);
		}
	}

	@Override
	public void postRender(DrawContext dc)
	{
		if (dc instanceof ExtendedDrawContext)
		{
			((ExtendedDrawContext) dc).getGeographicSurfaceTileRenderer().setIgnoreElevation(oldValue);
		}
	}

	@Override
	public String toDefinition(Element layerElement)
	{
		return DEFINITION_STRING;
	}

	@Override
	public IDelegate fromDefinition(String definition, Element layerElement, AVList params)
	{
		if (definition.equalsIgnoreCase(DEFINITION_STRING))
		{
			return new IgnoreElevationRenderDelegate();
		}
		return null;
	}
}