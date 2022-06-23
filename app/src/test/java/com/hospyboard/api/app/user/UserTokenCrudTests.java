package com.hospyboard.api.app.user;

import com.hospyboard.api.app.user.dto.UserTokenDTO;
import com.hospyboard.api.app.user.services.UserTokenService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTokenCrudTests {

    @Autowired
    private UserTokenService tokenService;

    @Test
    public void testCreateInaccessible() {
        try {
            tokenService.create(new UserTokenDTO());
            fail("no error detected");
        } catch (ApiBadRequestException ignored) {
        }
    }

    @Test
    public void testPatchInaccessible() {
        try {
            tokenService.update(new UserTokenDTO());
            fail("no error detected");
        } catch (ApiBadRequestException ignored) {
        }
    }

    @Test
    public void testPatchBatchInaccessible() {
        try {
            tokenService.update(new ArrayList<UserTokenDTO>());
            fail("no error detected");
        } catch (ApiBadRequestException ignored) {
        }
    }

}
