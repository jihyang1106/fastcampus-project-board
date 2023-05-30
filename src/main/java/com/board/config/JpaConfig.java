package com.board.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    // 누가 생성했는지 알기 위한 세팅
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("uno");  // auditing 시 이 사람의 이름이 들어간다.
        // TODO : 스프링 시큐리티로 인증 기능을 붙이게 될 때 수정해야 한다.
    }
}
