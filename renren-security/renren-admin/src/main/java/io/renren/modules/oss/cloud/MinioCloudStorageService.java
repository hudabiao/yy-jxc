package io.renren.modules.oss.cloud;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.renren.modules.oss.prop.MinioConfigProperties;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MinioCloudStorageService {

    @Autowired
    private MinioConfigProperties minioConfigProperties;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String upload(MultipartFile file) throws IOException {
        String endpoint = minioConfigProperties.getEndpoint();
        String accessKey = minioConfigProperties.getAccessKey();
        String secretKey = minioConfigProperties.getSecretKey();
        String bucket = minioConfigProperties.getBucket();
        InputStream inputStream = null;
        ByteArrayOutputStream out = null;
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            MinioClient minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
            inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            // 压缩
            out = new ByteArrayOutputStream();
            Thumbnails.of(inputStream).scale(0.5).outputQuality(0.6).toOutputStream(out);
            byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
            // 上传
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(originalFilename).stream(byteArrayInputStream, byteArrayInputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build());
            // 返回-文件名
            return originalFilename;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage());
        } finally {
            IOUtils.close(inputStream);
            IOUtils.close(out);
            IOUtils.close(byteArrayInputStream);
        }
    }

    public void upload(InputStream inputStream, String fileName, String contentType) throws IOException {
        String endpoint = minioConfigProperties.getEndpoint();
        String accessKey = minioConfigProperties.getAccessKey();
        String secretKey = minioConfigProperties.getSecretKey();
        String bucket = minioConfigProperties.getBucket();
        try {
            MinioClient minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
            // 上传
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName).stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build());
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException(e.getMessage());
        } finally {
            IOUtils.close(inputStream);
        }
    }


    /**
     * 获取url
     *
     * @param fileName 文件名称
     * @return {@link String}
     */
    public String getUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = "default.png";
        }
        String url = redisTemplate.opsForValue().get(fileName);
        if (url != null) {
            return url;
        }
        String endpoint = minioConfigProperties.getEndpoint();
        String accessKey = minioConfigProperties.getAccessKey();
        String secretKey = minioConfigProperties.getSecretKey();
        String bucket = minioConfigProperties.getBucket();
        MinioClient minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
        try {
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .method(Method.GET)
                            .object(fileName)
                            .expiry(7, TimeUnit.DAYS)
                            .build());
            redisTemplate.opsForValue().set(fileName, url, 6, TimeUnit.DAYS);
            return url;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }
}
