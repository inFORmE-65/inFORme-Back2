package com.informe.informeapisb.src.serviceDetail;

import com.informe.informeapisb.src.serviceDetail.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.util.List;


@Repository
public class ServiceDetailDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //ServiceDetail 삽입
    public void insertServiceDetail(data data) {
        String insertServiceDetailQuery = "INSERT INTO serviceDetail(SVC_ID,SupportType,ServiceName,ServicePurpose,ServiceApplyDue,ServiceTarget,TartgetCriteria,ServiceContent,ServiceHowApply,RequiredDocuments,AcceptAgencyName,ServiceAgencyPhone,ServiceUrl,updatedAt,ServiceAgencyName,AdministrationRule,SelfGoverningLaws,statute) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []insertServiceDetailParams = new Object[] {data.getSVC_ID(),data.get지원유형(),data.get서비스명(),data.get서비스목적(),data.get신청기한(),data.get지원대상(),data.get선정기준(),data.get지원내용(),data.get신청방법(),data.get구비서류(),data.get접수기관명(),data.get문의처전화번호(),data.get온라인신청사이트URL(),data.get수정일시(),data.get소관기관명(),data.get행정규칙(),data.get자치법규(),data.get법령()};
        this.jdbcTemplate.update(insertServiceDetailQuery,
                insertServiceDetailParams);
    }

}
