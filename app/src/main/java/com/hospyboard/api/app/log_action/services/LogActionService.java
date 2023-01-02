package com.hospyboard.api.app.log_action.services;

import com.hospyboard.api.app.log_action.dtos.LogActionDTO;
import com.hospyboard.api.app.log_action.entities.LogAction;
import com.hospyboard.api.app.log_action.mappers.LogActionMapper;
import com.hospyboard.api.app.log_action.repositories.LogActionRepository;
import com.hospyboard.api.app.user.services.CurrentUser;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.utils.network.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class LogActionService extends ApiService<LogActionDTO, LogAction, LogActionMapper, LogActionRepository> {

    private final IPUtils ipUtils;
    private final CurrentUser currentUser;

    public LogActionService(LogActionRepository repository,
                            LogActionMapper mapper,
                            IPUtils ipUtils,
                            CurrentUser currentUser) {
        super(repository, mapper);
        this.ipUtils = ipUtils;
        this.currentUser = currentUser;
    }

    @Transactional
    public void logAction(final HttpServletRequest request) {
        final StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        final String queryString = request.getQueryString();
        final String fullUrl;
        final LogAction logAction = new LogAction();

        if (queryString == null) {
            fullUrl = requestURL.toString();
        } else {
            fullUrl = requestURL.append('?').append(queryString).toString();
        }

        logAction.setWhen(Date.from(Instant.now()));
        logAction.setRoute(fullUrl);
        logAction.setHttpMethod(request.getMethod());
        logAction.setIp(ipUtils.getClientIp(request));

        try {
            logAction.setUserUuid(currentUser.getCurrentUser().getId());
        } catch (ApiForbiddenException e) {
            logAction.setUserUuid(null);
        }

        getRepository().save(logAction);
    }

}
