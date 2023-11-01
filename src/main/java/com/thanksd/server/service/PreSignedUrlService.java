package com.thanksd.server.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.thanksd.server.domain.Diary;
import com.thanksd.server.dto.response.PreSignedUrlResponse;
import com.thanksd.server.exception.badrequest.InvalidImageNameException;
import com.thanksd.server.exception.notfound.NotFoundDiaryException;
import com.thanksd.server.repository.DiaryRepository;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreSignedUrlService {
    private final AmazonS3Client amazonS3Client;
    private final DiaryRepository diaryRepository;
    private String savedImageName;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String location;

    public PreSignedUrlResponse getPreSignedUrl(String prefixImagePath, String imageName, Long memberId) {

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(
                prefixImagePath, imageName, memberId);

        return new PreSignedUrlResponse(generatePreSignedUrl(generatePresignedUrlRequest));
    }

    private String generatePreSignedUrl(GeneratePresignedUrlRequest generatePresignedUrlRequest) {

        String preSignedUrl = null;
        try {
            preSignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (AmazonServiceException e) {
            throw new InvalidImageNameException();
        }
        return preSignedUrl;
    }

    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String prefixImagePath, String imageName,
                                                                       Long memberId) {
        savedImageName = uniqueImageName(imageName, memberId);

        String savedImagePath = savedImageName;
        if (!prefixImagePath.isBlank()) {
            savedImagePath = prefixImagePath + "/" + savedImageName;
        }
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket,
                savedImagePath);
        return generatePresignedUrlRequest;
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String imageName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, imageName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String uniqueImageName(String imageName, Long memberId) {
        return memberId + "/" + UUID.randomUUID() + "_" + imageName;
    }

    /**
     * image path 형식 : "images" + "/" + memberId + "/" + UUID + "_" + imageName + 확장자;
     */
    public void deleteByPath(Long id) {
        Diary findDiary = diaryRepository.findById(id)
                .orElseThrow(NotFoundDiaryException::new);

        try {
            amazonS3Client.deleteObject(bucket, findDiary.getImage());
        } catch (AmazonServiceException e) {
            throw new InvalidImageNameException();
        }
    }
}
