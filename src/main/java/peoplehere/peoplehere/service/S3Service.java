
package peoplehere.peoplehere.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
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

    //파일의 ByteArray와 파일 정보를 통해 S3에 파일 업로드
    public String saveMultipartFileToS3(MultipartFile file) {

        //파일 이름 생성
        String fileName = createFileName(file.getOriginalFilename());

        //파일 변환
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        //파일 업로드
        try(InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, inputStream, objectMetadata).withCannedAcl(
                    CannedAccessControlList.PublicReadWrite)
            );
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("파일 변환 중 에러가 발생하였습니다. (%s)", file.getOriginalFilename()));
        }

        return fileName;
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