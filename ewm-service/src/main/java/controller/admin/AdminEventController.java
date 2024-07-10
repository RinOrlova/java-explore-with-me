package controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.Constants;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.ADMIN_PATH + Constants.CATEGORY_PATH)
public class AdminEventController {

}
