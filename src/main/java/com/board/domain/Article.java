package com.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@ToString
// 조건문 검색시 효율적이게 하기 위해
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false) private String title;  // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 내용

    @Setter private String hashtag; // 해시태그

    // article에 연동되어 있는 코멘트는 중복을 허용하지 않고, 모아서 컬렉션으로 볼 수 있기에 set으로 설정
    // 이름 설정하지 않으면 자동으로 연결, 관계 매핑 이름 생성된다.
    @ToString.Exclude // 순환 참조를 해결하기 위함
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleCommentSets = new LinkedHashSet<>();
    // Jpa Auditing 시간에 대해서 자동으로 값을 넣어준다.
    /* 각종 설정시에 config 폴더안에 config 파일을 넣는다.
    jpa 관련 설정은 JpaConfig에 넣기
    * */
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt;  // 생성일시
    @CreatedBy @Column(nullable = false, length = 100)private String createdBy;         // 생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy;        // 수정자


    // 생성자 메서드 사용할 수 있게 하기
    protected Article() {}

    // 자동으로 들어가는 값 빼고 세팅)
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // new 키워드를 사용해서 만들지 않아도 되게 하기 위함
    // 의도를 전달하기 위함(도메인 Article을 생성하기 위해서는 ~~값이 필요함)
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }


    // 컬렉션(리스트)으로 사용할 때 필요
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false; // java14 pattern-matching
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
