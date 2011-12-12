package au.gov.ga.worldwind.common.layers.geometry;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.layers.AbstractLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.WWXML;

import javax.xml.xpath.XPath;

import org.w3c.dom.Element;

import au.gov.ga.worldwind.common.layers.geometry.provider.ShapefileShapeProvider;
import au.gov.ga.worldwind.common.layers.geometry.types.airspace.AirspaceGeometryLayer;
import au.gov.ga.worldwind.common.layers.styled.StyleAndAttributeFactory;
import au.gov.ga.worldwind.common.util.AVKeyMore;

/**
 * A factory class used to create {@link GeometryLayer}s from XML layer
 * definition files
 * 
 * @author James Navin (james.navin@ga.gov.au)
 */
public class GeometryLayerFactory
{
	protected static final String DATE_TIME_PATTERN = "dd MM yyyy HH:mm:ss z";

	/**
	 * Create a new {@link GeometryLayer} from an XML definition.
	 */
	public static GeometryLayer createGeometryLayer(Element domElement, AVList params)
	{
		params = AbstractLayer.getLayerConfigParams(domElement, params);
		params = getParamsFromDocument(domElement, params);

		GeometryLayer layer;

		String type = WWXML.getText(domElement, "RenderType");
		if ("Airspace".equalsIgnoreCase(type))
		{
			layer = new AirspaceGeometryLayer(params);
		}
		else
		{
			throw new IllegalArgumentException("Could not find layer for GeometryLayer: " + type);
		}

		setLayerParams(layer, params);
		return layer;
	}

	/**
	 * Call the standard {@link Layer} setters for values in the params AVList.
	 */
	protected static void setLayerParams(GeometryLayer layer, AVList params)
	{
		String s = params.getStringValue(AVKey.DISPLAY_NAME);
		if (s != null)
		{
			layer.setName(s);
		}

		Double d = (Double) params.getValue(AVKey.OPACITY);
		if (d != null)
		{
			layer.setOpacity(d);
		}

		d = (Double) params.getValue(AVKey.MAX_ACTIVE_ALTITUDE);
		if (d != null)
		{
			layer.setMaxActiveAltitude(d);
		}

		d = (Double) params.getValue(AVKey.MIN_ACTIVE_ALTITUDE);
		if (d != null)
		{
			layer.setMinActiveAltitude(d);
		}

		Boolean b = (Boolean) params.getValue(AVKey.NETWORK_RETRIEVAL_ENABLED);
		if (b != null)
		{
			layer.setNetworkRetrievalEnabled(b);
		}

		Object o = params.getValue(AVKey.URL_CONNECT_TIMEOUT);
		if (o != null)
		{
			layer.setValue(AVKey.URL_CONNECT_TIMEOUT, o);
		}

		o = params.getValue(AVKey.URL_READ_TIMEOUT);
		if (o != null)
		{
			layer.setValue(AVKey.URL_READ_TIMEOUT, o);
		}

		o = params.getValue(AVKey.RETRIEVAL_QUEUE_STALE_REQUEST_LIMIT);
		if (o != null)
		{
			layer.setValue(AVKey.RETRIEVAL_QUEUE_STALE_REQUEST_LIMIT, o);
		}

		layer.setValue(AVKey.CONSTRUCTION_PARAMETERS, params.copy());
	}

	/**
	 * Fill the params with the values in the {@link GeometryLayer} specific XML
	 * elements.
	 */
	public static AVList getParamsFromDocument(Element domElement, AVList params)
	{
		if (params == null)
			params = new AVListImpl();

		XPath xpath = WWXML.makeXPath();

		WWXML.checkAndSetStringParam(domElement, params, AVKey.URL, "URL", xpath);
		WWXML.checkAndSetLongParam(domElement, params, AVKey.EXPIRY_TIME, "ExpiryTime", xpath);
		WWXML.checkAndSetDateTimeParam(domElement, params, AVKey.EXPIRY_TIME, "LastUpdate", DATE_TIME_PATTERN, xpath);
		WWXML.checkAndSetStringParam(domElement, params, AVKey.DATA_CACHE_NAME, "DataCacheName", xpath);
		WWXML.checkAndSetStringParam(domElement, params, AVKeyMore.RENDER_TYPE, "RenderType", xpath);
		WWXML.checkAndSetStringParam(domElement, params, AVKeyMore.SHAPE_TYPE, "ShapeType", xpath);

		setupShapeProvider(domElement, xpath, params);

		Element styles = WWXML.getElement(domElement, "Styles", xpath);
		if (styles != null)
		{
			StyleAndAttributeFactory.addStyles(styles, xpath, AVKeyMore.SHAPE_STYLES, params);
		}

		Element attributes = WWXML.getElement(domElement, "Attributes", xpath);
		if (attributes != null)
		{
			StyleAndAttributeFactory.addAttributes(attributes, xpath, AVKeyMore.SHAPE_ATTRIBUTES, params);
		}

		return params;
	}

	/**
	 * Adds a {@link ShapeProvider} to params matching the 'DataFormat' XML
	 * element.
	 */
	protected static void setupShapeProvider(Element domElement, XPath xpath, AVList params)
	{
		String format = WWXML.getText(domElement, "DataFormat", xpath);

		if ("Shapefile".equalsIgnoreCase(format))
		{
			params.setValue(AVKeyMore.SHAPE_PROVIDER, new ShapefileShapeProvider());
		}
		else
		{
			throw new IllegalArgumentException("Could not find shape provider for DataFormat: " + format);
		}
	}
}
