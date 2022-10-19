package cn.dawnings.wrdb.web.demo;


import cn.dawnings.wrdb.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cyp
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestWeb {

    private final TestService testService;




}
