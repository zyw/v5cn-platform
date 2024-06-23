package cn.v5cn.platform.system.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.v5cn.platform.framework.core.domain.R;
import cn.v5cn.platform.framework.web.core.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {

    @SaIgnore
    @GetMapping("/index")
    public R<Void> index() {
        return R.ok("成功");
    }
}
