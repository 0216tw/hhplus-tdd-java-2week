package com.plusbackend.week2.unit.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

/*
 * DB 커넥트가 되었는지 확인하는 테스트 코드
 */

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 내가 세팅한 설정정보를 사용하도록 함 (인메모리 안할려고)
public class RepositoryTestBase {

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void close() {

    }
}
