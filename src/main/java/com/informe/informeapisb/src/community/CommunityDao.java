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
    private List<GetImgData> ImgDataList;
    private List<GetPostCommentDetail> CommentList;
    private List<GetPostComment> childCommentList;
    private List<GetPostLikeNick> likeNickList;

    String likeNickListQuery = "SELECT u.nickname\n" +
            "FROM Post as p\n" +
            "         JOIN (select postIdx, userIdx, status from PostLike where status = 'ACTIVE') ln on ln.postIdx = p.postIdx\n" +
            "         LEFT JOIN (select userIdx, nickname from User) u on u.userIdx = ln.userIdx\n" +
            "WHERE p.status = 'ACTIVE' and p.postIdx = ?\n" +
            "ORDER BY p.createdAt DESC";

    String ImgDataListQuery = "SELECT pi.postImgIdx,pi.origFileName,pi.fileName,pi.filePath,pi.fileSize\n"+
            "FROM PostImgData as pi\n"+
            "   JOIN Post as p on p.postIdx = pi.postIdx\n"+
            "WHERE pi.status = 'ACTIVE' and p.postIdx = ?\n"+
            "ORDER BY pi.postImgIdx ASC";

    String childCommentListQuery = "SELECT pc.commentIdx,pc.userIdx,pc.postIdx,u.nickname,IF(pc.status = 'ACTIVE', pc.content, '삭제된 댓글입니다') as content,pc.parentCommentIdx,pc.status,pc.createdAt,pc.updatedAt\n" +
            "FROM PostComment as pc\n" +
            "         LEFT JOIN (select userIdx, nickname from User) u on u.userIdx = pc.userIdx\n" +
            "WHERE pc.parentCommentIdx = ?\n" +
            "ORDER BY pc.createdAt ASC";

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

    //게시글 이미지 데이터 삽입
    public int insertPostImg(int postIdx, int userIdx, PostImgData img) {
        String insertPostQuery = "INSERT INTO PostImgData(postIdx,userIdx,origFileName,fileName,filePath,fileSize) VALUES (?,?,?,?,?,?)";
        Object []insertPostParams = new Object[] {postIdx,userIdx,img.getOrigFileName(),img.getFileName(),img.getFilePath(),img.getFileSize()};
        this.jdbcTemplate.update(insertPostQuery,
                insertPostParams);

        //방금 들어간 데이터의 idx 리턴
        String lastInsertIdxQuery="SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);
    }

    //게시물 리스트 조회
    public List<GetPostsRes> getPosts(String type, int userIdx) {
        String getPostQuery;
        if(type.equals("Free")){
            getPostQuery = "SELECT p.postIdx,p.userIdx,u.nickname,ifnull(sL.ServiceName,'자유 게시물')as ServiceName,p.title,p.content,p.SVC_ID,IF(postLikeCount is null, 0, postLikeCount) as postLikeCount,IF(pl.status = 'ACTIVE', 'Y', 'N') as postLikeCheck,p.createdAt,p.updatedAt\n"+
                    "FROM Post as p\n"+
                    "   LEFT JOIN (select SVC_ID, serviceName from serviceList) sL on sL.SVC_ID = p.SVC_ID\n"+
                    "   LEFT JOIN (select postIdx, userIdx, count(postlikeIdx) as postLikeCount from PostLike where status = 'ACTIVE' group by postIdx) plc on plc.postIdx = p.postIdx\n"+
                    "   LEFT JOIN (select postIdx, userIdx, status from PostLike where userIdx =?) pl on pl.postIdx = p.postIdx\n"+
                    "   LEFT JOIN (select userIdx, nickname from User) u on u.userIdx = p.userIdx\n"+
                    "WHERE p.status = 'ACTIVE' and p.SVC_ID = ''\n"+
                    "ORDER BY p.createdAt DESC";
        }
        else{
            getPostQuery = "SELECT p.postIdx,p.userIdx,u.nickname,ifnull(sL.ServiceName,'자유 게시물')as ServiceName,p.title,p.content,p.SVC_ID,IF(postLikeCount is null, 0, postLikeCount) as postLikeCount,IF(pl.status = 'ACTIVE', 'Y', 'N') as postLikeCheck,p.createdAt,p.updatedAt\n"+
            "FROM Post as p\n"+
            "   LEFT JOIN (select SVC_ID, serviceName from serviceList) sL on sL.SVC_ID = p.SVC_ID\n"+
            "   LEFT JOIN (select postIdx, userIdx, count(postlikeIdx) as postLikeCount from PostLike where status = 'ACTIVE' group by postIdx) plc on plc.postIdx = p.postIdx\n"+
            "   LEFT JOIN (select postIdx, userIdx, status from PostLike where userIdx =?) pl on pl.postIdx = p.postIdx\n"+
            "   LEFT JOIN (select userIdx, nickname from User) u on u.userIdx = p.userIdx\n"+
            "WHERE p.status = 'ACTIVE' and p.SVC_ID != ''\n"+
            "ORDER BY p.createdAt DESC";
        }

        int getPostsParam = userIdx;
        return this.jdbcTemplate.query(getPostQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("ServiceName"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("SVC_ID"),
                        rs.getInt("postLikeCount"),
                        likeNickList = this.jdbcTemplate.query(likeNickListQuery,
                                (rk2, rowNum3) -> new GetPostLikeNick(
                                        rk2.getString("nickname")
                                ),rs.getInt("postIdx")),
                        rs.getString("postLikeCheck"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        ImgDataList = this.jdbcTemplate.query(ImgDataListQuery,
                                (rk, rowNum2) -> new GetImgData(
                                        rk.getInt("postImgIdx"),
                                        rk.getString("origFileName"),
                                        rk.getString("fileName"),
                                        rk.getString("filePath"),
                                        rk.getLong("fileSize"))
                        ,rs.getInt("postIdx"))),getPostsParam);
    }
    
    
    //단일 게시물 조회
    public GetPostsRes getPost(int postIdx, int userIdx){
        String getPostQuery = "SELECT p.postIdx,p.userIdx,u.nickname,ifnull(sL.ServiceName,'자유 게시물')as ServiceName,p.title,p.content,p.SVC_ID,IF(postLikeCount is null, 0, postLikeCount) as postLikeCount,IF(pl.status = 'ACTIVE', 'Y', 'N') as postLikeCheck,p.createdAt,p.updatedAt\n"+
                "FROM Post as p\n"+
                "   LEFT JOIN (select SVC_ID, serviceName from serviceList) sL on sL.SVC_ID = p.SVC_ID\n"+
                "   LEFT JOIN (select postIdx, userIdx, count(postlikeIdx) as postLikeCount from PostLike where status = 'ACTIVE' group by postIdx) plc on plc.postIdx = p.postIdx\n"+
                "   LEFT JOIN (select postIdx, userIdx, status from PostLike where userIdx =?) pl on pl.postIdx = p.postIdx\n"+
                "   LEFT JOIN (select userIdx, nickname from User) u on u.userIdx = p.userIdx\n"+
                "WHERE p.status = 'ACTIVE' and p.postIdx=?";
        Object []getPostParams = new Object[] {userIdx,postIdx};
        return this.jdbcTemplate.queryForObject(getPostQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("ServiceName"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("SVC_ID"),
                        rs.getInt("postLikeCount"),
                        likeNickList = this.jdbcTemplate.query(likeNickListQuery,
                                (rk2, rowNum3) -> new GetPostLikeNick(
                                        rk2.getString("nickname")
                                ),rs.getInt("postIdx")),
                        rs.getString("postLikeCheck"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        ImgDataList = this.jdbcTemplate.query(ImgDataListQuery,
                                (rk, rowNum2) -> new GetImgData(
                                        rk.getInt("postImgIdx"),
                                        rk.getString("origFileName"),
                                        rk.getString("fileName"),
                                        rk.getString("filePath"),
                                        rk.getLong("fileSize"))
                                ,rs.getInt("postIdx"))),getPostParams);
    }

    //단일 게시물 세부사항 조회
    public GetPostDetailRes getPostDetail(int postIdx, int userIdx){
        String getPostQuery = "SELECT p.postIdx,p.userIdx,u.nickname,ifnull(sL.ServiceName,'자유 게시물')as ServiceName,p.title,p.content,p.SVC_ID,IF(postLikeCount is null, 0, postLikeCount) as postLikeCount,IF(pl.status = 'ACTIVE', 'Y', 'N') as postLikeCheck,p.createdAt,p.updatedAt,CommentCount\n" +
                "FROM Post as p\n" +
                "    LEFT JOIN (select SVC_ID, serviceName from serviceList) sL on sL.SVC_ID = p.SVC_ID\n" +
                "    LEFT JOIN (select postIdx, userIdx, count(postlikeIdx) as postLikeCount from PostLike where status = 'ACTIVE' group by postIdx) plc on plc.postIdx = p.postIdx\n" +
                "    LEFT JOIN (select postIdx, userIdx, status from PostLike where userIdx =?) pl on pl.postIdx = p.postIdx\n" +
                "    LEFT JOIN (select userIdx, nickname from User) u on u.userIdx = p.userIdx\n" +
                "    LEFT JOIN (select postIdx, count(commentIdx) as CommentCount from PostComment) cc on cc.postIdx = p.postIdx\n" +
                "WHERE p.status = 'ACTIVE' and p.postIdx=?";
        Object []getPostParams = new Object[] {userIdx,postIdx};
        return this.jdbcTemplate.queryForObject(getPostQuery,
                (rs, rowNum) -> new GetPostDetailRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("ServiceName"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("SVC_ID"),
                        rs.getInt("postLikeCount"),
                        likeNickList = this.jdbcTemplate.query(likeNickListQuery,
                                (rk2, rowNum3) -> new GetPostLikeNick(
                                        rk2.getString("nickname")
                                ),rs.getInt("postIdx")),
                        rs.getString("postLikeCheck"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        ImgDataList = this.jdbcTemplate.query(ImgDataListQuery,
                                (rk, rowNum2) -> new GetImgData(
                                        rk.getInt("postImgIdx"),
                                        rk.getString("origFileName"),
                                        rk.getString("fileName"),
                                        rk.getString("filePath"),
                                        rk.getLong("fileSize"))
                                ,rs.getInt("postIdx")),
                        rs.getInt("CommentCount"),
                        CommentList = this.jdbcTemplate.query("SELECT pc.commentIdx,pc.userIdx,pc.postIdx,u.nickname,IF(pc.status = 'ACTIVE', pc.content, '삭제된 댓글입니다') as content,pc.status,pc.createdAt,pc.updatedAt\n" +
                                        "FROM PostComment as pc\n" +
                                        "         LEFT JOIN (select userIdx, nickname from User) u on u.userIdx = pc.userIdx\n" +
                                        "WHERE pc.postIdx = ? and pc.parentCommentIdx = 0\n" +
                                        "ORDER BY pc.createdAt ASC",
                                (rl, rowNum3) -> new GetPostCommentDetail(
                                        rl.getInt("commentIdx"),
                                        rl.getInt("userIdx"),
                                        rl.getInt("postIdx"),
                                        rl.getString("nickname"),
                                        rl.getString("content"),
                                        rl.getString("status"),
                                        rl.getString("createdAt"),
                                        rl.getString("updatedAt"),
                                        childCommentList = this.jdbcTemplate.query(childCommentListQuery,
                                                (rl2, rowNum3_2) -> new GetPostComment(
                                                        rl2.getInt("commentIdx"),
                                                        rl2.getInt("userIdx"),
                                                        rl2.getInt("postIdx"),
                                                        rl2.getString("nickname"),
                                                        rl2.getString("content"),
                                                        rl2.getInt("parentCommentIdx"),
                                                        rl2.getString("status"),
                                                        rl2.getString("createdAt"),
                                                        rl2.getString("updatedAt")
                                                ),rl.getInt("commentIdx"))),rs.getInt("postIdx"))),getPostParams);
    }

    //특정 유저의 게시물 리스트 조회
    public List<GetPostsRes> getUserPosts(int userIdx) {
        String getPostsQuery = "SELECT p.postIdx,p.userIdx,u.nickname,ifnull(sL.ServiceName,'자유 게시물')as ServiceName,p.title,p.content,p.SVC_ID,IF(postLikeCount is null, 0, postLikeCount) as postLikeCount,IF(pl.status = 'ACTIVE', 'Y', 'N') as postLikeCheck,p.createdAt,p.updatedAt\n"+
                "FROM Post as p\n"+
                "   LEFT JOIN (select SVC_ID, serviceName from serviceList) sL on sL.SVC_ID = p.SVC_ID\n"+
                "   LEFT JOIN (select postIdx, userIdx, count(postlikeIdx) as postLikeCount from PostLike where status = 'ACTIVE' group by postIdx) plc on plc.postIdx = p.postIdx\n"+
                "   LEFT JOIN (select postIdx, userIdx, status from PostLike where userIdx =?) pl on pl.postIdx = p.postIdx\n"+
                "   LEFT JOIN (select userIdx, nickname from User) u on u.userIdx = p.userIdx\n"+
                "WHERE p.status = 'ACTIVE' and p.userIdx=?\n"+
                "ORDER BY p.createdAt DESC";
        Object []getPostsParams = new Object[] {userIdx,userIdx};
        return this.jdbcTemplate.query(getPostsQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("ServiceName"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("SVC_ID"),
                        rs.getInt("postLikeCount"),
                        likeNickList = this.jdbcTemplate.query(likeNickListQuery,
                                (rk2, rowNum3) -> new GetPostLikeNick(
                                        rk2.getString("nickname")
                                ),rs.getInt("postIdx")),
                        rs.getString("postLikeCheck"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        ImgDataList = this.jdbcTemplate.query(ImgDataListQuery,
                                (rk, rowNum2) -> new GetImgData(
                                        rk.getInt("postImgIdx"),
                                        rk.getString("origFileName"),
                                        rk.getString("fileName"),
                                        rk.getString("filePath"),
                                        rk.getLong("fileSize"))
                                ,rs.getInt("postIdx"))),getPostsParams);
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
    public int updateImg(int postImgIdx, PostImgData imgData){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String updateImgQuery = "UPDATE PostImgData SET origFileName = ?,fileName=?, filePath=?, fileSize=?,updatedAt=? WHERE postImgIdx=?";
        Object []updateImgParams = new Object[] {imgData.getOrigFileName(),imgData.getFileName(),imgData.getFilePath(),imgData.getFileSize(),sdf.format(timestamp),postImgIdx};
        return this.jdbcTemplate.update(updateImgQuery, updateImgParams);
    }

    //이미지 삭제
    public int deleteImg(String fileName){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String deleteImgQuery = "UPDATE PostImgData SET status=?, updatedAt=? WHERE fileName=?";
        Object []deleteImgParams = new Object[] {"DELETED",sdf.format(timestamp),fileName};
        return this.jdbcTemplate.update(deleteImgQuery, deleteImgParams);
    }

    //좋아요 생성
    public int createLike(int userIdx,int postIdx) {
        String insertLikeQuery = "INSERT INTO PostLike(userIdx,postIdx) VALUES (?,?)";
        Object []insertLikeParams = new Object[] {userIdx,postIdx};
        this.jdbcTemplate.update(insertLikeQuery,
                insertLikeParams);

        //방금 들어간 데이터의 idx 리턴
        String lastInsertIdxQuery="SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);
    }

    //좋아요 활성화
    public int activeLike(int userIdx,int postIdx) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String activeLikeQuery = "UPDATE PostLike SET status = 'ACTIVE', updatedAt=? WHERE userIdx = ? and postIdx = ?";
        Object []activeLikeParams = new Object[] {sdf.format(timestamp),userIdx,postIdx};
        this.jdbcTemplate.update(activeLikeQuery,
                activeLikeParams);

        //방금 들어간 데이터의 idx 리턴
        String lastInsertIdxQuery="SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);
    }

    //좋아요 비활성화
    public int inactiveLike(int userIdx,int postIdx) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String inactiveLikeQuery = "UPDATE PostLike SET status = 'INACTIVE', updatedAt=? WHERE userIdx = ? and postIdx = ?";
        Object []inactiveLikeParams = new Object[] {sdf.format(timestamp),userIdx,postIdx};
        this.jdbcTemplate.update(inactiveLikeQuery,
                inactiveLikeParams);

        //방금 들어간 데이터의 idx 리턴
        String lastInsertIdxQuery="SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);
    }

    //좋아요 활성화 여부 확인
    public int checkLikeActiveExist(int userIdx, int postIdx) {
        String checkLikeExistQuery = "select exists(select userIdx,postIdx from PostLike where userIdx = ? and postIdx = ? and status = 'ACTIVE')";
        Object []checkLikeExistParams = new Object[] {userIdx,postIdx};
        return this.jdbcTemplate.queryForObject(checkLikeExistQuery,
                int.class,
                checkLikeExistParams);
    }

    //좋아요 비활성화 여부 확인
    public int checkLikeInActiveExist(int userIdx, int postIdx) {
        String checkLikeInActiveExistQuery = "select exists(select userIdx,postIdx from PostLike where userIdx = ? and postIdx = ? and status = 'INACTIVE')";
        Object []checkLikeInActiveExistParams = new Object[] {userIdx,postIdx};
        return this.jdbcTemplate.queryForObject(checkLikeInActiveExistQuery,
                int.class,
                checkLikeInActiveExistParams);
    }

    //게시글 댓글 생성
    //게시물 생성
    public int insertComment(int userIdx, int postIdx, PostCommentReq postCommentReq) {
        String insertCommentQuery = "INSERT INTO PostComment(userIdx,postIdx,content,parentCommentIdx) VALUES (?,?,?,?)";
        Object []insertCommentParams = new Object[] {userIdx,postIdx,postCommentReq.getContent(),postCommentReq.getParentCommentIdx()};
        this.jdbcTemplate.update(insertCommentQuery, insertCommentParams);

        //방금 들어간 데이터의 idx 리턴
        String lastInsertIdxQuery="SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);
    }


    //댓글 commentIdx로 조회
    public GetPostComment getCommentByIdx(int commentIdx) {
        String getCommentByIdxQuery = "select pc.commentIdx,pc.userIdx,pc.postIdx,u.nickname,pc.content,pc.parentCommentIdx,pc.status,pc.createdAt,pc.updatedAt\n" +
                "from PostComment as pc\n" +
                "         LEFT JOIN (select userIdx, nickname from User) u on u.userIdx = pc.userIdx\n" +
                "where pc.commentIdx = ?";
        int getCommentByIdxParam = commentIdx;
        return this.jdbcTemplate.queryForObject(getCommentByIdxQuery,
                (rs, rowNum) -> new GetPostComment(
                        rs.getInt("commentIdx"),
                        rs.getInt("userIdx"),
                        rs.getInt("postIdx"),
                        rs.getString("nickname"),
                        rs.getString("content"),
                        rs.getInt("parentCommentIdx"),
                        rs.getString("status"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt")
                ),getCommentByIdxParam);
    }

    //댓글 비활성화
    public int inactiveComment(int commentIdx) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        String inactiveCommentQuery = "UPDATE PostComment SET status = 'INACTIVE', updatedAt=? WHERE commentIdx = ?";
        Object []inactiveCommentParams = new Object[] {sdf.format(timestamp),commentIdx};
        this.jdbcTemplate.update(inactiveCommentQuery,
                inactiveCommentParams);

        //방금 들어간 데이터의 idx 리턴
        String lastInsertIdxQuery="SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery,int.class);
    }
}
