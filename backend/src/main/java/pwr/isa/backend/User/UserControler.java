package pwr.isa.backend.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/users")
public class UserControler {

    @GetMapping(path= "/test")
    public String tes1() {
        return "test";
    }
}
