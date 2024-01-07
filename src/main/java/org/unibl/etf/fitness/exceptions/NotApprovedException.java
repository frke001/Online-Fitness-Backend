package org.unibl.etf.fitness.exceptions;

public class NotApprovedException extends RuntimeException{
    public NotApprovedException()
    {
        super("Account not approved");
    }

    public NotApprovedException(String message)
    {
        super(message);
    }
}
