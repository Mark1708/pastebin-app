package mark1708.com.pastebin.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mark1708.com.pastebin.exception.http.BadRequestException;
import mark1708.com.pastebin.exception.http.ResourceNotFoundException;
import mark1708.com.pastebin.model.enums.QueryType;
import mark1708.com.pastebin.model.enums.ResourceType;
import mark1708.com.pastebin.service.ContentStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentStorageServiceImpl implements ContentStorageService {

  @Value("${minio.bucket.name}")
  private String bucketName;

  @Value("${minio.folders.bin}")
  private String folder;

  private final MinioClient minioClient;

  @Override
  public String uploadPasteContent(String hash, String content) {
    String path = folder + hash;
    byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
    try {
      minioClient.putObject(
          PutObjectArgs.builder()
              .bucket(bucketName)
              .object(path)
              .contentType("application/octet-stream")
              .stream(new ByteArrayInputStream(contentBytes), contentBytes.length, -1)
              .build()
      );
    } catch (Exception e) {
      throw new BadRequestException("Error during upload content in S3 storage", e);
    }
    return path;
  }

  @Override
  public String getPasteContent(String path) {
    try (InputStream stream =
        minioClient.getObject(
            GetObjectArgs.builder()
            .bucket(bucketName)
                .object(path)
                .build())
    ) {
      return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ResourceNotFoundException(ResourceType.CONTENT, QueryType.PATH, path);
    }
  }
}
