package com.informe.informeapisb.src.community;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.informe.informeapisb.src.community.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.informe.informeapisb.config.BaseResponseStatus.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class CommunityService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommunityDao communityDao;
    private final CommunityProvider communityProvider;
    private final JwtService jwtService;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Autowired
    public CommunityService(CommunityDao communityDao, CommunityProvider communityProvider, JwtService jwtService, AmazonS3 amazonS3) {
        this.communityDao = communityDao;
        this.communityProvider = communityProvider;
        this.jwtService = jwtService;
        this.amazonS3 = amazonS3;
    }

    @Transactional(rollbackFor = BaseException.class)
    public PostPostsRes createPost(int userIdx, PostPostsReq postPostsReq, List<MultipartFile> files) throws BaseException {
        try{
            //게시글 작성
            int postIdx = communityDao.insertPolicyPost(userIdx, postPostsReq.getTitle(), postPostsReq.getContent(), postPostsReq.getSVC_ID());

            //이미지 삽입(이미지 없을 시 삽입 안함)
            if(files.get(0).isEmpty()){
                return new PostPostsRes(postIdx);
            }else{
                for (int i = 0; i< files.size(); i++){
                    String origFileName = files.get(i).getOriginalFilename();
                    String fileName = postIdx + "_" + i + "_" + origFileName;
                    String filePath = "postImg/" +fileName;
                    Long fileSize = files.get(i).getSize();
                    String contentType = files.get(i).getContentType();

                    //s3 이미지 삽입
                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    objectMetadata.setContentLength(fileSize);
                    objectMetadata.setContentType(contentType);
                    amazonS3.putObject(bucket, filePath, files.get(i).getInputStream(), objectMetadata);

                    //이미지 데이터 삽입
                    PostImgData Img = new PostImgData(origFileName, fileName,amazonS3.getUrl(bucket, filePath).toString(),fileSize);
                    communityDao.insertPostImg(postIdx,userIdx,Img);
                }
            }

            return new PostPostsRes(postIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {BaseException.class, Exception.class})
    //게시물 수정
    public void modifyPost(PatchPostsReq patchPostsReq, List<MultipartFile> files) throws BaseException {
        try{
            //수정하고자 하는 게시물에서 해당 게시물의 postIdx 를 인자로 받음
            //현재 수정을 원하는 user 의 userIdx 와 수정하고자하는 postIdx , 수정 내용 PatchPostsReq 를 인자로 받음
            //해당하는 postIdx 의 원래의 게시글을 불러옴
            GetPostsRes temp = communityProvider.getPost(patchPostsReq.getPostIdx(),patchPostsReq.getUserIdx());

            //원본 게시글의 작성자 Idx 와 현재 userIdx 와 비교를 통해 수정 가능한 게시물인지 검증
            if(patchPostsReq.getUserIdx() != temp.getUserIdx()){
                throw new BaseException(PATCH_POST_AUTH_ERROR);
            }

            //게시글 내용 같을 때 condition1 = True
            boolean condition1 = patchPostsReq.getTitle().equals(temp.getTitle()) && patchPostsReq.getContent().equals(temp.getContent()) && patchPostsReq.getSVC_ID().equals(temp.getSVC_ID());
            int condition2 = 0;

            //사진의 수가 원래 게시물보다 적거나 같을 때 (files <= temp.getImgDataList)
            if(files.size() <= temp.getImgDataList().size()){
                //원래 있던 이미지와 같은 이미지면 수정 x
                //다른 이미지(이미지 순서도 포함)면 추가 -> (s3에 기존에 있던 이미지는 삭제후 새 이미지 저장, postImgData 업데이트)
                //files가 비어있다면 (s3에 기존에 있던 다른 이미지들 삭제, postImgData 업데이트)
                if(!files.get(0).isEmpty()) {
                    for (int i = 0; i < files.size(); i++) {
                        String origFileName = files.get(i).getOriginalFilename();
                        Long fileSize = files.get(i).getSize();
                        String fileName = patchPostsReq.getPostIdx() + "_" + i + "_" + origFileName;
                        String filePath = "postImg/" + fileName;
                        String contentType = files.get(i).getContentType();
                        String fileName_del = temp.getImgDataList().get(i).getFileName();
                        if (fileName.equals(fileName_del)) {
                            continue;
                        } else {
                            condition2++;
                            //DB상의 해당 파일 삭제 상태로 변경
                            communityDao.deleteImg(fileName_del);

                            //DB상의 해당 파일 삭제 상태로 변경
                            String filePath_del = "postImg/" + fileName_del;
                            amazonS3.deleteObject(bucket, filePath_del);

                            //다시 이미지 저장
                            //s3 이미지 삽입
                            ObjectMetadata objectMetadata = new ObjectMetadata();
                            objectMetadata.setContentLength(fileSize);
                            objectMetadata.setContentType(contentType);
                            amazonS3.putObject(bucket, filePath, files.get(i).getInputStream(), objectMetadata);

                            //이미지 데이터 삽입
                            PostImgData Img = new PostImgData(origFileName, fileName, amazonS3.getUrl(bucket, filePath).toString(), fileSize);
                            communityDao.insertPostImg(patchPostsReq.getPostIdx(), patchPostsReq.getUserIdx(), Img);
                        }
                    }

                    for (int i = files.size(); i < temp.getImgDataList().size(); i++) {
                        condition2++;
                        String fileName_del = temp.getImgDataList().get(i).getFileName();
                        //DB상의 해당 파일 삭제 상태로 변경
                        communityDao.deleteImg(fileName_del);

                        //DB상의 해당 파일 삭제 상태로 변경
                        String filePath_del = "postImg/" + fileName_del;
                        amazonS3.deleteObject(bucket, filePath_del);
                    }
                } else{
                    for (int i = 0; i < temp.getImgDataList().size(); i++) {
                        condition2++;
                        String fileName_del = temp.getImgDataList().get(i).getFileName();
                        //DB상의 해당 파일 삭제 상태로 변경
                        communityDao.deleteImg(fileName_del);

                        //DB상의 해당 파일 삭제 상태로 변경
                        String filePath_del = "postImg/" + fileName_del;
                        amazonS3.deleteObject(bucket, filePath_del);
                    }
                }
            } else if ((!files.get(0).isEmpty()) && (files.size() > temp.getImgDataList().size())){
                //file 이 비어 있지 않고 사진의 수가 원래 게시물보다 많을 때 (files > temp.getImgDataList)
                //기존에 있던 크기에서는 이미지 비교후 추가, 추가되는 크기는 새로 이미지 추가
                //원래 있던 이미지와 같은 이미지면 수정 x
                //다른 이미지(이미지 순서도 포함)면 추가 -> (s3에 기존에 있던 이미지는 삭제후 새 이미지 저장, postImgData 업데이트)
                for(int i =0;i<temp.getImgDataList().size(); i++){
                    String origFileName = files.get(i).getOriginalFilename();
                    Long fileSize = files.get(i).getSize();
                    String fileName = patchPostsReq.getPostIdx() + "_" + i + "_" + origFileName;
                    String filePath = "postImg/" +fileName;
                    String contentType = files.get(i).getContentType();
                    String fileName_del = temp.getImgDataList().get(i).getFileName();
                    if(fileName.equals(fileName_del)){
                        continue;
                    } else{
                        condition2++;
                        //DB상의 해당 파일 삭제 상태로 변경
                        communityDao.deleteImg(fileName_del);

                        //DB상의 해당 파일 삭제 상태로 변경
                        String filePath_del = "postImg/" + fileName_del;
                        amazonS3.deleteObject(bucket,filePath_del);

                        //다시 이미지 저장
                        //s3 이미지 삽입
                        ObjectMetadata objectMetadata = new ObjectMetadata();
                        objectMetadata.setContentLength(fileSize);
                        objectMetadata.setContentType(contentType);
                        amazonS3.putObject(bucket, filePath, files.get(i).getInputStream(), objectMetadata);

                        //이미지 데이터 삽입
                        PostImgData Img = new PostImgData(origFileName, fileName,amazonS3.getUrl(bucket, filePath).toString(),fileSize);
                        communityDao.insertPostImg(patchPostsReq.getPostIdx(), patchPostsReq.getUserIdx(),Img);
                    }
                }
                for(int i = temp.getImgDataList().size(); i<files.size();i++){
                    condition2++;
                    String origFileName = files.get(i).getOriginalFilename();
                    Long fileSize = files.get(i).getSize();
                    String fileName = patchPostsReq.getPostIdx() + "_" + i + "_" + origFileName;
                    String filePath = "postImg/" +fileName;
                    String contentType = files.get(i).getContentType();

                    //s3 이미지 삽입
                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    objectMetadata.setContentLength(fileSize);
                    objectMetadata.setContentType(contentType);
                    amazonS3.putObject(bucket, filePath, files.get(i).getInputStream(), objectMetadata);

                    //이미지 데이터 삽입
                    PostImgData Img = new PostImgData(origFileName, fileName,amazonS3.getUrl(bucket, filePath).toString(),fileSize);
                    communityDao.insertPostImg(patchPostsReq.getPostIdx(), patchPostsReq.getUserIdx(),Img);
                }
            }

            //사진을 한 번이라도 업데이트 했거나, 원본 게시글과 내용이 다르면 내용을 업데이트한다
            if((condition2>0) || (!condition1)){
                int result = communityDao.updatePost(patchPostsReq.getPostIdx(), patchPostsReq);
                if(result==0){
                    throw new BaseException(MODIFY_FAIL_POST);
                }
            }else{
                throw new BaseException(MODIFY_FAIL_POST_2);
            }
        }catch (BaseException exception){
            throw new BaseException(exception.getStatus());
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }


    //게시글 좋아요 api
    @Transactional(rollbackFor = BaseException.class)
    public void createLike(int userIdx, int postIdx) throws BaseException {
        try{
            //해당 userIdx와 postIdx로 좋아요가 비활성화 되어있는것이 있다면 활성화상태로 바꿈
            if(communityProvider.checkLikeInActiveExist(userIdx,postIdx) != 0){
                communityDao.activeLike(userIdx, postIdx);
                //해당 userIdx와 postIdx로 좋아요가 활성화 되어있는것이 없다면 새로 생성
            } else if (communityProvider.checkLikeActiveExist(userIdx,postIdx) == 0) {
                communityDao.createLike(userIdx, postIdx);
            }else {
                throw new BaseException(POST_LIKE_EXIST);
            }

        } catch(BaseException exception){
            throw new BaseException(exception.getStatus());
        }
    }

    //게시글 좋아요 취소 api
    @Transactional(rollbackFor = BaseException.class)
    public void cancelLike(int userIdx, int postIdx) throws BaseException {
        try{
            //해당 userIdx와 postIdx로 좋아요가 활성화 되어있는게 있다면 비활성화상태로 바꿈
            if(communityProvider.checkLikeActiveExist(userIdx,postIdx) != 0){
                communityDao.inactiveLike(userIdx, postIdx);
            }else {
                throw new BaseException(POST_LIKE_NOT_EXIST);
            }

        } catch(BaseException exception){
            throw new BaseException(exception.getStatus());
        }
    }

    //게시글 댓글 생성
    @Transactional(rollbackFor = BaseException.class)
    public PostCommentRes insertComment(int userIdx, int postIdx, PostCommentReq postCommentReq) throws BaseException {
        try{
            //게시글 작성
            int commentIdx = communityDao.insertComment(userIdx,postIdx, postCommentReq);

            return new PostCommentRes(commentIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //게시글 댓글 삭제
    @Transactional(rollbackFor = BaseException.class)
    public void cancelComment(int userIdx, int commentIdx) throws BaseException {
        try{
            //commentIdx로 가져온 댓글과 현재 접속한 userIdx가 같은지 확인
            GetPostComment getPostComment = communityProvider.getCommentByIdx(commentIdx);
            if(userIdx != getPostComment.getUserIdx()){
                throw new BaseException(POST_COMMENT_CANT_CHANGE);
            }

            communityDao.inactiveComment(commentIdx);

        } catch(BaseException exception){
            throw new BaseException(exception.getStatus());
        }
    }
}
