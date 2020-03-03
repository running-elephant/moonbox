
package moonbox.wizard.handler;


import lombok.extern.slf4j.Slf4j;
import moonbox.wizard.model.response.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    private ResponseEntity runtimeExceptionHandler(HttpServletRequest request, Exception e) {

        log.error(request.getRequestURI(), e);

        return ResponseEntity.buildServerErrorEntry(e.getMessage());
    }
}
