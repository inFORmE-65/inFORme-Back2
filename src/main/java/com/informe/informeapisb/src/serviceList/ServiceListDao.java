package com.informe.informeapisb.src.serviceList;

import com.informe.informeapisb.src.serviceList.model.*;
import com.informe.informeapisb.src.serviceList.model.hits.GetHitsServiceListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
        String insertServiceListQuery = "INSERT INTO serviceList(SVC_ID,SupportType,ServiceName,ServicePurpose,ServiceTarget,TartgetCriteria,ServiceContent,ServiceHowApply,ServiceApplyDue,ServiceUrl,ServiceAgencyCode,ServiceAgencyName,ServiceAgencyPartName,ServiceViewCount) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []insertServiceListParams = new Object[] {data.get서비스ID(),data.get지원유형(),data.get서비스명(),data.get서비스목적(),data.get지원대상(),data.get선정기준(),data.get지원내용(),data.get신청방법(),data.get신청기한(),data.get상세조회URL(),data.get소관기관코드(),data.get소관기관명(),data.get부서명(),data.get조회수()};
        this.jdbcTemplate.update(insertServiceListQuery,
                insertServiceListParams);
    }


    public GetServiceListRes getServiceList(int page, int perPage) {
        String GetServiceListQuery = "select count(*) as totalCount, currentCount from serviceList left join (select SVC_ID, count(*) as currentCount from serviceList "+ "limit " + perPage +" offset "+ (page-1)*perPage + ")";
        String GetDataQuery = "select SVC_ID, SupportType, ServiceName, ServicePurpose, ServiceTarget, TartgetCriteria, ServiceContent, ServiceHowApply, ServiceApplyDue, ServiceUrl, ServiceAgencyCode, ServiceAgencyName, ServiceAgencyPartName, ServiceViewCount from serviceList "+ "limit " + perPage +" offset "+ (page-1)*perPage;
        System.out.println(GetServiceListQuery);
        System.out.println(GetDataQuery);
        return this.jdbcTemplate.queryForObject(GetServiceListQuery,
                (rs, rowNum1) -> new GetServiceListRes(
                        page,
                        perPage,
                        rs.getInt("totalCount"),
                        rs.getInt("currentCount"),
                        rs.getInt("currentCount"),
                        data = this.jdbcTemplate.query(GetDataQuery,
                                (rk, rowNum2) -> new data(
                                        rk.getString("SVC_ID"),
                                        rk.getString("SupportType"),
                                        rk.getString("ServiceName"),
                                        rk.getString("ServicePurpose"),
                                        rk.getString("ServiceTarget"),
                                        rk.getString("TartgetCriteria"),
                                        rk.getString("ServiceContent"),
                                        rk.getString("ServiceHowApply"),
                                        rk.getString("ServiceApplyDue"),
                                        rk.getString("ServiceUrl"),
                                        rk.getString("ServiceAgencyCode"),
                                        rk.getString("ServiceAgencyName"),
                                        rk.getString("ServiceAgencyPartName"),
                                        rk.getInt("ServiceViewCount")
                                )
                        )
                ));
    }

    // 실시간 정책 조회
    public List<GetHitsServiceListRes> getHitsServiceList(int offset, int limit){
        String getHitsServiceQuery = "select SL.SVC_ID, SL.ServiceName, SL.ServiceTarget, SL.ServiceContent, SL.ServiceViewCount\n" +
                "from serviceList as SL\n" +
                "order by ServiceViewCount desc\n" +
                "limit " + limit + " offset " + (offset - 1) * limit + ";";

        return this.jdbcTemplate.query(getHitsServiceQuery,
                (rs, rowNum) -> new GetHitsServiceListRes(
                        rs.getString("SVC_ID"),
                        rs.getString("serviceName"),
                        rs.getString("serviceTarget"),
                        rs.getString("serviceContent"),
                        rs.getString("serviceViewCount")
                ));
    }
}
