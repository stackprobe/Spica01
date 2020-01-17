package violet.gbcTunnels;

import java.util.Map;

import charlotte.tools.MapTools;

public class Ground {
	public static boolean death = false;
	public static Server[] servers;
	public static Pump pump;
	public static Map<String, Connection> connections = MapTools.<Connection>create();
}
