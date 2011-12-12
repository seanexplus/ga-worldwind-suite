package au.gov.ga.worldwind.common.layers.styled;

import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.util.WWXML;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;

import org.w3c.dom.Element;

import au.gov.ga.worldwind.common.util.XMLUtil;

/**
 * Factory that reads {@link Style}s and {@link Attribute}s from XML.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class StyleAndAttributeFactory
{
	/**
	 * Read a list of {@link Style}s from the provided XML element and add them to the
	 * {@link AVList} under the provided parameter key.
	 * 
	 * @param element
	 *            XML element under which to read styles from
	 * @param xpath
	 *            XPath object
	 * @param paramKey
	 *            Key to use when setting the list in the params
	 * @param params
	 *            Params in which to add the list of styles
	 */
	public static void addStyles(Element element, XPath xpath, String paramKey, AVList params)
	{
		List<Style> styles = new ArrayList<Style>();

		Element[] styleElements = WWXML.getElements(element, "Style", xpath);
		if (styleElements != null)
		{
			for (Element s : styleElements)
			{
				String name = WWXML.getText(s, "@name", xpath);
				boolean defalt = XMLUtil.getBoolean(s, "@default", false);
				Style style = new Style(name, defalt);

				Element[] properties = WWXML.getElements(s, "Property", xpath);
				if (properties != null)
				{
					for (Element p : properties)
					{
						String pname = WWXML.getText(p, "@name", xpath);
						String value = WWXML.getText(p, "@value", xpath);
						String type = WWXML.getText(p, "@type", xpath);
						style.addProperty(pname, value, type);
					}
				}

				styles.add(style);
			}
		}

		params.setValue(paramKey, styles);
	}

	/**
	 * Read a list of {@link Attribute}s from the provided XML element and add them to the
	 * {@link AVList} under the provided parameter key.
	 * 
	 * @param element
	 *            XML element under which to read attributes from
	 * @param xpath
	 *            XPath object
	 * @param paramKey
	 *            Key to use when setting the list in the params
	 * @param params
	 *            Params in which to add the list of attributes
	 */
	public static void addAttributes(Element element, XPath xpath, String paramKey, AVList params)
	{
		List<Attribute> attributes = new ArrayList<Attribute>();

		Element[] attributesElements = WWXML.getElements(element, "Attribute", xpath);
		if (attributesElements != null)
		{
			for (Element a : attributesElements)
			{
				String name = WWXML.getText(a, "@name", xpath);
				Attribute attribute = new Attribute(name);

				Element[] cases = WWXML.getElements(a, "Case", xpath);
				if (cases != null)
				{
					for (Element c : cases)
					{
						String value = WWXML.getText(c, "@value", xpath);
						String style = WWXML.getText(c, "@style", xpath);
						attribute.addCase(value, style);
					}
				}

				Element[] regexes = WWXML.getElements(a, "Regex", xpath);
				if (regexes != null)
				{
					for (Element r : regexes)
					{
						String pattern = WWXML.getText(r, "@pattern", xpath);
						String style = WWXML.getText(r, "@style", xpath);
						attribute.addRegex(pattern, style);
					}
				}

				Element[] ranges = WWXML.getElements(a, "Range", xpath);
				if (ranges != null)
				{
					for (Element r : ranges)
					{
						Double min = WWXML.getDouble(r, "@min", xpath);
						Double max = WWXML.getDouble(r, "@max", xpath);
						String style = WWXML.getText(r, "@style", xpath);
						attribute.addRange(min, max, style);
					}
				}

				Element text = WWXML.getElement(a, "Text", xpath);
				if (text != null)
				{
					String value = WWXML.getText(text, "@value", xpath);
					String placeholder = WWXML.getText(text, "@placeholder", xpath);
					attribute.addText(value, placeholder);
				}

				Element link = WWXML.getElement(a, "Link", xpath);
				if (link != null)
				{
					String url = WWXML.getText(link, "@url", xpath);
					String placeholder = WWXML.getText(link, "@placeholder", xpath);
					attribute.addLink(url, placeholder);
				}

				attributes.add(attribute);
			}
		}

		params.setValue(paramKey, attributes);
	}
}
