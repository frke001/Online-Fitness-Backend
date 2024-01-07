package org.unibl.etf.fitness.exceptions;

public class AccountBlockedException extends RuntimeException {


    public AccountBlockedException()
    {
        super("Your account is blocked!");
    }

    public AccountBlockedException(String message)
    {
        super(message);
    }

}

