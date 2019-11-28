package moonbox.mbw.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import moonbox.mbw.annotation.CurrentUser;
import moonbox.mbw.consts.Constants;
import moonbox.mbw.model.response.ResponseEntity;
import moonbox.mbw.model.response.ResponseListData;
import moonbox.mbw.security.Session;
import moonbox.mbw.service.ApplicationService;
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
