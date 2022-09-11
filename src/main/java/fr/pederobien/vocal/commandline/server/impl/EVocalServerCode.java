package fr.pederobien.vocal.commandline.server.impl;

import fr.pederobien.commandline.ICode;

public enum EVocalServerCode implements ICode {

	// Starting application -----------------------------------------------------------------------
	VOCAL_SERVER_CL__STARTING,

	// Code when the arguments are ignored
	VOCAL_SERVER_CL__STARTING__IGNORING_ARGUMENTS__NOT_ENOUGH_ARGUMENT,

	// Stopping application -----------------------------------------------------------------------
	VOCAL_SERVER_CL__STOPPING,

	// Common codes -------------------------------------------------------------------------------

	// Code for the name completion
	VOCAL_SERVER_CL__NAME__COMPLETION,

	// Code for the port number completion
	VOCAL_SERVER_CL__PORT__COMPLETION,

	// Code for the "vocal" command ---------------------------------------------------------------
	VOCAL_SERVER_CL__ROOT__EXPLANATION,

	// Code for the "open" command ----------------------------------------------------------------
	VOCAL_SERVER_CL__OPEN__EXPLANATION,

	// Code when the server name is missing
	VOCAL_SERVER_CL__OPEN__SERVER_NAME_IS_MISSING,

	// Code when the server port number is missing
	VOCAL_SERVER_CL__OPEN__SERVER_PORT_IS_MISSING,

	// Code when the server port number has a bad format
	VOCAL_SERVER_CL__OPEN__SERVER_PORT_BAD_FORMAT,

	// Code when the server port number is out of range
	VOCAL_SERVER_CL__OPEN__SERVER_PORT_OUT_OF_RANGE,

	// Code when a vocal server is opened
	VOCAL_SERVER_CL__OPEN__SERVER_OPENED,

	// Code for the "close" command ---------------------------------------------------------------
	VOCAL_SERVER_CL__CLOSE__EXPLANATION,

	// Code when the vocal server is closed
	VOCAL_SERVER_CL__CLOSE__SERVER_CLOSED,

	// Code for the "test" command ----------------------------------------------------------------
	VOCAL_SERVER_CL__TEST__EXPLANATION,

	// Code when the test status is missing
	VOCAL_SERVER_CL__TEST__TEST_STATUS_IS_MISSING,

	// Code when the test status has a bad format
	VOCAL_SERVER_CL__TEST__TEST_STATUS_BAD_FORMAT,

	// Code when the test mode is activated
	VOCAL_SERVER_CL__TEST__TEST_MODE_ACTIVATED,

	// Code when the test mode is deactivated
	VOCAL_SERVER_CL__TEST__TEST_MODE_DEACTIVATED,

	;

	@Override
	public String getCode() {
		return name();
	}
}
