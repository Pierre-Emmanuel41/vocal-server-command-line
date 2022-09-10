package fr.pederobien.vocal.commandline.server.impl;

import fr.pederobien.commandline.ICode;

public enum EVocalServerCode implements ICode {

	// Starting application ------------------------------------------------------
	VOCAL_SERVER_CL__STARTING,

	// Stopping application ------------------------------------------------------
	VOCAL_SERVER_CL__STOPPING,

	// Common codes --------------------------------------------------------------
	VOCAL_SERVER_CL__PORT__COMPLETION,

	// Code for the "vocal" command ---------------------------------------------------------------
	VOCAL_SERVER_CL__ROOT__EXPLANATION;

	@Override
	public String getCode() {
		return name();
	}
}
