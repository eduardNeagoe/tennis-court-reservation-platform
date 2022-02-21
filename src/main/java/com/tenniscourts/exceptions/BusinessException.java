package com.tenniscourts.exceptions;

/**
 * The type Business exception.
 */
public class BusinessException extends RuntimeException {
  /**
   * Instantiates a new Business exception.
   *
   * @param msg the msg
   */
  public BusinessException(String msg){
        super(msg);
    }

    private BusinessException(){}
}
