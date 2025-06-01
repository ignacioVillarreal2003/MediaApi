package com.api.mediaapi.infrastructure.azure.blobstorage;

import com.api.mediaapi.config.properties.AzureBlobStorageProperties;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureBlobStorageService {

    private final AzureBlobStorageProperties azureBlobStorageProperties;

    private BlobContainerClient getContainerClient() {
        try {
            BlobContainerClient containerClient = new BlobServiceClientBuilder()
                    .connectionString(azureBlobStorageProperties.getConnectionString())
                    .buildClient()
                    .getBlobContainerClient(azureBlobStorageProperties.getContainerName());

            if (!containerClient.exists()) {
                containerClient.create();
            }

            return containerClient;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getPublicImageUrl(String blobName) {
        if (blobName == null || blobName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        BlobContainerClient containerClient = getContainerClient();
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        if (!blobClient.exists()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            BlobSasPermission permission = new BlobSasPermission().setReadPermission(true);
            OffsetDateTime expiryTime = OffsetDateTime.now().plusHours(1);
            BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expiryTime, permission);
            String sasToken = blobClient.generateSas(sasValues);
            return blobClient.getBlobUrl() + "?" + sasToken;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String uploadImage(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        BlobContainerClient containerClient = getContainerClient();
        String filename = UUID.randomUUID() + ".jpg";
        BlobClient blobClient = containerClient.getBlobClient(filename);

        try (InputStream inputStream = new java.io.ByteArrayInputStream(imageBytes)) {
            blobClient.upload(inputStream, imageBytes.length, true);
            return blobClient.getBlobName();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteImage(String blobName) {
        if (blobName == null || blobName.trim().isEmpty()) {
            return;
        }

        BlobContainerClient containerClient = getContainerClient();
        BlobClient blobClient = containerClient.getBlobClient(blobName);

        try {
            if (blobClient.exists()) {
                blobClient.delete();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

