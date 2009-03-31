package terrain;

import java.util.List;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.terrain.CompoundElevationModel;

public class OffsetCompoundElevationModel extends CompoundElevationModel
{
	protected double elevationOffset;

	@Override
	public double getUnmappedElevation(Angle latitude, Angle longitude)
	{
		return super.getUnmappedElevation(latitude, longitude)
				+ elevationOffset;
	}

	@Override
	public double getElevations(Sector sector, List<? extends LatLon> latlons,
			double targetResolution, double[] buffer)
	{
		double retval = super.getElevations(sector, latlons, targetResolution,
				buffer);
		for (int i = 0; i < buffer.length; i++)
		{
			buffer[i] += elevationOffset;
		}
		return retval;
	}

	public double getElevationOffset()
	{
		return elevationOffset;
	}

	public void setElevationOffset(double elevationOffset)
	{
		this.elevationOffset = elevationOffset;
	}
}