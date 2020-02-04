package wb.t20200125_GBCTunnels;

public class Ground {
	public static boolean death = false;
	public static Server[] servers;
	public static ThreadLocal<Connection> connections = new ThreadLocal<Connection>();
}
