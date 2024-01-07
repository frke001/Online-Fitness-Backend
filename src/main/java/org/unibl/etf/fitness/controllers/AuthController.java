package org.unibl.etf.fitness.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.fitness.models.dto.*;
import org.unibl.etf.fitness.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RequestRegisterClientDTO request){
        this.authService.registerClient(request);
    }

    @PostMapping("/activate")
    @ResponseStatus(HttpStatus.OK)
    public boolean activateAccount(@RequestBody ValidationDTO validationDTO){
        return this.authService.activateAccount(validationDTO);
    }

    @PostMapping("/resend-activation")
    public void resendActivation(@RequestBody EmailDTO emailDTO){
        this.authService.resendActivation(emailDTO);

    }
    @PostMapping("/check-details")
    public boolean checkDetails(@RequestBody DetailsRequestDTO requestDTO){
        return this.authService.checkDetails(requestDTO);
    }
    @PostMapping("/login")
    public ClientDTO login(@RequestBody RequestLoginDTO requestLoginDTO){
        return this.authService.login(requestLoginDTO);
    }
}
