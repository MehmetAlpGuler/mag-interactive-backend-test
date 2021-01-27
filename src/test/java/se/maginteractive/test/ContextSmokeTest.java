package se.maginteractive.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.maginteractive.test.service.AccountService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ContextSmokeTest {

    @Autowired
    private AccountService service;

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
    }
}