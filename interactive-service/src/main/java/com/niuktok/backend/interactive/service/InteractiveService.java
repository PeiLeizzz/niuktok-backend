package com.niuktok.backend.interactive.service;

public interface InteractiveService {
    void view(Long userID, long videoID, Integer progress);

    void like(Long userID, Long videoID);

    void cancelLike(Long userID, long videoID);

    void favorite(Long userID, long videoID);

    void cancelFavorite(Long userID, long videoID);

    String share(Long userID, long videoID);
}
