package org.unibl.etf.fitness.services;

import org.unibl.etf.fitness.models.dto.*;

public interface AuthService {

    void registerClient(RequestRegisterClientDTO request);
    boolean activateAccount(ValidationDTO validationDTO);
    void resendActivation(EmailDTO emailDTO);

    boolean checkDetails(DetailsRequestDTO detailsRequestDTO);

    ClientDTO login(RequestLoginDTO requestLoginDTO);
}
