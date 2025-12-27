package com.dearhaeny.dearhaeny.post.query.dto;

import java.util.List;

public record PostListResultResponse(
        String selectedType,   // ALL / NEW_YEAR_WISH / INNER_THOUGHT / COURAGE
        long totalCount,
        List<PostListResponse> posts
) {}
