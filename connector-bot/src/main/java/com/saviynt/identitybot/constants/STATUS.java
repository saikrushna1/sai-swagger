package com.saviynt.identitybot.constants;

/**
 * 
 * @author Srinivasa Reddy Challa, Raj Kumar
 * 
 *         Enum to list possible status of executing script
 *
 */
public enum STATUS {

	Submitted(100), Inprocess(101), 
	RetryInprogress(102), Success(200), 
	TargetSystemUnrechable(500), Failed(501),
	ExecutionTimedout(502), UNKNOWN(503), 
	Unauthorized(401), NotFound(404),
	BadRequest(400), Forbidden(403),Fail(417)
	;

	private int id;

	STATUS(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}
}