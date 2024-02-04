package peoplehere.peoplehere.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public byte[] getPictureBytesFromS3(String storedName) throws IOException {
        S3Object object = amazonS3Client.getObject(bucket, storedName);
        S3ObjectInputStream is = object.getObjectContent();
        return is.readAllBytes();
    }

    public String getPictureS3Url(String storedName) {
        return amazonS3Client.getUrl(bucket, storedName).toString();
    }

    public void saveByteArrayToS3(byte[] pictureFileByteArray, String storedPictureName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(extractExt(storedPictureName));
        amazonS3Client.putObject(bucket, storedPictureName,
            new ByteArrayInputStream(pictureFileByteArray), metadata);
    }

    private String extractExt(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx == -1) {
            return "";
        } else {
            return filename.substring(idx + 1);
        }
    }


}

