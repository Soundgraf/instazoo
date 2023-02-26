package com.example.instazoo.service;

import com.example.instazoo.exception.ImageNotFoundException;
import com.example.instazoo.model.ImageModel;
import com.example.instazoo.model.Post;
import com.example.instazoo.model.User;
import com.example.instazoo.repository.ImageRepository;
import com.example.instazoo.repository.PostRepository;
import com.example.instazoo.repository.UserRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        log.info("Uploading image profile to User: {}", user.getUsername());

        ImageModel userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getName());
        return imageRepository.save(imageModel);
    }

    public ImageModel uploadFileToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        User user = getUserByPrincipal(principal);
        Post post = user.getPosts().stream()
            .filter(p -> p.getId().equals(postId))
            .collect(toSinglePostCollector());
        ImageModel imageModel = new ImageModel();
        imageModel.setPostId(post.getId());
        imageModel.setImageBytes(file.getBytes());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        log.info("Uploading image to Post: {}", post.getId());
        return imageRepository.save(imageModel);
    }

    public ImageModel getImageToUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        ImageModel imageModel = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    public ImageModel getImageToPost(Long postId) {
        ImageModel imageModel = imageRepository.findByPostId(postId)
            .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Post: {}" + postId));
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found with Username: " + username));
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Cannot compress bytes");
        }
        System.out.println("Compressed image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            log.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {
        return Collectors.collectingAndThen(
            Collectors.toList(),
            list -> {
                if (list.size() != 1) {
                    throw new IllegalStateException();
                }
                return list.get(0);
            }
        );
    }
}
