package org.unibl.etf.fitness.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.unibl.etf.fitness.exceptions.UnauthorizedException;
import org.unibl.etf.fitness.models.dto.JwtUserDTO;
import org.unibl.etf.fitness.repositories.ClientRepository;
import org.unibl.etf.fitness.services.JwtUserDetailsService;

@Service
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    public JwtUserDetailsServiceImpl(ClientRepository clientRepository, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return modelMapper.map(clientRepository.findByUsername(username).orElseThrow(UnauthorizedException::new), JwtUserDTO.class);
    }
}
