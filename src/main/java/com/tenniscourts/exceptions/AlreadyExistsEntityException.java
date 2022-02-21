package com.tenniscourts.exceptions;

/**
 * The type Already exists entity exception.
 */
public class AlreadyExistsEntityException extends RuntimeException {
  /**
   * Instantiates a new Already exists entity exception.
   *
   * @param msg the msg
   */
  public AlreadyExistsEntityException(String msg){
        super(msg);
    }

    private AlreadyExistsEntityException(){}
}
