package com.utils.net;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.utils.log.Logger;

class FactoryLocalHostLocalIpAddressDataTest {

	@Test
	void testNewInstance() {

		final LocalHostIpAddress localHostIpAddress = FactoryLocalHostIpAddress.newInstance();
		assertNotNull(localHostIpAddress);

		Logger.printNewLine();
		Logger.printLine(localHostIpAddress);
	}
}
