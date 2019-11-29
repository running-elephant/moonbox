package moonbox.mbw.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import moonbox.mbw.consts.Constants;
import moonbox.mbw.dto.Login;
import moonbox.mbw.model.response.ResponseEntity;
import moonbox.mbw.security.PasswordNotMatchException;
import moonbox.mbw.security.UserNotFoundException;
import moonbox.mbw.security.UsernameFormatException;
import moonbox.mbw.service.LoginService;
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
