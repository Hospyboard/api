package com.hospyboard.api.app.mails.dtos;

import fr.funixgaming.api.core.mail.dtos.ApiMailDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospyboardMailDTO extends ApiMailDTO {

    @Override
    public String toString() {
        return String.format("To: %s, From: %s", this.getTo(), this.getFrom());
    }
}
