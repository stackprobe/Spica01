package violet.gbcTunnels;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.BinTools;
import charlotte.tools.RTError;
import charlotte.tools.SockChannel;

public class PumpPacket {
	public byte[] data;

	public PumpPacket(byte[] data) {
		this.data = data;
	}

	public String url;
	public byte[] resBody;
	public List<byte[]> resDataParts = new ArrayList<byte[]>();

	public byte[] getResData() {
		if(resDataParts.size() == 0) {
			return BinTools.EMPTY;
		}
		if(2 <= resDataParts.size()) {
			byte[] tmp = BinTools.join(resDataParts);

			resDataParts.clear();
			resDataParts.add(tmp);
		}
		return resDataParts.get(0);
	}

	public void recvWhileToSize(int size, IPump nextPump) throws Exception {
		int millis = 0;

		while(getResData().length < size) {
			if(Ground.death) {
				throw new Exception("DEATH");
			}
			if(millis < 100) {
				millis++;
			}
			final int f_millis = millis;

			SockChannel.critical.unsection_a(() -> Thread.sleep(f_millis));

			{
				PumpPacket pp = new PumpPacket(BinTools.EMPTY);
				nextPump.pump(pp);
				resDataParts.addAll(pp.resDataParts);
			}
		}
	}

	public byte[] readFromResData(int size) {
		byte[] resData = getResData();

		if(resData.length < size) {
			throw new RTError("Bad size: " + size);
		}
		resDataParts.set(0, BinTools.getSubBytes(resData, size));
		return BinTools.getSubBytes(resData, 0, size);
	}
}
