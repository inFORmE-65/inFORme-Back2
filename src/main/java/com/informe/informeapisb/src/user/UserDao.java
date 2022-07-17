package com.informe.informeapisb.src.user;

import com.informe.informeapisb.src.user.model.GetUserRes;
import com.informe.informeapisb.src.user.model.PostUserReq;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetUserRes getUserByEmail(String email) {
        String getUsersByEmailQuery = "select userIdx, name, nickname, email from User where email=?";
        String getUserByEmailParams = email;
        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("email")),
                getUserByEmailParams);
    }

    public int createUser(@NotNull PostUserReq postUserReq) {
        String createUserQuery = "INSERT into User (name, phone, email, password) VALUES (?,?,?,?)";

        // 이게 문제!!!!!
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getPhone(), postUserReq.getEmail(), postUserReq.getPassword()};

        // 이걸로 test했을 때는 성공함.. 그런거 보면 저 createUserParams가 문제인데 뭐가 문제일까...
        Object[] createUserTest = new Object[]{"신아름", "01051102831", "ocar1115@naver.com", "dkfmaekdns"};

        System.out.println(createUserTest[0].getClass().getName());
        for (int i = 0; i<createUserTest.length; i++) {
            System.out.println(createUserTest[i]);
        }

        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }
}
