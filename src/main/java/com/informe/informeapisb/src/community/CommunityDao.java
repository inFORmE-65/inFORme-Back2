package com.informe.informeapisb.src.community;

import com.informe.informeapisb.src.community.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.util.List;

@Repository
public class CommunityDao {
    private JdbcTemplate jdbcTemplate;

    private List<GetImgUrl> getImgUrls;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //게시물 생성
    public int insertPolicyPost(int userIdx,String title,String content,String SVC_ID) {
        String insertPostQuery = "INSERT INTO Post(userIdx,title,content,SVC_ID) VALUES (?,?,?,?)";
        Object []insertPostParams = new Object[] {userIdx,title,content,SVC_ID};
        this.jdbcTemplate.update(insertPostQuery,
                insertPostParams);

        //방금 들어간 데이터의 idx 리턴
        String lastInsertIdxQuery="SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);
    }

    //게시글 이미지 삽입
    public int insertPostImg(int postIdx,int userIdx,PostImgUrl postImgUrl) {
        String insertPostQuery = "INSERT INTO PostImgUrls(postIdx,userIdx,imgUrl) VALUES (?,?,?)";
        Object []insertPostParams = new Object[] {postIdx,userIdx,postImgUrl.getImgUrl()};
        this.jdbcTemplate.update(insertPostQuery,
                insertPostParams);

        //방금 들어간 데이터의 idx 리턴
        String lastInsertIdxQuery="SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);
    }

    //게시물 조회
    public List<GetPostsRes> getPost(String type) {
        String getPostQuery;
        if(type.equals("Free")){
            getPostQuery = "SELECT postIdx,userIdx,title,content,SVC_ID,createdAt,updatedAt,status FROM Post WHERE SVC_ID = '' and status = 'ACTIVE' ORDER BY createdAt DESC";
        } else {
            getPostQuery = "SELECT postIdx,userIdx,title,content,SVC_ID,createdAt,updatedAt,status FROM Post WHERE SVC_ID != '' and status = 'ACTIVE' ORDER BY createdAt DESC";
        }

        return this.jdbcTemplate.query(getPostQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("SVC_ID"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        getImgUrls = this.jdbcTemplate.query("SELECT pi.postImgIdx,pi.imgUrl FROM PostImgUrls as pi JOIN Post as p on p.postIdx = pi.postIdx WHERE pi.status = 'ACTIVE' and p.postIdx = ? ORDER BY pi.postImgIdx ASC",
                                (rk, rownum) -> new GetImgUrl(
                                        rk.getInt("postImgIdx"),
                                        rk.getString("imgUrl"))
                        ,rs.getInt("postIdx"))
                )
        );
    }

}
