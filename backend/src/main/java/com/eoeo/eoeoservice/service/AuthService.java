package com.eoeo.eoeoservice.service;

import com.eoeo.eoeoservice.domain.account.Account;
import com.eoeo.eoeoservice.domain.account.AccountRepository;
import com.eoeo.eoeoservice.dto.auth.*;
import com.eoeo.eoeoservice.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public UserRegisterResponseDto register(UserRegisterRequestDto request) throws Exception{

        if(!accountRepository.findByUsername(request.getUsername()).isEmpty()){
            throw new IllegalArgumentException();
        }

        byte[] bytes = new byte[16];
        String salt;

        new Random().nextBytes(bytes);
        salt = new String(bytes, Charset.forName("UTF-8"));

        Account account = Account.builder()
                .username(request.getUsername())
                .name(request.getName())
                .salt(salt)
                .password(passwordEncoder.encode(salt + request.getPassword()))
                .build();

        accountRepository.save(account);


        return UserRegisterResponseDto.builder()
                .id(account.getId())
                .build();

    }

    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto request) throws Exception{
        Account account = accountRepository.findByUsername(request.getUsername()).orElseThrow(() -> new IllegalArgumentException());


        if(!passwordEncoder.matches(account.getSalt()+request.getPassword(), account.getPassword())){
            throw new IllegalArgumentException();
        }

        String validationToken = jwtProvider.createValidationToken();
        account.setValidationToken(passwordEncoder.encode(validationToken));

        return UserLoginResponseDto.builder()
                .id(account.getId())
                .name(account.getName())
                .username(account.getUsername())
                .accessToken(jwtProvider.createAccessToken(account.getUsername()))
                .refreshToken(jwtProvider.createRefreshToken(account.getUsername(), validationToken))
                .build();
    }

    @Transactional
    public RequestNewTokenResponseDto requestNewToken(RequestNewTokenRequestDto request){

        RefreshTokenValidationDto validationDto;
        Account account;
        try{
            validationDto = jwtProvider.extractClaimAndUsername(request.getRefreshToken());
            account = accountRepository.findByUsername(validationDto.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("No such user."));

        } catch(Exception e){
            return RequestNewTokenResponseDto.builder()
                    .isValidated(false)
                    .build();
        }

        boolean validationResult = jwtProvider.validateRefreshToken(validationDto, account, passwordEncoder);

        if(validationResult){
            String validationToken = jwtProvider.createValidationToken();
            account.setValidationToken(passwordEncoder.encode(validationToken));
            return RequestNewTokenResponseDto.builder()
                    .isValidated(true)
                    .accessToken(jwtProvider.createAccessToken(account.getUsername()))
                    .refreshToken(jwtProvider.createRefreshToken(account.getUsername(), validationToken))
                    .build();
        } else{
            return RequestNewTokenResponseDto.builder()
                    .isValidated(false)
                    .build();
        }

    }



}
