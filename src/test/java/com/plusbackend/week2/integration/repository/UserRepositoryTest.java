package com.plusbackend.week2.integration.repository;

/*
* 사용자 DB 테이블 생성 및 데이터 적재 테스트
*/

import com.plusbackend.week2.domain.User;
import com.plusbackend.week2.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/*
 * 테스트 정보
 * User 테이블에 정상 insert 되는지 확인
 */
public class UserRepositoryTest extends RepositoryTestBase{


    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback
    public void insert() {
        //given
        User user1 = new User(1L , "테스트1" , "010-1111-1111");
        userRepository.save(user1);
        User user2 = new User(2L , "테스트2" , "010-2222-2222");
        userRepository.save(user2);

        //when
        List<User> users = userRepository.findAll();

        for(User user : users) {
            System.out.println(user.getUserName());
        }

        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);
        Assertions.assertThat(users).extracting(User::getUserName).contains("테스트1", "테스트2");

    }


}
