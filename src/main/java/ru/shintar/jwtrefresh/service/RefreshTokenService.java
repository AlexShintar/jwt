package ru.shintar.jwtrefresh.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shintar.jwtrefresh.model.entity.RefreshToken;
import ru.shintar.jwtrefresh.repository.TokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final TokenRepository tokenRepository;
    @Transactional
    public void save(Long userId, String refreshToken) {
        System.out.println("savesavesavesavesavesavesavesavesavesavesavesavesave");
        RefreshToken token = tokenRepository.findByUserId(userId).orElse(new RefreshToken());
        token.setUserId(userId);
        token.setValue(refreshToken);
        tokenRepository.save(token);
    }

    @Transactional
    public RefreshToken getRefreshTokenValueByUserId(Long userId) {
        System.out.println("!!!!!!!!!!!!getRefreshTokenValuseByUserId!!!!!!!!!");
        return tokenRepository.findByUserId(userId).orElse(null);
    }
}
