package com.nashs.daily_log.domain.auth.oauthPrincipal;

import com.nashs.daily_log.domain.auth.info.GoogleProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GoogleOAuth2Principal implements OAuth2User {
    private final GoogleProfile profile;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    public GoogleOAuth2Principal(GoogleProfile profile,
                                 Collection<? extends GrantedAuthority> authorities,
                                 Map<String, Object> attributes) {
        this.profile = profile;
        this.authorities = List.copyOf(authorities);
        this.attributes = Map.copyOf(attributes);
    }

    public GoogleProfile profile() { return profile; }

    @Override
    public Map<String, Object> getAttributes() { return attributes; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getName() { return profile.sub(); }
}

