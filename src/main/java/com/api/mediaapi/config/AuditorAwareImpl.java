package com.api.mediaapi.config;

import com.api.mediaapi.config.authentication.AuthenticationUserProvider;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
@RequiredArgsConstructor
public class AuditorAwareImpl  implements AuditorAware<Long> {

    private final AuthenticationUserProvider authenticationUserProvider;

    @Override
    public @NotNull Optional<Long> getCurrentAuditor() {
        Long userId = authenticationUserProvider.getUserId();
        if (userId != null) {
            return Optional.of(userId);
        }
        return Optional.empty();
    }
}
