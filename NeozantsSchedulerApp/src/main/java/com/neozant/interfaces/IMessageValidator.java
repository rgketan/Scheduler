package com.neozant.interfaces;

import com.neozant.request.GenericRequestType;
import com.neozant.response.GenericResponse;

public interface IMessageValidator {

	public GenericResponse validateMessage(GenericRequestType request);
}
