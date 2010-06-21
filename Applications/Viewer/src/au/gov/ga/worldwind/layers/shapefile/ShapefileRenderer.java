package au.gov.ga.worldwind.layers.shapefile;

import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.util.Logging;

import java.awt.Color;
import java.util.List;

import javax.media.opengl.GL;

import nasa.worldwind.retrieve.ExtendedRetrievalService.SectorPolyline;
import au.gov.ga.worldwind.util.HSLColor;

public class ShapefileRenderer
{
	private static final String EXT_BLEND_FUNC_SEPARATE_STRING = "GL_EXT_blend_func_separate";

	private boolean enableAntialiasing = false;
	private boolean enableBlending = false;
	private boolean enableDepthOffset = false;
	private boolean enableLighting = false;
	private boolean drawWireframe = false;
	private boolean showImageTileOutlines = false;
	private Material lightMaterial = Material.WHITE;
	private Vec4 lightDirection = new Vec4(1.0, 0.5, 1.0);
	private boolean useEXTBlendFuncSeparate = false;
	private boolean haveEXTBlendFuncSeparate = false;

	public ShapefileRenderer()
	{
	}

	public void renderTiles(DrawContext dc, List<ShapefileTile> tiles)
	{
		//beginRendering(dc);

		GL gl = dc.getGL();
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);

		for (ShapefileTile tile : tiles)
		{
			tile.render(dc);

			if (showImageTileOutlines)
			{
				SectorPolyline polyline = new SectorPolyline(tile.getSector());
				polyline.setColor(getColor(tile.getLevelNumber()));
				polyline.render(dc);
			}
		}

