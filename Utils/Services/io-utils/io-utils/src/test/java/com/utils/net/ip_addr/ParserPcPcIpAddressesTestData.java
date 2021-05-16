package com.utils.net.ip_addr;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.utils.log.Logger;

class ParserPcPcIpAddressesTestData {

	@Test
	void testWork() {

		final ParserLocalIpAddresses parserLocalIpAddresses = new ParserLocalIpAddresses();
		parserLocalIpAddresses.work();

		final List<LocalIpAddressData> localIpAddressDataList = parserLocalIpAddresses.getLocalIpAddressDataList();
		assertFalse(localIpAddressDataList.isEmpty());

		Logger.printNewLine();
		for (final LocalIpAddressData localIpAddressData : localIpAddressDataList) {
			Logger.printLine(localIpAddressData);
		}
	}
}
