package com.makersacademy.acebook.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PostTest {

	private Post post = new Post("hello", LocalDate.now(), 0);

	@Test
	public void postHasContent() {
		assertThat(post.getContent(), containsString("hello"));
	}

}
