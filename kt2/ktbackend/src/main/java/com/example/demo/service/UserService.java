package com.example.demo.service;

import com.example.demo.dto.UserSearchDTO;
import com.example.demo.dto.emailDTO;
import com.example.demo.model.*;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.TokenUtils;
import com.example.demo.view.UserLoginView;
import com.example.demo.view.UserRegisterView;
import com.example.demo.view.UserTokenState;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public UserSearchDTO safeFindOneByEmail(emailDTO emaill) throws SQLException {
        System.out.println(emaill.getEmail());

        String sql = "select "
                + "first_name, last_name, email from users "
                + "where email = ?";


        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/postgres?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("JovanJenjic123");


        DataSource dataSource = dataSourceBuilder.build();
        Connection c = dataSource.getConnection();
        PreparedStatement p = c.prepareStatement(sql);
        p.setString(1, emaill.getEmail());
        ResultSet rs = p.executeQuery();

        String firstName = "";
        String lastName = "";
        String email = "";
        while (rs.next()) {
            firstName = rs.getString("first_name");
            lastName = rs.getString("last_name");
            email = rs.getString("email");

            System.out.println(firstName + "\t" + lastName +
                    "\t" + email);
        }

        UserSearchDTO u = new UserSearchDTO();
        u.setEmail(email);
        u.setFirstName(firstName);
        u.setLastName(lastName);

        return u;
        // omitted - process rows and return an account list
    }




    public Users register(UserRegisterView userRegisterView) {
        if (!userRegisterView.getPassword().equals(userRegisterView.getRepeatPassword()))
            return null;

        Users u = this.userService.findOneByEmail(userRegisterView.getEmail());
        if (u != null)
            return null;

        return this.userService.save(userRegisterView);
    }

    public UserTokenState login(UserLoginView user) throws NotFoundException {
        Users u = this.findOneByEmailAndPassword(user.getEmail(), user.getPassword());
        if (u == null)
            return null;

        List<Authority> auth = this.authorityService.findAllByRoleName(u.getRole().getRole());
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), auth));

        //ubaci username(email) + password u kontext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Kreiraj token
        Users userToken = (Users) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(userToken.getEmail(), userToken.getRole().getRole());
        int expiresIn = tokenUtils.getExpiredIn();

        return new UserTokenState(jwt, (long) expiresIn, userToken.isFirstTimeLogged());
    }

    public Users findOneByEmailAndPassword(String email, String password) throws NotFoundException {
        Users user = this.userRepository.findOneByEmail(email);
        if (!this.passwordEncoder.matches(password, user.getPassword()))
            throw new NotFoundException("Not existing user");

        return user;
    }

    public Users findOneByEmail(String email) {
        return this.userRepository.findOneByEmail(email);
    }

    public Users save(UserRegisterView user) {
        Users u = Users.builder().role(UserRole.valueOf("USER"))
                .firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword())).build();

        return this.userRepository.save(u);
    }
}