package com.informe.informeapisb.src.supportConditions;

import com.informe.informeapisb.src.supportConditions.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.util.List;


@Repository
public class SupportConditionsDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //SupportDetail 삽입
    public void insertSupportConditions(data data) {
        String insertSupportConditionsQuery = "INSERT INTO supportConditions(SVC_ID,JA0101,JA0102,JA0103,JA0104,JA0105,JA0106,JA0107,JA0108,JA0109,JA0110,JA0111,JA0201,JA0202,JA0203,JA0204,JA0205,JA0301,JA0302,JA0303,JA0304,JA0305,JA0306,JA0307,JA0308,JA0309,JA0310,JA0311,JA0312,JA0313,JA0314,JA0315,JA0316,JA0317,JA0318,JA0319,JA0320,JA0322,JA0323,JA0324,JA0325,JA0326,JA0327,JA0401,JA0402,JA0403,JA0404,JA0410,JA0411,JA0412,JA0413,JA0414) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []insertSupportConditionsParams = new Object[] {data.getSVC_ID(),data.getJA0101(),data.getJA0102(),data.getJA0103(),data.getJA0104(),data.getJA0105(),data.getJA0106(),data.getJA0107(),data.getJA0108(),data.getJA0109(),data.getJA0110(),data.getJA0111(),data.getJA0201(),data.getJA0202(),data.getJA0203(),data.getJA0204(),data.getJA0205(),data.getJA0301(),data.getJA0302(),data.getJA0303(),data.getJA0304(),data.getJA0305(),data.getJA0306(),data.getJA0307(),data.getJA0308(),data.getJA0309(),data.getJA0310(),data.getJA0311(),data.getJA0312(),data.getJA0313(),data.getJA0314(),data.getJA0315(),data.getJA0316(),data.getJA0317(),data.getJA0318(),data.getJA0319(),data.getJA0320(),data.getJA0322(),data.getJA0323(),data.getJA0324(),data.getJA0325(),data.getJA0326(),data.getJA0327(),data.getJA0401(),data.getJA0402(),data.getJA0403(),data.getJA0404(),data.getJA0410(),data.getJA0411(),data.getJA0412(),data.getJA0413(),data.getJA0414()};
        this.jdbcTemplate.update(insertSupportConditionsQuery,
                insertSupportConditionsParams);
    }

}
