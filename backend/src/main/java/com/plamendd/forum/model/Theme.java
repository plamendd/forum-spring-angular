package com.plamendd.forum.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Community name is required.")
    private String name;

    @NotBlank(message = "Description is required.")
    private String description;

    @OneToMany(
            mappedBy = "theme",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<Post> posts  = new ArrayList<>();;

    public void addPosts(Post post) {
        posts.add(post);
        post.setTheme(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setTheme(null);
    }

    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Theme theme = (Theme) o;
        return id != null && Objects.equals(id, theme.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
