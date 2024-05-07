package com.auth.user.services;

import com.auth.user.Models.Token;
import com.auth.user.Models.User;
import com.auth.user.Repository.TokenRepository;
import com.auth.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class Userservice {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public User signup(String name, String email, String password)
    {
        //skipping email verification password
        Optional<User> optionalUser=userRepository.findByEmail(email);

        if(optionalUser.isPresent())
        {
            //throw user is already present
        }
       // if not create user
        User user=new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));//password we cant direct store right
        //for that we we need to store the becrypt encoder and decoder
        return userRepository.save(user);
    }


    public Token login(String email, String password) {
        Optional<User> optionalUser=userRepository.findByEmail(email);
        if(optionalUser.isEmpty())
        {
            //user is not valid
            return null;
        }
      User user=  optionalUser.get();
        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword()))
        {
            //throw password not matching
            return null;
        }
        Token token=new Token();
        token.setUser(user);
        token.setExpirydate(get30daysLater());
        token.setValue(UUID.randomUUID().toString());
        return tokenRepository.save(token);
    }
    private Date get30daysLater()
    {
        Date date=new Date();
       Calendar calendar= Calendar.getInstance();
       calendar.setTime(date);

       calendar.add(Calendar.DAY_OF_MONTH,30);

       return calendar.getTime();

    }

    public void logout(String token) {

        //if token is already deleted,no need to do anything,
        //if the token is already expired, no need
        //else isdeleted true;

        Optional<Token> optionalToken=tokenRepository.findByValueAndIsDeletedEquals(token,false);
        if(optionalToken.isEmpty())
        {
            //throw an exception saving token is not present or alreadt deleted
            return ;
        }
        Token updatedtoken=optionalToken.get();
        updatedtoken.setDeleted(true);
        tokenRepository.save(updatedtoken);

    }

    public Boolean validateToken(String token) {

        /*
        1.chech if the toekn is present in db
        2.check if the toekn is not deleted
        3.check the token is not expired
         */
      Optional<Token> optionalToken=  tokenRepository.findByValueAndIsDeletedEqualsAndExpirydateGreaterThan(token,false,new Date());
      return optionalToken.isPresent();


    }
}
