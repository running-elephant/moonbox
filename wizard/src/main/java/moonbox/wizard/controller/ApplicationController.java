package moonbox.wizard.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import moonbox.wizard.annotation.CurrentUser;
import moonbox.wizard.consts.Constants;
import moonbox.wizard.model.response.ResponseEntity;
import moonbox.wizard.model.response.ResponseListData;
import moonbox.wizard.security.Session;
import moonbox.wizard.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@Api(description = "application api")
@RequestMapping(value = Constants.BASE_API_PATH + "/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService appService;

    @ApiOperation(value = "list all applications")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity listApps(@ApiIgnore @CurrentUser Session session) {
        return ResponseEntity.buildOkEntry(new ResponseListData<>(appService.listApps()));
    }

}
