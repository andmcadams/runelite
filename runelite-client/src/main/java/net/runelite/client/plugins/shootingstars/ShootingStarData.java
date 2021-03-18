package net.runelite.client.plugins.shootingstars;

import java.time.Instant;
import lombok.Data;
import lombok.Getter;

@Data
public class ShootingStarData
{
	@Getter
	private final int loc;

	@Getter
	private final int world;

	@Getter
	private final long minTime;

	@Getter
	private final long maxTime;

	public ShootingStarData(ShootingStarLocation loc, int world, long minTime, long maxTime)
	{
		this.loc = loc.getId();
		this.world = world;
		this.minTime = minTime;
		this.maxTime = maxTime;
	}

	public ShootingStarLocation getLocation()
	{
		return ShootingStarLocation.getLocation(this.loc);
	}

}
