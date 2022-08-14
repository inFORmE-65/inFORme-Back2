package com.informe.informeapisb.src.auth;

import com.informe.informeapisb.src.auth.model.PostLoginReq;
import com.informe.informeapisb.src.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AuthDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User getPwd(PostLoginReq postLoginReq) {
        String getPwdQuery = "select userIdx, name, nickname, birth, phone, email, password, imgUrl, status from User where email = ? AND status='ACTIVE'";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickname"),
                        rs.getString("birth"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("imgUrl"),
                        rs.getString("status")
                ),
                getPwdParams
        );
    }

    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ? AND status='ACTIVE')";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }
}
