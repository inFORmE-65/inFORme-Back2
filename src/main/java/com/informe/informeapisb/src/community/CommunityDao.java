package com.informe.informeapisb.src.community;

import com.informe.informeapisb.src.community.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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

    //게시물 리스트 조회
    public List<GetPostsRes> getPosts(String type) {
        String getPostQuery;
        if(type.equals("Free")){
            getPostQuery = "SELECT postIdx,userIdx,title,content,SVC_ID,createdAt,updatedAt FROM Post WHERE SVC_ID = '' and status = 'ACTIVE' ORDER BY createdAt DESC";
        } else {
            getPostQuery = "SELECT postIdx,userIdx,title,content,SVC_ID,createdAt,updatedAt FROM Post WHERE SVC_ID != '' and status = 'ACTIVE' ORDER BY createdAt DESC";
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
                                (rk, rowNum2) -> new GetImgUrl(
                                        rk.getInt("postImgIdx"),
                                        rk.getString("imgUrl"))
                        ,rs.getInt("postIdx"))
                )
        );
    }
    
    
    //단일 게시물 조회
    public GetPostsRes getPost(int postIdx){
        String getPostQuery = "SELECT postIdx,userIdx,title,content,SVC_ID,createdAt,updatedAt FROM Post WHERE postIdx = ?";
        int getPostParam = postIdx;
        return this.jdbcTemplate.queryForObject(getPostQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("SVC_ID"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        getImgUrls = this.jdbcTemplate.query("SELECT pi.postImgIdx,pi.imgUrl FROM PostImgUrls as pi JOIN Post as p on p.postIdx = pi.postIdx WHERE pi.status = 'ACTIVE' and p.postIdx = ? ORDER BY pi.postImgIdx ASC",
                                (rk, rowNum2) -> new GetImgUrl(
                                        rk.getInt("postImgIdx"),
                                        rk.getString("imgUrl"))
                                ,rs.getInt("postIdx"))),getPostParam);
    }


    //특정 유저의 게시물 리스트 조회
    public List<GetPostsRes> getUserPosts(int userIdx) {
        String getPostsQuery = "SELECT postIdx,userIdx,title,content,SVC_ID,createdAt,updatedAt FROM Post WHERE userIdx = ? and status = 'ACTIVE' ORDER BY createdAt DESC";
        int getPostsParam = userIdx;
        return this.jdbcTemplate.query(getPostsQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("SVC_ID"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        getImgUrls = this.jdbcTemplate.query("SELECT pi.postImgIdx,pi.imgUrl FROM PostImgUrls as pi JOIN Post as p on p.postIdx = pi.postIdx WHERE pi.status = 'ACTIVE' and p.postIdx = ? ORDER BY pi.postImgIdx ASC",
                                (rk, rowNum2) -> new GetImgUrl(
                                        rk.getInt("postImgIdx"),
                                        rk.getString("imgUrl"))
                                ,rs.getInt("postIdx"))),getPostsParam);
    }

    //게시물 내용 업데이트
    public int updatePost(int postIdx,PatchPostsReq patchPostsReq) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String updatePostQuery = "UPDATE Post SET title=?,content=?,SVC_ID=?,updatedAt=? WHERE postIdx=?";
        Object []updatePostParams = new Object[] {patchPostsReq.getTitle(),patchPostsReq.getContent(),patchPostsReq.getSVC_ID(),sdf.format(timestamp),postIdx};
        return this.jdbcTemplate.update(updatePostQuery, updatePostParams);
    }

    //이미지 업데이트
    public int updateImg(GetImgUrl getImgUrl){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String updateImgQuery = "UPDATE PostImgUrls SET imgUrl = ?,updatedAt=? WHERE postImgIdx=?";
        Object []updateImgParams = new Object[] {getImgUrl.getImgUrl(),sdf.format(timestamp),getImgUrl.getPostImgIdx()};
        return this.jdbcTemplate.update(updateImgQuery, updateImgParams);
    }
}
