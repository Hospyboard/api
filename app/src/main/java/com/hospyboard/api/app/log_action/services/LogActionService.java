package com.hospyboard.api.app.log_action.services;

import com.hospyboard.api.app.log_action.dtos.LogActionDTO;
import com.hospyboard.api.app.log_action.entities.LogAction;
import com.hospyboard.api.app.log_action.mappers.LogActionMapper;
import com.hospyboard.api.app.log_action.repositories.LogActionRepository;
import fr.funixgaming.api.core.crud.services.ApiService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;

@Service
public class LogActionService extends ApiService<LogActionDTO, LogAction, LogActionMapper, LogActionRepository> {

    public LogActionService(LogActionRepository repository,
                            LogActionMapper mapper) {
        super(repository, mapper);
    }

    @Async
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
        logAction.setIp(request.getRemoteAddr());

        getRepository().save(logAction);
    }

}
