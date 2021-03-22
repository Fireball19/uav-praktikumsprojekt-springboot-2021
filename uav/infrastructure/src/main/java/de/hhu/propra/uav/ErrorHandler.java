package de.hhu.propra.uav;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

@Profile("!test")
@SuppressWarnings("PMD.AtLeastOneConstructor")
@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(Exception.class)
  public ModelAndView handleCustomRuntimeException() {
    final ModelAndView mav = new ModelAndView("error");
    mav.addObject("message", "Ein Fehler ist aufgetreten!");
    return mav;
  }

  @ExceptionHandler(HttpClientErrorException.class)
  public ModelAndView handleHttpClientErrorException(final Exception exception) {
    final ModelAndView mav = new ModelAndView("error");
    mav.addObject("message", exception.getMessage());
    return mav;
  }
}
