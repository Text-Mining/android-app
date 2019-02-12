package ir.textmining.app.listener;

public interface SelectTagEventListener {
  void tagSuccessEvent(String tagTitle);

  void tokenInvalidEvent();
}
