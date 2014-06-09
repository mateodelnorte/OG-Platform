package com.electronifie.trace;

public class Log {

  private Logger logger;

  public static boolean isEnabled() {
    return INSTANCE.logger != null;
  }

  /* to use this class, pass a dynamic proxy from Nodejs which
      defines an emit callback function (  function (message) { }  )
  */
  public static void setLogger(Logger logger) {
    INSTANCE.logger = logger;
  }

  public static void unsetLogger() {
    INSTANCE.logger = null;
  }

  private static final Log INSTANCE = new Log();

  public static void trace(String message) {
    INSTANCE.emit(message);
  }

  private void emit(String message) {
    if (this.logger != null ) {
      this.logger.emit(message);
    }
  }
}
