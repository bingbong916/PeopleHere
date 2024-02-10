
package peoplehere.peoplehere.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String getPictureS3Url(String storedName) {
        return amazonS3Client.getUrl(bucket, storedName).toString();
    }

    //S3에 파일 저장 후 UUID로 생성 된 이름 return
    public String saveByteArrayToS3(byte[] pictureFileByteArray, String originalFileName) {
        String storedFileName = createFileName(originalFileName);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(getFileExtension(originalFileName));
        amazonS3Client.putObject(bucket, storedFileName, new ByteArrayInputStream(pictureFileByteArray), metadata);
        return storedFileName;
    }

    //파일 이름 생성 로직
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    //파일의 확장자명을 가져오는 로직
    private String getFileExtension(String fileName){
        try{
            return fileName.substring(fileName.lastIndexOf("."));
        }catch(StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다.",fileName));
        }
    }
}