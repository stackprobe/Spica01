package charlotte.tools;

import java.lang.management.ManagementFactory;

public class KernelTools {
	public static final int PID = Integer.parseInt(
			ManagementFactory.getRuntimeMXBean().getName().split("@")[0]
			);
}
