package com.informe.informeapisb.src.supportConditions;

import com.informe.informeapisb.src.supportConditions.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class SupportConditionsDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //SupportDetail 삽입
    public void insertSupportConditions(SupportConditionsData data) {
        String insertSupportConditionsQuery = "INSERT INTO supportConditions(SVC_ID,JA0101,JA0102,JA0103,JA0104,JA0105,JA0106,JA0107,JA0108,JA0109,JA0110,JA0111,JA0201,JA0202,JA0203,JA0204,JA0205,JA0301,JA0302,JA0303,JA0304,JA0305,JA0306,JA0307,JA0308,JA0309,JA0310,JA0311,JA0312,JA0313,JA0314,JA0315,JA0316,JA0317,JA0318,JA0319,JA0320,JA0322,JA0323,JA0324,JA0325,JA0326,JA0327,JA0401,JA0402,JA0403,JA0404,JA0410,JA0411,JA0412,JA0413,JA0414) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object []insertSupportConditionsParams = new Object[] {data.getSVC_ID(),data.getJA0101(),data.getJA0102(),data.getJA0103(),data.getJA0104(),data.getJA0105(),data.getJA0106(),data.getJA0107(),data.getJA0108(),data.getJA0109(),data.getJA0110(),data.getJA0111(),data.getJA0201(),data.getJA0202(),data.getJA0203(),data.getJA0204(),data.getJA0205(),data.getJA0301(),data.getJA0302(),data.getJA0303(),data.getJA0304(),data.getJA0305(),data.getJA0306(),data.getJA0307(),data.getJA0308(),data.getJA0309(),data.getJA0310(),data.getJA0311(),data.getJA0312(),data.getJA0313(),data.getJA0314(),data.getJA0315(),data.getJA0316(),data.getJA0317(),data.getJA0318(),data.getJA0319(),data.getJA0320(),data.getJA0322(),data.getJA0323(),data.getJA0324(),data.getJA0325(),data.getJA0326(),data.getJA0327(),data.getJA0401(),data.getJA0402(),data.getJA0403(),data.getJA0404(),data.getJA0410(),data.getJA0411(),data.getJA0412(),data.getJA0413(),data.getJA0414()};
        this.jdbcTemplate.update(insertSupportConditionsQuery,
                insertSupportConditionsParams);
    }

    // https://cho-coding.tistory.com/131
    public List<String> getAllServiceId() {
        List<String> serviceIdList=new ArrayList<>();
        serviceIdList.addAll(jdbcTemplate.queryForList("SELECT SVC_ID FROM supportConditions;", String.class));
        // for (Object content:insertSupportConditionsParams){  serviceIdList.add(content);}
        log.info("실행");
        return serviceIdList;
    }

    // 실시간 정책 조회
    public List<GetRecommendSupportConditionsRes> getRecommendSupportConditions(int page, int perPage, int age, int income_range, int gender, String area, int[]  personalArray, int[] householdslArray){

        // 소득분위 판단단
       String income_query="";
        if (0<=income_range && income_range<=50) {             // JA0201
            income_query = " JA0201='Y'";
        } else if (51<=income_range && income_range<=75) {     // JA0202
            income_query = " JA0202='Y'";
        } else if (76<=income_range && income_range<=100) {    // JA0203
            income_query = " JA0203='Y'";
        } else if (101<=income_range && income_range<=200) {   // JA0204
            income_query = " JA0204='Y'";
        } else  {                                              // JA0205
            income_query = " JA0205='Y'";
        }

        // 성별 판단
        String gender_query="";
        if (gender==0) {               // 여성
            gender_query="JA0102 ='Y'";
        } else if (gender==1) {        // 남성
            gender_query="JA0101 ='Y'";
        }

        // 개인 특성
        String personal_query=" ";
        int count1=0;
        for (int a: personalArray) {
            count1=count1+1;
            if (a==0) {
                int idx=300+count1;
                if (idx==321) {
                    idx=idx+1;
                }
                personal_query=personal_query.concat("OR JA0"+idx+" = 'Y' ");
            }
        }

        // 가구 특성
        String households__query=" ";
        int count2=0;
        for (int a: personalArray) {
            count2=count2+1;
            if (a==0) {
                int idx=400+count2;
                if (idx==405) {
                    idx=idx+5;
                }
                personal_query=personal_query.concat("OR JA0"+idx+" = 'Y' ");
            }
        }

        // String GetSupportConditionsQuery2 = "select count(*) as totalCount, count(*) as matchCount from supportConditions";
        // String GetDataQuery = "select * from supportConditions "+ "limit " + perPage +" offset "+ (page-1)*perPage;

        String getRecommendServiceQuery="select * from supportConditions where JA0110 <= "+age+" AND JA0111 >= "+age+
                " AND "+income_query+" AND "+gender_query+personal_query+households__query
                +" limit " + perPage +" offset "+ (page-1)*perPage;
        return this.jdbcTemplate.query(getRecommendServiceQuery,
                (rs, rowNum) -> new GetRecommendSupportConditionsRes(
                        rs.getString("SVC_ID"),
                        rs.getString("JA0101"),    // 남성
                        rs.getString("JA0102"),    // 여성

                        rs.getString("JA0110"),     // 대상 연령 시작
                        rs.getString("JA0111")      // 대상 연령 종료

                        /*
                        rs.getString("JA0203"),    // 76 ~ 100
                        rs.getString("JA0204"),     // 101 ~ 200

                        rs.getString("JA0301"),    // 예비부모/난임
                        rs.getString("JA0302"),    // 임신부
                        rs.getString("JA0303"),    // 출산/입양
                        rs.getString("JA0304")     // 심한 장애

                         */
                ));
    }


    public List<GetRecommendSupportConditionsRes> getRecommendSupportConditions2(int page, int perPage, int age, int income_range, int gender){

        // 소득분위 판단단
        String income_query="";
        if (0<=income_range && income_range<=50) {             // JA0201
            income_query = " JA0201='Y'";
        } else if (51<=income_range && income_range<=75) {     // JA0202
            income_query = " JA0202='Y'";
        } else if (76<=income_range && income_range<=100) {    // JA0203
            income_query = " JA0203='Y'";
        } else if (101<=income_range && income_range<=200) {   // JA0204
            income_query = " JA0204='Y'";
        } else  {                                              // JA0205
            income_query = " JA0205='Y'";
        }

        // 성별 판단
        String gender_query="";
        if (gender==0) {               // 여성
            gender_query="JA0102 ='Y'";
        } else if (gender==1) {        // 남성
            gender_query="JA0101 ='Y'";
        }



        String getRecommendServiceQuery="select * from supportConditions where JA0110 <= "+age+" AND JA0111 >= "+age+
                " AND "+income_query+" AND "+gender_query
                +" limit " + perPage +" offset "+ (page-1)*perPage;
        return this.jdbcTemplate.query(getRecommendServiceQuery,
                (rs, rowNum) -> new GetRecommendSupportConditionsRes(
                        rs.getString("SVC_ID"),
                        rs.getString("JA0101"),    // 남성
                        rs.getString("JA0102"),    // 여성

                        rs.getString("JA0110"),     // 대상 연령 시작
                        rs.getString("JA0111")      // 대상 연령 종료
                ));
    }



    public GetSupportConditionsRes getSupportConditions(int page, int perPage) {
        String GetSupportConditionsQuery = "select count(*) as totalCount, count(*) as matchCount from supportConditions";
        String GetDataQuery = "select * from supportConditions "+ "limit " + perPage +" offset "+ (page-1)*perPage;

        List<SupportConditionsData> data = this.jdbcTemplate.query(GetDataQuery,
                (rk, rowNum2) -> new SupportConditionsData(
                        rk.getString("SVC_ID"),
                        rk.getString("JA0101"),
                        rk.getString("JA0102"),
                        rk.getString("JA0103"),
                        rk.getString("JA0104"),
                        rk.getString("JA0105"),
                        rk.getString("JA0106"),
                        rk.getString("JA0107"),
                        rk.getString("JA0108"),
                        rk.getString("JA0109"),
                        rk.getString("JA0110"),
                        rk.getString("JA0111"),

                        rk.getString("JA0201"),
                        rk.getString("JA0202"),
                        rk.getString("JA0203"),
                        rk.getString("JA0204"),
                        rk.getString("JA0205"),

                        rk.getString("JA0301"),
                        rk.getString("JA0302"),
                        rk.getString("JA0303"),
                        rk.getString("JA0304"),
                        rk.getString("JA0305"),
                        rk.getString("JA0306"),
                        rk.getString("JA0307"),
                        rk.getString("JA0308"),
                        rk.getString("JA0309"),

                        rk.getString("JA0310"),
                        rk.getString("JA0311"),
                        rk.getString("JA0312"),
                        rk.getString("JA0313"),
                        rk.getString("JA0314"),
                        rk.getString("JA0315"),
                        rk.getString("JA0316"),
                        rk.getString("JA0317"),
                        rk.getString("JA0318"),
                        rk.getString("JA0319"),

                        rk.getString("JA0320"),
                        rk.getString("JA0322"),
                        rk.getString("JA0323"),
                        rk.getString("JA0324"),
                        rk.getString("JA0325"),
                        rk.getString("JA0326"),
                        rk.getString("JA0327"),

                        rk.getString("JA0401"),
                        rk.getString("JA0402"),
                        rk.getString("JA0403"),
                        rk.getString("JA0404"),

                        rk.getString("JA0410"),
                        rk.getString("JA0411"),
                        rk.getString("JA0412"),
                        rk.getString("JA0413"),
                        rk.getString("JA0414")

                ));

        return this.jdbcTemplate.queryForObject(GetSupportConditionsQuery,
                (rs, rowNum1) -> new GetSupportConditionsRes(
                        page,
                        perPage,
                        rs.getInt("totalCount"),
                        data.size(),
                        rs.getInt("matchCount"),
                        data
                ));
    }


    public List<GetProfileData> getProfile(String userIdx) {
        // String GetProfileQuery = "select count(*) as totalCount, count(*) as matchCount from supportConditions";
        // String GetDataQuery = "select * from supportConditions "+ "limit " + perPage +" offset "+ (page-1)*perPage;
        String GetProfileQuery="select * from umchwanDB.Profile where userIdx = "+userIdx;




        return this.jdbcTemplate.query(GetProfileQuery,
                (rk, rowNum1) -> new GetProfileData(
                        rk.getString("userIdx"),
                        rk.getString("birth"),
                        rk.getString("age"),
                        rk.getString("JA0101"),
                        rk.getString("JA0102"),
                        // rk.getString("JA0103"),
                        // rk.getString("JA0104"),
                        // rk.getString("JA0105"),
                        // rk.getString("JA0106"),
                        // rk.getString("JA0107"),
                        // rk.getString("JA0108"),
                        // rk.getString("JA0109"),
                        // rk.getString("JA0110"),
                        // rk.getString("JA0111"),

                        rk.getString("JA0201"),
                        rk.getString("JA0202"),
                        rk.getString("JA0203"),
                        rk.getString("JA0204"),
                        rk.getString("JA0205"),

                        rk.getString("JA0301"),
                        rk.getString("JA0302"),
                        rk.getString("JA0303"),
                        rk.getString("JA0304"),
                        rk.getString("JA0305"),
                        rk.getString("JA0306"),
                        rk.getString("JA0307"),
                        rk.getString("JA0308"),
                        rk.getString("JA0309"),

                        rk.getString("JA0310"),
                        rk.getString("JA0311"),
                        rk.getString("JA0312"),
                        rk.getString("JA0313"),
                        rk.getString("JA0314"),
                        rk.getString("JA0315"),
                        rk.getString("JA0316"),
                        rk.getString("JA0317"),
                        rk.getString("JA0318"),
                        rk.getString("JA0319"),

                        rk.getString("JA0320"),
                        rk.getString("JA0322"),
                        rk.getString("JA0323"),
                        rk.getString("JA0324"),
                        rk.getString("JA0325"),
                        rk.getString("JA0326"),
                        rk.getString("JA0327"),

                        rk.getString("JA0401"),
                        rk.getString("JA0402"),
                        rk.getString("JA0403"),
                        rk.getString("JA0404"),

                        rk.getString("JA0410"),
                        rk.getString("JA0411"),
                        rk.getString("JA0412"),
                        rk.getString("JA0413"),
                        rk.getString("JA0414")

                ));
    }


}
