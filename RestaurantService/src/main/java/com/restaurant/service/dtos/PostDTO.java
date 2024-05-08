package com.restaurant.service.dtos;

import com.restaurant.service.entities.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String image;
    private String content;
    private String created_actor;
    private String create_at;
    private int user_seen;
    private boolean archived; // Thêm trường này

    public PostDTO(Long id, String title, String image, String content, String created_actor, String create_at,
			int user_seen, boolean archived) {
		super();
		this.id = id;
		this.title = title;
		this.image = image;
		this.content = content;
		this.created_actor = created_actor;
		this.create_at = create_at;
		this.user_seen = user_seen;
		this.archived = archived;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreated_actor() {
		return created_actor;
	}

	public void setCreated_actor(String created_actor) {
		this.created_actor = created_actor;
	}

	public String getCreate_at() {
		return create_at;
	}

	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}

	public int getUser_seen() {
		return user_seen;
	}

	public void setUser_seen(int user_seen) {
		this.user_seen = user_seen;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public static PostDTO fromEntity(Post post) {
        return new PostDTO(
            post.getId(),
            post.getTitle(),
            post.getImage(),
            post.getContent(),
            post.getCreated_actor(),
            post.getCreate_at(),
            post.getUser_seen(),
            post.isArchived() // Đảm bảo rằng bạn đã thêm getter isArchived() trong entity Post của bạn
        );
    }
}
