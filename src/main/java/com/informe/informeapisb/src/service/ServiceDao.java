package com.informe.informeapisb.src.service;

import com.informe.informeapisb.src.service.model.GetSearchInfoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ServiceDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 정책 검색
    public List<GetSearchInfoRes> getSearchInfoResList(String word){
        String search_word = "'" + word + "'";
        String getSearchServiceQuery = "select SL.SVC_ID, SL.ServiceName, SL.ServiceTarget, SL.ServiceContent\n" +
                "from serviceList as SL\n" +
                "where SL.ServiceName like concat('%'," + search_word + ",'%');";


        return  this.jdbcTemplate.query(getSearchServiceQuery,
                (rs, rowNum) -> new GetSearchInfoRes(
                        rs.getString("SVC_ID"),
                        rs.getString("serviceName"),
                        rs.getString("serviceTarget"),
                        rs.getString("serviceContent")
                ));
    }
}
