package com.manev.quislisting.domain.post;

import javax.persistence.*;

@Entity
@Table(name = "ql_postmeta")
public class PostMeta {

    public static final String META_KEY_EXPIRATION_DATE = "_expiration_date";
    public static final String META_KEY_ATTACHED_IMAGE = "_attached_image";
    public static final String META_KEY_CONTENT_FIELD_20 = "_content_field_20";
    public static final String META_KEY_CONTENT_FIELD_21 = "_content_field_21";
    public static final String META_KEY_CONTENT_FIELD_22 = "_content_field_22";
    public static final String META_KEY_ATTACHED_IMAGE_AS_LOGO = "_attached_image_as_logo";
    public static final String META_KEY_THUMBNAIL_ID = "_thumbnail_id";
    public static final String META_KEY_LISTING_STATUS = "_listing_status";
    public static final String META_KEY_POST_VIEWS_COUNT = "post_views_count";
    public static final String META_KEY_LOCATION_ID = "_location_id";
    public static final String META_KEY_ADDRESS_LINE_1 = "_address_line_1";
    public static final String META_KEY_ADDRESS_LINE_2 = "_address_line_2";
    public static final String META_KEY_MAP_COORDS_1 = "_map_coords_1";
    public static final String META_KEY_MAP_COORDS_2 = "_map_coords_2";
    public static final String META_VALUE_MAP_ZOOM = "11";
    public static final String META_KEY_CLICKS_DATA = "_clicks_data";
    public static final String META_KEY_TOTAL_CLICKS = "_total_clicks";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false, updatable=false)
    private AbstractPost abstractPost;

    public PostMeta() {
        // default constructor
    }

    public PostMeta(AbstractPost abstractPost, String key, String value) {
        this.abstractPost = abstractPost;
        this.key = key;
        this.value = value;
    }

    @Column
    private String key;

    @Column
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AbstractPost getAbstractPost() {
        return abstractPost;
    }

    public void setAbstractPost(AbstractPost abstractPost) {
        this.abstractPost = abstractPost;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
