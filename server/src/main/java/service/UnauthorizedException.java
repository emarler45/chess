package service;

public class UnauthoriedException extends RuntimeException {
  public UnauthoriedException(String message) {
    super(message);
  }
}
