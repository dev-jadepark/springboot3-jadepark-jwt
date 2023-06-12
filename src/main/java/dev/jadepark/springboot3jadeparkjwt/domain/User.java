package dev.jadepark.springboot3jadeparkjwt.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Builder
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

//
//    /**
//     * 권한을 반환하는 메서드
//     * @return
//     */
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("user"));
//    }
//
//    /**
//     * 사용자의 id를 반환(고유값)
//     * @return
//     */
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    /**
//     * 계정 만료 여부 반환
//     * @return
//     */
//    @Override
//    public boolean isAccountNonExpired() {
//
//        // 만료 여부 판단하는 로직 추가하는 구간
//
//        return true;    // true 이면 만료되지 않은 상태(사용가능상태)
//    }
//
//    /**
//     * 계정 잠금 여부 반환
//     * @return
//     */
//    @Override
//    public boolean isAccountNonLocked() {
//
//        // 잠금 여부 판단하는 로직 추가 구간
//
//        return true;
//    }
//
//    /**
//     * 비밀번호 만료 여부 반환
//     * @return
//     */
//    @Override
//    public boolean isCredentialsNonExpired() {
//
//        // 비밀번호 만료 여부 판단하는 로직 추가 구간
//
//        return true;
//    }
//
//    /**
//     * 계정 사용 가능 여부 반환
//     * @return
//     */
//    @Override
//    public boolean isEnabled() {
//
//
//        return true;
//    }
}
