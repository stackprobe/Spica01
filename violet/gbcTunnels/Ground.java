package violet.gbcTunnels;

import java.util.Map;
import java.util.TreeMap;

import charlotte.tools.BinTools;

public class Ground {
	public static boolean death = false;
	public static Server[] servers;
	public static Pump pump;
	public static Map<byte[], Connection> connections = new TreeMap<byte[], Connection>(BinTools.comp_array);
}
