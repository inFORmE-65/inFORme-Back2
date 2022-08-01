package com.informe.informeapisb.src.user;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.src.user.model.*;
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
        String createUserQuery = "INSERT into User (name, phone, email, password) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getPhone(), postUserReq.getEmail(), postUserReq.getPassword()};

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

    public int setProfile (int userIdx) {
        String setProfileQuery = "INSERT into Profile (userIdx) VALUES(?)";
        Object[] setProfileParams = new Object[]{userIdx};

        return this.jdbcTemplate.update(setProfileQuery, setProfileParams);
    }

    public int checkUserExist(int userIdx) {
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }


    public int updateProfile(int userIdx, PostProfileReq postProfileReq) throws BaseException {
        String updateProfileQuery = "UPDATE Profile set JA0101=?, JA0102=?, birth=?," +
                "JA0201=?, JA0202=?, JA0203=?, JA0204=?, JA0205=?," +
                "JA0301=?, JA0302=? ,JA0303=?, JA0304=?, JA0305=?, JA0306=?, JA0307=?, JA0308=?, JA0309=?, JA0310=?," +
                "JA0311=?, JA0312=?, JA0313=?, JA0314=?, JA0315=?, JA0316=?, JA0317=?, JA0318=?, JA0319=?, JA0320=?," +
                "JA0322=?, JA0323=?, JA0324=?, JA0325=?, JA0326=?, JA0327=?," +
                "JA0401=?, JA0402=?, JA0403=?, JA0404=?," +
                "JA0410=?, JA0411=?, JA0412=?, JA0413=?, JA0414=? where userIdx=?";
        Object[] updateProfileParams = new Object[]{postProfileReq.getJA0101(), postProfileReq.getJA0102(), postProfileReq.getBirth(),
                postProfileReq.getJA0201(), postProfileReq.getJA0202(), postProfileReq.getJA0203(), postProfileReq.getJA0204(), postProfileReq.getJA0205(),
                postProfileReq.getJA0301(), postProfileReq.getJA0302(), postProfileReq.getJA0303(), postProfileReq.getJA0304(), postProfileReq.getJA0305(), postProfileReq.getJA0306(), postProfileReq.getJA0307(), postProfileReq.getJA0308(), postProfileReq.getJA0309(), postProfileReq.getJA0310(),
                postProfileReq.getJA0311(), postProfileReq.getJA0312(), postProfileReq.getJA0313(), postProfileReq.getJA0314(), postProfileReq.getJA0315(), postProfileReq.getJA0316(), postProfileReq.getJA0317(), postProfileReq.getJA0318(), postProfileReq.getJA0319(), postProfileReq.getJA0320(),
                postProfileReq.getJA0322(), postProfileReq.getJA0323(), postProfileReq.getJA0324(), postProfileReq.getJA0325(), postProfileReq.getJA0326(), postProfileReq.getJA0327(),
                postProfileReq.getJA0401(), postProfileReq.getJA0402(), postProfileReq.getJA0403(), postProfileReq.getJA0404(),
                postProfileReq.getJA0410(), postProfileReq.getJA0411(), postProfileReq.getJA0412(), postProfileReq.getJA0413(), postProfileReq.getJA0414(),
                userIdx};

        return this.jdbcTemplate.update(updateProfileQuery,updateProfileParams);

    }
}
