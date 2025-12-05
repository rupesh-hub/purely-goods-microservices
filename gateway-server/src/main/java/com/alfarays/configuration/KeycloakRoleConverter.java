package com.alfarays.configuration;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        var realmAccessObj = jwt.getClaims().get("realm_access");

        if (!(realmAccessObj instanceof Map<?, ?> realmAccess)) {
            return List.of();
        }

        Object rolesObj = realmAccess.get("roles");

        if (!(rolesObj instanceof Collection<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .filter(Objects::nonNull)
                .map(Object::toString) // convert safely to string
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    //    @Override
//    public Collection<GrantedAuthority> convert(Jwt source) {
//        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
//        if (realmAccess == null || realmAccess.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
//                .stream().map(roleName -> "ROLE_" + roleName)
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//        return returnValue;
//    }


}
