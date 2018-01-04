package com.teamtreehouse.techdegrees.exc;

public class ApiError extends RuntimeException {
  private final int STATUS;

  public ApiError(String message, int status) {
    super(message);
    this.STATUS = status;
  }

  public int getStatus() {
    return STATUS;
  }
}
