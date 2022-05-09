package com.hospyboard.api.app.user;

import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.entity.User;
import com.hospyboard.api.app.user.entity.UserToken;
import com.hospyboard.api.app.user.mappers.UserMapper;
import com.hospyboard.api.app.user.mappers.UserTokenMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UserMappersTests {

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void tokenMappperToEntity() {
        final Date now = Date.from(Instant.now());

        final UserTokenDTO tokenDTO = new UserTokenDTO();
        tokenDTO.setToken("smlkdfsml");
        tokenDTO.setUser(null);
        tokenDTO.setExpirationDate(now);
        tokenDTO.setId(UUID.randomUUID());
        tokenDTO.setCreatedAt(now);
        tokenDTO.setUpdatedAt(now);

        final UserToken token = userTokenMapper.toEntity(tokenDTO);
        assertEquals(tokenDTO.getToken(), token.getToken());
        assertEquals(tokenDTO.getExpirationDate(), token.getExpirationDate());
        assertEquals(tokenDTO.getId(), token.getUuid());
        assertEquals(tokenDTO.getCreatedAt(), token.getCreatedAt());
        assertEquals(tokenDTO.getUpdatedAt(), token.getUpdatedAt());
    }

    @Test
    public void tokenMapperPatch() {
        final Date now = Date.from(Instant.now());
        final String token = "smlkdfsml";

        final UserToken token1 = new UserToken();
        token1.setToken(token);
        token1.setUser(new User());
        token1.setExpirationDate(now);
        token1.setId(1L);
        token1.setUuid(UUID.randomUUID());
        token1.setCreatedAt(now);
        token1.setUpdatedAt(now);

        final UserToken token2 = new UserToken();
        token2.setToken("toPtach");
        token2.setUser(new User());
        token2.setExpirationDate(now);
        token2.setId(1L);
        token2.setUuid(UUID.randomUUID());
        token2.setCreatedAt(now);
        token2.setUpdatedAt(now);

        userTokenMapper.patch(token1, token2);

        assertEquals(token, token2.getToken());
        assertEquals(token1.getExpirationDate(), token2.getExpirationDate());
        assertEquals(token1.getId(), token2.getId());
        assertEquals(token1.getUuid(), token2.getUuid());
        assertEquals(token1.getCreatedAt(), token2.getCreatedAt());
        assertEquals(token1.getUpdatedAt(), token2.getUpdatedAt());
    }

    @Test
    public void userMapperPatch() {
        final Date now = Date.from(Instant.now());
        final String username = "smlkdfsml";

        final User user1 = new User();
        user1.setId(1L);
        user1.setUsername(username);
        user1.setPassword("qmsldj");
        user1.setUuid(UUID.randomUUID());
        user1.setEmail("qlsmdml@qklsdj.fr");
        user1.setLastName("dsqd");
        user1.setFirstName("qsd");
        user1.setCreatedAt(now);
        user1.setUpdatedAt(now);
        user1.setRole("sdfs");
        user1.setTokens(new HashSet<>());

        final User user2 = new User();
        user2.setId(1L);
        user2.setUsername(username);
        user2.setPassword("qmsldj");
        user2.setUuid(UUID.randomUUID());
        user2.setEmail("qlsmdml@qklsdj.fr");
        user2.setLastName("dsqd");
        user2.setFirstName("qsd");
        user2.setCreatedAt(now);
        user2.setUpdatedAt(now);
        user2.setRole("sdfs");
        user2.setTokens(new HashSet<>());

        userMapper.patch(user1, user2);
        assertEquals(user1.getId(), user2.getId());
        assertEquals(username, user2.getUsername());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getUuid(), user2.getUuid());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getLastName(), user2.getLastName());
        assertEquals(user1.getFirstName(), user2.getFirstName());
        assertEquals(user1.getCreatedAt(), user2.getCreatedAt());
        assertEquals(user1.getUpdatedAt(), user2.getUpdatedAt());
        assertEquals(user1.getRole(), user2.getRole());
        assertEquals(user1.getTokens(), user2.getTokens());
    }

}
