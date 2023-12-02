package com.rvc;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class EncYMLTest {
  @Autowired
  StringEncryptor encryptor;

  @Test
  public void getPass() {
    System.out.println(encryptor.encrypt("117.21.200.55:18848"));
    System.out.println(encryptor.encrypt("nacos"));
    System.out.println(encryptor.encrypt("keKEaiai5201314"));
  }
}