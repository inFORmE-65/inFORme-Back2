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
                        rs.getString("email")),
                getUserByEmailParams);
    }

    public int createUser(@NotNull PostUserReq postUserReq) {
        String createUserQuery = "INSERT into User (name, nickname, phone, email, password, imgUrl, status) VALUES (?,?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getNickname(), postUserReq.getPhone(), postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getImgUrl(), postUserReq.getStatus()};

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