		//endRendering(dc);
	}

	protected void beginRendering(DrawContext dc)
	{
		if (dc == null)
		{
			String message = Logging.getMessage("nullValue.DrawContextIsNull");
			Logging.logger().severe(message);
			throw new IllegalArgumentException(message);
		}
		if (dc.getGL() == null)
		{
			String message = Logging.getMessage("nullValue.DrawingContextGLIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}

		GL gl = dc.getGL();

		gl.glPushClientAttrib(GL.GL_CLIENT_VERTEX_ARRAY_BIT);
		//gl.glEnableClientState(GL.GL_VERTEX_ARRAY);

		if (!dc.isPickingMode())
		{
			int attribMask = (this.isEnableLighting() ? GL.GL_LIGHTING_BIT : 0) // For lighting, material, and matrix mode
					| GL.GL_COLOR_BUFFER_BIT // For color write mask. If blending is enabled: for blending src and func, and alpha func.
					| GL.GL_CURRENT_BIT // For current color.
					| GL.GL_DEPTH_BUFFER_BIT // For depth test, depth func, depth write mask.
					| GL.GL_LINE_BIT // For line width, line smoothing.
					| GL.GL_POLYGON_BIT // For polygon mode, polygon offset.
					| GL.GL_TRANSFORM_BIT; // For matrix mode.
			gl.glPushAttrib(attribMask);

			if (this.isDrawWireframe())
			{
				gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
			}

			if (this.isEnableBlending())
			{
				this.setBlending(dc);
			}

			if (this.isEnableLighting())
			{
				gl.glEnableClientState(GL.GL_NORMAL_ARRAY);
				this.setLighting(dc);
			}

			if (this.isEnableAntialiasing())
			{
				gl.glEnable(GL.GL_LINE_SMOOTH);
			}
		}
		else
		{
			int attribMask = GL.GL_CURRENT_BIT // For current color.
					| GL.GL_DEPTH_BUFFER_BIT // For depth test and depth func.
					| GL.GL_LINE_BIT; // For line width.
			gl.glPushAttrib(attribMask);
		}

		if (this.isEnableDepthOffset())
		{
			gl.glEnable(GL.GL_POLYGON_OFFSET_FILL);
		}

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
	}

	protected void endRendering(DrawContext dc)
	{
		if (dc == null)
		{
			String message = Logging.getMessage("nullValue.DrawContextIsNull");
			Logging.logger().severe(message);
			throw new IllegalArgumentException(message);
		}
		if (dc.getGL() == null)
		{
			String message = Logging.getMessage("nullValue.DrawingContextGLIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}

		GL gl = dc.getGL();

		gl.glPopAttrib();
		//gl.glPopClientAttrib();
	}

	public void setBlending(DrawContext dc)
	{
		if (dc == null)
		{
			String message = Logging.getMessage("nullValue.DrawContextIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}
		if (dc.getGL() == null)
		{
			String message = Logging.getMessage("nullValue.DrawingContextGLIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}

		GL gl = dc.getGL();

		if (this.isUseEXTBlendFuncSeparate())
			this.setHaveEXTBlendFuncSeparate(gl
					.isExtensionAvailable(EXT_BLEND_FUNC_SEPARATE_STRING));

		gl.glEnable(GL.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL.GL_GREATER, 0.0f);

		gl.glEnable(GL.GL_BLEND);
		// The separate blend function correctly handles regular (non-premultiplied) colors. We want
		//     Cd = Cs*As + Cf*(1-As)
		//     Ad = As    + Af*(1-As)
		// So we use GL_EXT_blend_func_separate to specify different blending factors for source color and source
		// alpha.
		if (this.isUseEXTBlendFuncSeparate() && this.isHaveEXTBlendFuncSeparate())
		{
			gl.glBlendFuncSeparate(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA, // rgb   blending factors
					GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA); // alpha blending factors
		}
		// Fallback to a single blending factor for source color and source alpha. The destination alpha will be
		// incorrect.
		else
		{
			gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA); // rgba  blending factors
		}
	}

	public void setLighting(DrawContext dc)
	{
		if (dc == null)
		{
			String message = Logging.getMessage("nullValue.DrawContextIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}
		if (dc.getGL() == null)
		{
			String message = Logging.getMessage("nullValue.DrawingContextGLIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}

		GL gl = dc.getGL();

		gl.glEnable(GL.GL_LIGHTING);
		setLightModel(gl);
		setShadeModel(gl);

		gl.glEnable(GL.GL_LIGHT0);
		setLightMaterial(gl, GL.GL_LIGHT0, this.lightMaterial);
		setLightDirection(gl, GL.GL_LIGHT0, this.lightDirection);
	}

	private Color getColor(int level)
	{
		HSLColor color = new HSLColor(level * 60, 100, 50);
		return color.getRGB();
	}

	public boolean isEnableAntialiasing()
	{
		return enableAntialiasing;
	}

	public void setEnableAntialiasing(boolean enableAntialiasing)
	{
		this.enableAntialiasing = enableAntialiasing;
	}

	public boolean isEnableBlending()
	{
		return enableBlending;
	}

	public void setEnableBlending(boolean enableBlending)
	{
		this.enableBlending = enableBlending;
	}

	public boolean isEnableDepthOffset()
	{
		return enableDepthOffset;
	}

	public void setEnableDepthOffset(boolean enableDepthOffset)
	{
		this.enableDepthOffset = enableDepthOffset;
	}

	public boolean isEnableLighting()
	{
		return enableLighting;
	}

	public void setEnableLighting(boolean enableLighting)
	{
		this.enableLighting = enableLighting;
	}

	public boolean isDrawWireframe()
	{
		return drawWireframe;
	}

	public void setDrawWireframe(boolean drawWireframe)
	{
		this.drawWireframe = drawWireframe;
	}

	public boolean isShowImageTileOutlines()
	{
		return showImageTileOutlines;
	}

	public void setShowImageTileOutlines(boolean showImageTileOutlines)
	{
		this.showImageTileOutlines = showImageTileOutlines;
	}

	public boolean isUseEXTBlendFuncSeparate()
	{
		return useEXTBlendFuncSeparate;
	}

	public void setUseEXTBlendFuncSeparate(boolean useEXTBlendFuncSeparate)
	{
		this.useEXTBlendFuncSeparate = useEXTBlendFuncSeparate;
	}

	public boolean isHaveEXTBlendFuncSeparate()
	{
		return haveEXTBlendFuncSeparate;
	}

	public void setHaveEXTBlendFuncSeparate(boolean haveEXTBlendFuncSeparate)
	{
		this.haveEXTBlendFuncSeparate = haveEXTBlendFuncSeparate;
	}

	protected static void setLightModel(GL gl)
	{
		if (gl == null)
		{
			String message = Logging.getMessage("nullValue.GLIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}

		float[] modelAmbient = new float[4];
		modelAmbient[0] = 1.0f;
		modelAmbient[1] = 1.0f;
		modelAmbient[2] = 1.0f;
		modelAmbient[3] = 0.0f;

		gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, modelAmbient, 0);
		gl.glLightModeli(GL.GL_LIGHT_MODEL_LOCAL_VIEWER, GL.GL_TRUE);
		gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, GL.GL_FALSE);
	}

	protected static void setShadeModel(GL gl)
	{
		if (gl == null)
		{
			String message = Logging.getMessage("nullValue.GLIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}

		gl.glShadeModel(GL.GL_SMOOTH);
	}

	protected static void setLightMaterial(GL gl, int light, Material material)
	{
		if (gl == null)
		{
			String message = Logging.getMessage("nullValue.GLIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}
		if (material == null)
		{
			String message = Logging.getMessage("nullValue.MaterialIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}

		// The alpha value at a vertex is taken only from the diffuse material's alpha channel, without any
		// lighting computations applied. Therefore we specify alpha=0 for all lighting ambient, specular and
		// emission values. This will have no effect on material alpha.

		float[] ambient = new float[4];
		float[] diffuse = new float[4];
		float[] specular = new float[4];
		material.getDiffuse().getRGBColorComponents(diffuse);
		material.getSpecular().getRGBColorComponents(specular);
		ambient[3] = diffuse[3] = specular[3] = 0.0f;

		gl.glLightfv(light, GL.GL_AMBIENT, ambient, 0);
		gl.glLightfv(light, GL.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(light, GL.GL_SPECULAR, specular, 0);
	}

	protected static void setLightDirection(GL gl, int light, Vec4 direction)
	{
		if (gl == null)
		{
			String message = Logging.getMessage("nullValue.GLIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}
		if (direction == null)
		{
			String message = Logging.getMessage("nullValue.DirectionIsNull");
			Logging.logger().severe(message);
			throw new IllegalStateException(message);
		}

		// Setup the light as a directional light coming from the viewpoint. This requires two state changes
		// (a) Set the light position as direction x, y, z, and set the w-component to 0, which tells OpenGL this is
		//     a directional light.
		// (b) Invoke the light position call with the identity matrix on the modelview stack. Since the position
		//     is transfomed by the 

		Vec4 vec = direction.normalize3();
		float[] params = new float[4];
		params[0] = (float) vec.x;
		params[1] = (float) vec.y;
		params[2] = (float) vec.z;
		params[3] = 0.0f;

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glLightfv(light, GL.GL_POSITION, params, 0);

		gl.glPopMatrix();
	}
}
