package cn.dawnings.wrdb;

import cn.dawnings.wrdb.domains.base.PageDto;
import cn.dawnings.wrdb.domains.bo.demo.TestPageBo;
import cn.dawnings.wrdb.domains.dto.demo.TestPageDto;
import cn.dawnings.wrdb.service.DemoService;
import cn.dawnings.wrdb.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
class WrdbBackendApplicationTests {

    @Autowired
    TestService testService;

    @Test
    void contextLoads() {
        Flux<TestPageDto> data1 =  testService.findAll();
        System.out.println("查找所有数据："+data1.toString());
        //

        TestPageBo bo = new TestPageBo();
        bo.setNameLikeAll("111");
        bo.setPage(1);
        bo.setLength(100);
        Flux<TestPageDto> data2 = testService.page(bo);
        System.out.println("单条数据(page)："+data2.toString());
        Mono<Integer> data3 = testService.count(bo);
        System.out.println("总条数(count)："+data3.toFuture());
        Mono<PageDto<TestPageDto>> data4 = testService.page2(bo);
        System.out.println("单条数据(page2)："+data4.name("111"));
        Mono<PageDto<TestPageDto>> data5 = testService.page3(bo);
        System.out.println("单条数据(page3)："+data5.toFuture());
        Mono<PageDto<TestPageDto>> data6 = testService.page4(bo);
        System.out.println("单条数据(page4)："+data6.flux());
    }

}
