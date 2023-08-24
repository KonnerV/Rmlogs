package com.rmlogsClient;

import net.fabricmc.api.ClientModInitializer;

import static com.rmlogsClient.RmlogsKeybind.registerKeybind;


public class RmlogsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		registerKeybind();
	}
}