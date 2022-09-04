package com.hospyboard.api.app.hospital.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkRoomAndPatientDTO {
    @NotNull
    private UUID roomId;
    @NotNull
    private UUID patientId;
}
