package com.hisabKitab.springProject.exception;

public class EntityAlreadyExistException extends Exception{
	
	
	private static final long serialVersionUID = 1L;

	public EntityAlreadyExistException() {

        super();

    }

    public EntityAlreadyExistException(String message) {

        super(message);

    }


}
