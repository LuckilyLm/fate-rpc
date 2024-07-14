import org.fate.examplespringbootconsumer.ExampleServiceImpl;
import org.fate.examplespringbootconsumer.ExampleSpringbootConsumerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author: Fate
 * @Date: 2024/7/13 18:17
 **/

@SpringBootTest(classes = ExampleSpringbootConsumerApplication.class)
public class TestService {

    @Resource
    private ExampleServiceImpl exampleService;

    @Test
    public void test() {
        exampleService.test();
    }
}
