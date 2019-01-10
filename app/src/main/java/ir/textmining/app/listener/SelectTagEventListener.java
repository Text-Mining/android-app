package ir.textmining.app.listener;

public interface SelectTagEventListener {
  void tagSuccess(String tagTitle);

  void tokenInvalid();
}
