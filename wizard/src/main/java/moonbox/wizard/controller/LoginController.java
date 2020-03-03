package moonbox.wizard.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import moonbox.wizard.consts.Constants;
import moonbox.wizard.dto.Login;
import moonbox.wizard.model.response.ResponseEntity;
import moonbox.wizard.security.PasswordNotMatchException;
import moonbox.wizard.security.UserNotFoundException;
import moonbox.wizard.security.UsernameFormatException;
import moonbox.wizard.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(description = "login api")
@RequestMapping(value = Constants.BASE_API_PATH + "/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "login")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity login(@RequestBody Login login) {
        try {
            String token = loginService.login(login.getUsername(), login.getPassword());
            return ResponseEntity.buildOkEntry(token);
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                return ResponseEntity.buildNotFoundEntry(e.getMessage());
            } else if (e instanceof PasswordNotMatchException) {
                return ResponseEntity.buildForbiddenEntry(e.getMessage());
            } else if (e instanceof UsernameFormatException) {
                return ResponseEntity.buildBadRequestEntry(e.getMessage());
            } else {
                return ResponseEntity.buildServerErrorEntry(e.getMessage());
            }
        }
    }

}
