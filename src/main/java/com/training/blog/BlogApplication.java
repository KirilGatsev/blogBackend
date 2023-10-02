package com.training.blog;

import com.training.blog.models.Post;
import com.training.blog.models.User;
import com.training.blog.repository.PostRepository;
import com.training.blog.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(PostRepository postRepository, UserRepository userRepository){
		return args -> {
			User user = new User();
			user.setUsername("pa6ata");
			userRepository.save(user);

			Post post = new Post();
			post.setTitle("Po Gore6toto");
			post.setBody("Da se nahusva6 ot zlove6toto");
			post.setAuthor(user);
			postRepository.save(post);
		};
	}

}
