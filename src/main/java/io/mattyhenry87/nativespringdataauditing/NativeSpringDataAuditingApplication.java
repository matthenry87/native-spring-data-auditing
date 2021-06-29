package io.mattyhenry87.nativespringdataauditing;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@EnableJpaAuditing
@SpringBootApplication
public class NativeSpringDataAuditingApplication {

    public static void main(String[] args) {

        SpringApplication.run(NativeSpringDataAuditingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(PojoRepository pojoRepository) {

        return run -> {

            pojoRepository.save(new Pojo());

            var pojos = pojoRepository.findAll();

            var pojo = pojos.get(0);

            log.info(pojo.getCreatedBy());
            log.info(pojo.getUpdatedBy());
            log.info(pojo.getCreatedDateTime().toString());
            log.info(pojo.getUpdatedDateTime().toString());
        };
    }

    @Bean
    public AuditorAware<String> auditorProvider() {

        return () -> Optional.ofNullable("Foo");
    }

    @Getter
    @Setter
    @Entity
    @EntityListeners(AuditingEntityListener.class)
    static class Pojo {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @CreatedBy
        private String createdBy;

        @LastModifiedBy
        private String updatedBy;

        @CreatedDate
        private LocalDateTime createdDateTime;

        @LastModifiedDate
        private LocalDateTime updatedDateTime;

    }


}

@Repository
interface PojoRepository extends JpaRepository<NativeSpringDataAuditingApplication.Pojo, Long> {

}