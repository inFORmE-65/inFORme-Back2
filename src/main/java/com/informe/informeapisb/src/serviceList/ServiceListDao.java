package com.informe.informeapisb.src.serviceList;

import com.informe.informeapisb.src.serviceList.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;


@Repository
public class ServiceListDao {
    private JdbcTemplate jdbcTemplate;
    private List<data> data;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //ServiceList 삽입
    public void insertServiceList(data data) {
        String insertServiceListQuery = "INSERT INTO serviceList(SVC_ID,SupportType,ServiceName,ServicePurpose,ServiceTarget,TargetCriteria,ServiceContent,ServiceHowApply,ServiceApplyDue,ServiceUrl,ServiceAgencyCode,ServiceAgencyName,ServiceAgencyPartName,ServiceViewCount) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []insertServiceListParams = new Object[] {data.get서비스ID(),data.get지원유형(),data.get서비스명(),data.get서비스목적(),data.get지원대상(),data.get선정기준(),data.get지원내용(),data.get신청방법(),data.get신청기한(),data.get상세조회URL(),data.get소관기관코드(),data.get소관기관명(),data.get부서명(),data.get조회수()};
        this.jdbcTemplate.update(insertServiceListQuery,
                insertServiceListParams);
    }


    public GetServiceListRes getServiceList(int page, int perPage) {
        String GetServiceListQuery = "select count(*) as totalCount, count(*) as matchCount from serviceList";
        String GetDataQuery = "select * from serviceList "+ "limit " + perPage +" offset "+ (page-1)*perPage;

        List<data> data = this.jdbcTemplate.query(GetDataQuery,
                (rk, rowNum2) -> new data(
                        rk.getString("SVC_ID"),
                        rk.getString("SupportType"),
                        rk.getString("ServiceName"),
                        rk.getString("ServicePurpose"),
                        rk.getString("ServiceTarget"),
                        rk.getString("TargetCriteria"),
                        rk.getString("ServiceContent"),
                        rk.getString("ServiceHowApply"),
                        rk.getString("ServiceApplyDue"),
                        rk.getString("ServiceUrl"),
                        rk.getString("ServiceAgencyCode"),
                        rk.getString("ServiceAgencyName"),
                        rk.getString("ServiceAgencyPartName"),
                        rk.getInt("ServiceViewCount")
                ));

        return this.jdbcTemplate.queryForObject(GetServiceListQuery,
                (rs, rowNum1) -> new GetServiceListRes(
                        page,
                        perPage,
                        rs.getInt("totalCount"),
                        data.size(),
                        rs.getInt("matchCount"),
                        data
                ));
    }
}
