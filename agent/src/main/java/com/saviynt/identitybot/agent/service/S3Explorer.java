/**
 * 
 */
package com.saviynt.identitybot.agent.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.saviynt.identitybot.agent.constants.Constants;

/**
 * @author venkat
 *
 */
@Component
public class S3Explorer {
	final static Logger log = Logger.getLogger(S3Explorer.class);

	@Value("${s3.access_key}")
	String Accesskey;

	@Value("${s3.secret_key}")
	String Secretkey;

	Regions clientRegion = Regions.DEFAULT_REGION;

	public static void main(String[] args) throws Exception {
		S3Explorer explorer = new S3Explorer();
		String bucketName = "*** Bucket name ***";
		String fileName = "*** Path to file to upload ***";
		File file = new File(fileName);
		String type = "RecordCLI";
		explorer.saveRecordedVideo(bucketName, file, type);
	}

	public void saveRecordedVideo(String bucketName, File file, String type) throws Exception {
		try {
			if (file == null || bucketName == null)
				throw new Exception("File object / Bucket Name is null.");
			Long fileLength = Long.valueOf(file.length());
			if (log.isDebugEnabled())
				log.debug("File:" + file + "BucketName:" + bucketName + "File Size:" + fileLength);
			/*
			 * if you want upload file into a folder, append folder name to object key
			 */
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(Accesskey, Secretkey);
			AmazonS3 s3Client = new AmazonS3Client(awsCreds);
			
			String key = null;
			if(type.equalsIgnoreCase("RecordCLI")) {
				key = "recordCLI" + "/" + file.getName();
			}
			
			// Upload a file as a new object with ContentType and title specified.
			PutObjectRequest request = new PutObjectRequest(bucketName, key, file);
			ObjectMetadata metadata = new ObjectMetadata();
			request.setMetadata(metadata);
			s3Client.putObject(request);
		} catch (AmazonServiceException ase) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			log.error("Error Message: " + ase.getMessage());
			log.error("HTTP Status Code: " + ase.getStatusCode());
			log.error("AWS Error Code: " + ase.getErrorCode());
			log.error("Error Type: " + ase.getErrorType());
			log.error("Request ID: " + ase.getRequestId());
		} catch (AmazonClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		}
	}
	
	public String readFileAsString(String bucketName, String fileName) throws Exception {
		InputStream input = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = readFileAsStream(bucketName, fileName);
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				sb.append(line);
				sb.append(Constants.csvNewLine);
			}
		} catch (AmazonServiceException ase) {
			log.error("Error Message:    " + ase.getMessage());
			log.error("HTTP Status Code: " + ase.getStatusCode());
			log.error("AWS Error Code:   " + ase.getErrorCode());
			log.error("Error Type:       " + ase.getErrorType());
			log.error("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			log.error("Error Message: " + ace.getMessage());
			log.error("Caught an AmazonClientException, which means" + " the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			log.error("Error Message: " + ace.getMessage());
		} finally {
			if (input != null)
				input.close();
			if (reader != null)
				reader.close();
		}
		return sb.toString();
	}
	
	public BufferedReader readFileAsStream(String bucketName, String fileName) throws Exception {
		InputStream input = null;
		try {
			if (fileName == null || bucketName == null)
				throw new Exception("File object / Bucket Name is null.");
			log.debug("Downloading an object");
			log.debug("bucketName: " + bucketName + "FileName: " + fileName);
			
			BasicAWSCredentials awsCreds = new BasicAWSCredentials(Accesskey, Secretkey);
			AmazonS3 s3Client = new AmazonS3Client(awsCreds);
			S3Object s3object = s3Client
					.getObject(new GetObjectRequest(bucketName, fileName));
			input = s3object.getObjectContent();
		} catch (AmazonServiceException ase) {
			log.error("Error Message:    " + ase.getMessage());
			log.error("HTTP Status Code: " + ase.getStatusCode());
			log.error("AWS Error Code:   " + ase.getErrorCode());
			log.error("Error Type:       " + ase.getErrorType());
			log.error("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			log.error("Error Message: " + ace.getMessage());
			log.error("Caught an AmazonClientException, which means" + " the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			log.error("Error Message: " + ace.getMessage());
		}
		return new BufferedReader(new InputStreamReader(input));
	}
	public List<String> listObjectKeys(String bucketName, String folderPrefix) throws Exception {
		BasicAWSCredentials awsCreds = null;
		List<String> files = new ArrayList<String>();
		try {
			checkForNulls(bucketName);
			if (log.isDebugEnabled()) {
				log.debug("bucketName: " + bucketName);
			}
			awsCreds = new BasicAWSCredentials(Accesskey, Secretkey);
			AmazonS3 s3Client = new AmazonS3Client(awsCreds);
			for (S3ObjectSummary summary : S3Objects.withPrefix(s3Client, bucketName, folderPrefix)) {
				if (null != summary && summary.getSize() != 0)
					files.add(summary.getKey());
			}

		} catch (AmazonServiceException ase) {
			log.error("Error Message:    " + ase.getMessage());
			log.error("HTTP Status Code: " + ase.getStatusCode());
			log.error("AWS Error Code:   " + ase.getErrorCode());
			log.error("Error Type:       " + ase.getErrorType());
			log.error("Request ID:       " + ase.getRequestId());
			throw ase;
		} catch (AmazonClientException ace) {
			log.error("Error Message: " + ace.getMessage());
			log.error("Caught an AmazonClientException, which means" + " the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			log.error("Error Message: " + ace.getMessage());
			throw ace;
		} catch (Exception ex) {
			log.error("Error Message:    " + ex.getMessage());
			throw ex;
		}
		return files;
	}
	
	private void checkForNulls(String checkString) throws Exception {
		if (checkString == null)
			throw new Exception(checkString + "is Null.");
	}
}
