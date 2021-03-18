package net.runelite.client.plugins.shootingstars;

import lombok.Getter;

public enum ShootingStarLocation
{
	ASGARNIA(0, "Asgarnia"),
	KARAMJA(1, "Karamja"),
	FELDIP_HILLS(2, "Feldip Hills"),
	FOSSIL_ISLAND(3, "Fossil Island"),
	FREMENNIK(4, "Fremennik"),
	KOUREND(5, "Kourend"),
	KANDARIN(6, "Kandarin"),
	KEBOS(7, "Kebos Lowlands"),
	KHARIDIAN_DESERT(8, "Kharidian Desert"),
	MISTHALIN(9, "Misthalin"),
	MORYTANIA(10, "Morytania"),
	PISCATORIS(11, "Piscatoris"),
	TIRANNWN(12, "Tirannwn"),
	WILDERNESS(13, "Wilderness"),
	UNKNOWN(14, "Unknown");

	@Getter
	private int id;
	@Getter
	private String name;

	ShootingStarLocation(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	private static final String asgarnia = "Asgarnia";
	private static final String karamja = "Karamja";
	private static final String feldipHills = "Feldip";
	private static final String fossilIsland = "Fossil";
	private static final String fremennik = "Fremennik";
	private static final String kourend = "Kourend";
	private static final String kandarin = "Kandarin";
	private static final String kebos = "Kebos";
	private static final String kharidianDesert = "Kharidian";
	private static final String misthalin = "Misthalin";
	private static final String morytania = "Morytania";
	private static final String piscatoris = "Piscatoris";
	private static final String tirannwn = "Tirannwn";
	private static final String wilderness = "Wilderness";
	public static ShootingStarLocation determineLocation(String text)
	{
		text = text.replace("<br>", " ");
		if(text.contains(asgarnia))
			return ShootingStarLocation.ASGARNIA;
		if(text.contains(karamja))
			return ShootingStarLocation.KARAMJA;
		if(text.contains(feldipHills))
			return ShootingStarLocation.FELDIP_HILLS;
		if(text.contains(fossilIsland))
			return ShootingStarLocation.FOSSIL_ISLAND;
		if(text.contains(fremennik))
			return ShootingStarLocation.FREMENNIK;
		if(text.contains(kourend))
			return ShootingStarLocation.KOUREND;
		if(text.contains(kandarin))
			return ShootingStarLocation.KANDARIN;
		if(text.contains(kebos))
			return ShootingStarLocation.KEBOS;
		if(text.contains(kharidianDesert))
			return ShootingStarLocation.KHARIDIAN_DESERT;
		if(text.contains(misthalin))
			return ShootingStarLocation.MISTHALIN;
		if(text.contains(morytania))
			return ShootingStarLocation.MORYTANIA;
		if(text.contains(piscatoris))
			return ShootingStarLocation.PISCATORIS;
		if(text.contains(tirannwn))
			return ShootingStarLocation.TIRANNWN;
		if(text.contains(wilderness))
			return ShootingStarLocation.WILDERNESS;
		return ShootingStarLocation.UNKNOWN;
	}

	public static ShootingStarLocation getLocation(int id)
	{
		for (ShootingStarLocation l : values())
			if(l.getId() == id)
				return l;

		return ShootingStarLocation.UNKNOWN;
	}
}
