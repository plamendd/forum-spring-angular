package com.plamendd.forum.service;


import com.plamendd.forum.exeptions.ForumException;
import com.plamendd.forum.model.Refreshtoken;
import com.plamendd.forum.repository.RefreshtokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshtokenService {
    private final RefreshtokenRepository  refreshtokenRepository;

    public Refreshtoken generateToken(){
        Refreshtoken refreshtoken = new Refreshtoken();
            refreshtoken.setToken(UUID.randomUUID().toString());
            refreshtoken.setCreatedDate(Instant.now());
            return refreshtokenRepository.save(refreshtoken);
    }
    public void validateToken(String token) {
        refreshtokenRepository.findByToken(token)
                .orElseThrow(() -> new ForumException("Invalid refresh Token"));
    }

    public void deleteToken(String token) {
        refreshtokenRepository.deleteByToken(token);
    }

}
